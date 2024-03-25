package pe.gob.muniveintiseisdeoctubre.servicios.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MulticastMessage;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.gob.muniveintiseisdeoctubre.servicios.dto.HeatMapDataDTO;
import pe.gob.muniveintiseisdeoctubre.servicios.entity.Report;
import pe.gob.muniveintiseisdeoctubre.servicios.entity.Sector;
import pe.gob.muniveintiseisdeoctubre.servicios.repository.ReportRepository;
import pe.gob.muniveintiseisdeoctubre.servicios.repository.SessionRepository;
import pe.gob.muniveintiseisdeoctubre.servicios.service.CrudService;
import pe.gob.muniveintiseisdeoctubre.servicios.util.Constants;
import pe.gob.muniveintiseisdeoctubre.servicios.util.GeoUtil;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ReportService implements CrudService<Report, Integer> {

    private static final Logger LOG = LoggerFactory.getLogger(ReportService.class);

    @PersistenceContext
    private EntityManager entityManager;

    private ReportRepository repository;

    private CategoryService categoryService;

    private SectorService sectorService;

    private GeoUtil geoUtil;

    private SessionRepository sessionRepository;

    private Constants constants;

    private final ObjectMapper objectMapper;


    @Autowired
    public ReportService(Constants constants, ReportRepository repository, CategoryService categoryService, GeoUtil geoUtil,
    SessionRepository sessionRepository, SectorService sectorService) {
        this.constants = constants;
        this.sectorService = sectorService;
        this.repository = repository;
        this.categoryService = categoryService;
        this.geoUtil = geoUtil;
        this.sessionRepository = sessionRepository;
        try {
            LOG.info("Firebase init");
            LOG.info(constants.serviceAccountPath);
            FileInputStream serviceAccount = new FileInputStream(constants.serviceAccountPath);
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();
            FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            LOG.error(e.getMessage());
        }
        objectMapper = new ObjectMapper();
    }

    @Override
    public Report createOrUpdate(final Report report) {
        List<Sector> sectors = sectorService.findAll();
        Optional<Sector> sector = sectors.stream().filter(s -> geoUtil.insidePolygon(s.getPoints(), report.getLatitude(), report.getLongitude())).findFirst();

        if (sector.isPresent()) {
            report.setSector(sector.get());
            LOG.info("result :" + sector);
        } else {
            LOG.error("Reporte sin sector encontrado");
        }

        try {
            final var registrationTokens = sessionRepository.getActiveTokenAgentsSessions();
            var title = report.getId() == null ? "Alerta registrada " : "Alerta actualizada ";
            Report reportSaved = repository.save(report);
            title = title + reportSaved.getId();

            final var category = categoryService.find(report.getCategory().getId()).get();

            if (registrationTokens != null && !registrationTokens.isEmpty() && category.getType() == 1) {
                MulticastMessage message = MulticastMessage.builder()
                        .putData("data", objectMapper.writeValueAsString(report))
                        .putData("title", title)
                        .putData("condition", "true")
                        .putData("body", report.getCategory().getTitle() != null ? report.getCategory().getTitle() : "")
                        .addAllTokens(registrationTokens)
                        .build();

                final var response = FirebaseMessaging.getInstance().sendMulticast(message);
                LOG.info(response.getSuccessCount() + " messages were sent successfully");
            }

            return report;

        } catch (FirebaseMessagingException | IllegalStateException e) {
            LOG.error(e.getMessage());
            return report;
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return null;
        }
    }

    @Transactional
    public boolean updateStatus(Integer reportId, Constants.ReportStatus status) {
        var report = entityManager.getReference(Report.class, reportId);
        if (status == Constants.ReportStatus.PENDING) {
            return false;
        }
        report.setStatus(status);
        entityManager.merge(report);
        return true;
    }

    @Override
    public Optional<Report> find(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Iterable<Report> findAll() {
        return repository.findAll();
    }

    public Iterable<Report> findAllPrimary() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, -48);

        return entityManager.createQuery("SELECT r FROM Report r INNER JOIN r.category c where c.type = 1 AND r.status <> ?1 AND r.createdAt > ?2 ORDER BY r.id DESC", Report.class)
                .setParameter(1, Constants.ReportStatus.DISCARDED)
                .setParameter(2, calendar.getTime())
                .getResultList();
    }

    public Iterable<Report> findAllSecondary() {
        return entityManager.createQuery("SELECT r FROM Report r INNER JOIN r.category c where c.type = 0 ORDER BY r.id DESC", Report.class).setMaxResults(100).getResultList();
    }

    public Iterable<HeatMapDataDTO> getHeatMapData(Map<String, Object> filters) {
        final var lStatusFilter = new ArrayList<>();
        ((List<String>) filters.get("status")).forEach(item -> {
            lStatusFilter.add(Constants.ReportStatus.valueOf(item));
        });
        return entityManager.createQuery(

                        "SELECT NEW pe.gob.muniveintiseisdeoctubre.servicios.dto.HeatMapDataDTO(r.latitude, r.longitude, CAST(COALESCE(i.severity, 0.5) AS double)) FROM Report r " +
                                "LEFT JOIN r.incidence i WHERE (r.createdAt BETWEEN :startDate and :endDate) AND COALESCE(r.incidence.id, 0) IN (:incidences) AND COALESCE(r.sector.id, 0) IN (:sectors)" +
                                " AND r.status in (:status)", HeatMapDataDTO.class)
                .setParameter("startDate", new Date((Long) filters.get("startDate")))
                .setParameter("endDate", new Date((Long) filters.get("endDate")))
                .setParameter("incidences", filters.get("incidences"))
                .setParameter("sectors", filters.get("sectors"))
                .setParameter("status", lStatusFilter)
                .getResultList();
    }

    public Iterable<Report> consultar(HashMap<String, Object> filters) {
        final var lStatusFilter = new ArrayList<>();

        if (filters != null && !filters.isEmpty()) {
            ((List<String>) filters.get("status")).forEach(item -> {
                lStatusFilter.add(Constants.ReportStatus.valueOf(item));
            });
        }

        return entityManager.createQuery("SELECT r FROM Report r " +
                        "WHERE (r.createdAt BETWEEN :startDate and :endDate) AND COALESCE(r.incidence.id, 0) IN (:incidences) " +
                        "AND COALESCE(r.sector.id, 0) IN (:sectors) AND r.status in (:status) ORDER BY r.id DESC", Report.class)
                .setParameter("startDate", new Date((Long) filters.get("startDate")))
                .setParameter("endDate", new Date((Long) filters.get("endDate")))
                .setParameter("incidences", filters.get("incidences"))
                .setParameter("sectors", filters.get("sectors"))
                .setParameter("status", lStatusFilter)
                .getResultList();
    }

    @Override
    public boolean delete(Integer id) {
        repository.deleteById(id);
        return true;
    }

    public XSSFWorkbook generateExcel(Iterable<Report> report, String[] campos, HashMap<String, Object> filters) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();
        XSSFCellStyle style = workbook.createCellStyle();
        XSSFRow row = sheet.createRow(0);
        XSSFCell cell = row.createCell(0);
        XSSFFont font = workbook.createFont();
        SimpleDateFormat ft = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
        SimpleDateFormat fecha = new SimpleDateFormat("d-M-yyyy hh:mm");

        //Fuente y estilo del titulo
        font.setBold(true);
        font.setColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
        font.setFontName("Verdana, Geneva, Tahoma, sans-serif");
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(HSSFColor.HSSFColorPredefined.RED.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cell.setCellStyle(style);
        cell.setCellValue(constants.nameMunicipalidad);
        sheet.addMergedRegion(new CellRangeAddress(0, 4, 0, 11));
        //Fin

        //Fuente y estilo de cabecera
        XSSFCellStyle stylecell = workbook.createCellStyle();
        XSSFFont fontcell = workbook.createFont();
        stylecell.setAlignment(HorizontalAlignment.CENTER);
        stylecell.setVerticalAlignment(VerticalAlignment.CENTER);
        stylecell.setFillForegroundColor(HSSFColor.HSSFColorPredefined.DARK_RED.getIndex());
        stylecell.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        fontcell.setBold(true);
        fontcell.setColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
        fontcell.setFontName("Arial");
        stylecell.setFont(fontcell);
        //Fin

        //Fuente y estilo de las columnas
        XSSFCellStyle stylecellcolumn = workbook.createCellStyle();
        XSSFFont fontcellcolumn = workbook.createFont();
        stylecellcolumn.setAlignment(HorizontalAlignment.CENTER);
        stylecellcolumn.setVerticalAlignment(VerticalAlignment.CENTER);
        stylecellcolumn.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        stylecellcolumn.setFillForegroundColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
        fontcellcolumn.setBold(true);
        fontcellcolumn.setColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        fontcellcolumn.setFontName("Arial");
        stylecellcolumn.setFont(fontcellcolumn);
        //Fin

        XSSFCellStyle stylecellcolumndate = workbook.createCellStyle();
        XSSFFont fontcellcolumndate = workbook.createFont();
        stylecellcolumndate.setAlignment(HorizontalAlignment.CENTER);
        stylecellcolumndate.setVerticalAlignment(VerticalAlignment.CENTER);
        stylecellcolumndate.setFillForegroundColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
        stylecellcolumndate.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        stylecellcolumndate.setDataFormat(BuiltinFormats.getBuiltinFormat("d-m-Y"));
        fontcellcolumndate.setBold(true);
        fontcellcolumndate.setColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        fontcellcolumndate.setFontName("Arial");
        stylecellcolumndate.setFont(fontcellcolumndate);

        XSSFCellStyle stylecellcolumnred = workbook.createCellStyle();
        stylecellcolumnred.setAlignment(HorizontalAlignment.CENTER);
        stylecellcolumnred.setVerticalAlignment(VerticalAlignment.CENTER);
        stylecellcolumnred.setFillForegroundColor(HSSFColor.HSSFColorPredefined.RED.getIndex());
        stylecellcolumnred.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        stylecellcolumnred.setFont(fontcellcolumn);

        XSSFCellStyle stylecellcolumnyellow = workbook.createCellStyle();
        stylecellcolumnyellow.setAlignment(HorizontalAlignment.CENTER);
        stylecellcolumnyellow.setVerticalAlignment(VerticalAlignment.CENTER);
        stylecellcolumnyellow.setFillForegroundColor(HSSFColor.HSSFColorPredefined.YELLOW.getIndex());
        stylecellcolumnyellow.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        stylecellcolumnyellow.setFont(fontcellcolumn);

        XSSFCellStyle stylecellcolumnorange = workbook.createCellStyle();
        stylecellcolumnorange.setAlignment(HorizontalAlignment.CENTER);
        stylecellcolumnorange.setVerticalAlignment(VerticalAlignment.CENTER);
        stylecellcolumnorange.setFillForegroundColor(HSSFColor.HSSFColorPredefined.ORANGE.getIndex());
        stylecellcolumnorange.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        stylecellcolumnorange.setFont(fontcellcolumn);

        XSSFCellStyle stylecellcolumngray = workbook.createCellStyle();
        stylecellcolumngray.setAlignment(HorizontalAlignment.CENTER);
        stylecellcolumngray.setVerticalAlignment(VerticalAlignment.CENTER);
        stylecellcolumngray.setFillForegroundColor(HSSFColor.HSSFColorPredefined.GREY_25_PERCENT.getIndex());
        stylecellcolumngray.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        stylecellcolumngray.setFont(fontcellcolumn);

        XSSFCellStyle stylecellcolumngreen = workbook.createCellStyle();
        stylecellcolumngreen.setAlignment(HorizontalAlignment.CENTER);
        stylecellcolumngreen.setVerticalAlignment(VerticalAlignment.CENTER);
        stylecellcolumngreen.setFillForegroundColor(HSSFColor.HSSFColorPredefined.GREEN.getIndex());
        stylecellcolumngreen.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        stylecellcolumngreen.setFont(fontcellcolumn);

        List<Report> item = (List<Report>) report;

        //Datos de Filtros
        row = sheet.createRow(6);
        cell = row.createCell(1);
        cell.setCellValue("FECHA DE INICIO : ");
        cell.setCellStyle(stylecellcolumn);
        sheet.addMergedRegion(new CellRangeAddress(6, 6, 1, 2));

        cell = row.createCell(3);
        cell.setCellValue(fecha.format(filters.get("startDate")));
        cell.setCellStyle(stylecellcolumn);

        cell = row.createCell(4);
        cell.setCellValue("CANTIDAD DE REPORTES : ");
        cell.setCellStyle(stylecellcolumn);
        sheet.addMergedRegion(new CellRangeAddress(6, 6, 4, 6));

        cell = row.createCell(7);
        cell.setCellValue(item.size() == 0 ? 0 : item.size());
        cell.setCellStyle(stylecellcolumn);


        cell = row.createCell(8);
        cell.setCellValue("FECHA DE FIN : ");
        cell.setCellStyle(stylecellcolumn);
        sheet.addMergedRegion(new CellRangeAddress(6, 6, 8, 9));

        cell = row.createCell(10);
        cell.setCellValue(fecha.format(filters.get("endDate")));
        cell.setCellStyle(stylecellcolumn);

        //HEADER
        row = sheet.createRow(8);
        for (int i = 0; i < campos.length; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(stylecell);
            cell.setCellValue(campos[i]);
            cell.setCellType(CellType.STRING);
        }
        //FIN

        int numrow = 10;

        if (item != null) {
            for (Report rp : item) {
                row = sheet.createRow(numrow++);
                cell = row.createCell(0);
                cell.setCellStyle(stylecellcolumn);
                cell.setCellValue(rp.getId());
                cell.setCellType(CellType.NUMERIC);

                cell = row.createCell(1);
                cell.setCellStyle(stylecellcolumn);
                cell.setCellValue(rp.getCategory() == null ? "------" : rp.getCategory().getTitle());
                cell.setCellType(CellType.STRING);

                cell = row.createCell(2);
                cell.setCellStyle(stylecellcolumn);
                cell.setCellValue(rp.getIncidence() == null ? "------" : rp.getIncidence().getShortTitle());
                cell.setCellType(CellType.STRING);

                cell = row.createCell(3);
                cell.setCellStyle(stylecellcolumn);
                cell.setCellValue(rp.getSector() == null ? "------" : rp.getSector().getName());
                cell.setCellType(CellType.STRING);

                cell = row.createCell(4);
                cell.setCellStyle(stylecellcolumn);
                cell.setCellValue(rp.getAddress() == null ? "------" : rp.getAddress());
                cell.setCellType(CellType.STRING);

                cell = row.createCell(5);
                cell.setCellStyle(stylecellcolumn);
                cell.setCellValue(rp.getLatitude() == null ? "------" : rp.getLatitude());
                cell.setCellType(CellType.STRING);

                cell = row.createCell(6);
                cell.setCellStyle(stylecellcolumn);
                cell.setCellValue(rp.getLongitude() == null ? "------" : rp.getLongitude());
                cell.setCellType(CellType.STRING);

                cell = row.createCell(7);
                cell.setCellStyle(stylecellcolumn);
                cell.setCellValue(rp.getAccount().getFirstName() + " " + rp.getAccount().getLastName());
                cell.setCellType(CellType.STRING);

                cell = row.createCell(8);
                cell.setCellStyle(stylecellcolumndate);
                cell.setCellValue(rp.getCreatedAt() == null ? "--:--:--" : ft.format(rp.getCreatedAt()));

                cell = row.createCell(9);
                cell.setCellStyle(rp.getIncidence() == null ? stylecellcolumn : rp.getIncidence().getSeverity() == 1 ? stylecellcolumnyellow : rp.getIncidence().getSeverity() == 2 ? stylecellcolumnorange : rp.getIncidence().getSeverity() == 3 ? stylecellcolumnred : stylecellcolumn);
                cell.setCellValue(rp.getIncidence() == null ? "------" : rp.getIncidence().getSeverity() == 1 ? "BAJA" : rp.getIncidence().getSeverity() == 2 ? "LEVE" : rp.getIncidence().getSeverity() == 3 ? "ALTA" : "------");
                cell.setCellType(CellType.STRING);

                cell = row.createCell(10);
                cell.setCellStyle(rp.getStatus().toString() == "PENDING" ? stylecellcolumngray : rp.getStatus().toString() == "DISCARDED" ? stylecellcolumn : rp.getStatus().toString() == "ATTENDED" ? stylecellcolumngreen : stylecellcolumn);
                cell.setCellValue(rp.getStatus().toString() == null ? "------" : rp.getStatus().toString() == "PENDING" ? "PENDIENTE" : rp.getStatus().toString() == "DISCARDED" ? "DESCARTADA" : rp.getStatus().toString() == "ATTENDED" ? "ATENDIDA" : rp.getStatus().toString());
                cell.setCellType(CellType.STRING);

                cell = row.createCell(11);
                cell.setCellStyle(stylecellcolumn);
                cell.setCellValue(rp.getComment() == null ? "------" : rp.getComment() == "" ? "------" : rp.getComment().isEmpty() ? "------" : rp.getComment());
                cell.setCellType(CellType.STRING);

                for (int a = 0; a < row.getPhysicalNumberOfCells(); a++) {
                    sheet.autoSizeColumn(a);
                }
            }
        }

        return workbook;
    }
}
