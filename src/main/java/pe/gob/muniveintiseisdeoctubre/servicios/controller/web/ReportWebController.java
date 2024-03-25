package pe.gob.muniveintiseisdeoctubre.servicios.controller.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.gob.muniveintiseisdeoctubre.servicios.dto.ResponseDTO;
import pe.gob.muniveintiseisdeoctubre.servicios.service.impl.ReportService;
import pe.gob.muniveintiseisdeoctubre.servicios.util.Constants;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("web/reports")
public class ReportWebController {

    private static final Log LOG = LogFactory.getLog(ReportWebController.class);

    @Autowired
    ReportService reportService;

    @Autowired
    private Constants constants;

    @GetMapping
    public ResponseEntity<ResponseDTO> list() {
        final var result = reportService.findAllPrimary();
        return ResponseEntity.ok(new ResponseDTO<>(result, "OK", constants.buildVersion));
    }

    @PostMapping(value = "/consultar")
    public ResponseEntity<ResponseDTO> consult(@RequestBody(required = false) HashMap<String, Object> filters) {
        final var result = reportService.consultar(filters);
        LOG.debug(filters);
        return ResponseEntity.ok(new ResponseDTO<>(result, "OK", constants.buildVersion));
    }

    @GetMapping(value = "/{reportId}")
    public ResponseEntity<ResponseDTO> get(@PathVariable Integer reportId) {
        final var report = reportService.find(reportId);
        if (report.isPresent()) {
            return ResponseEntity.ok(new ResponseDTO<>(report, "OK", constants.buildVersion));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDTO<>(report, "OK", constants.buildVersion));
    }

    @PostMapping("/heat")
    public ResponseEntity<ResponseDTO> getHeatMapData(@RequestBody(required = false) Map<String, Object> filters) {
        final var result = reportService.getHeatMapData(filters);
        return ResponseEntity.ok(new ResponseDTO<>(result, "OK", constants.buildVersion));
    }

    @PutMapping(value = "/{reportId}/status")
    public ResponseEntity<ResponseDTO> changeStatus(@PathVariable Integer reportId, @RequestBody Map<String, String> body) {
        final var status = reportService.updateStatus(reportId, Constants.ReportStatus.valueOf(body.get("status")));
        if (status) {
            return ResponseEntity.ok(new ResponseDTO<>(null, "OK", constants.buildVersion));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDTO<>(null, "OK", constants.buildVersion));
    }

    @PostMapping("/exports-excel")
    public void exportsExcel(HttpServletResponse response, @RequestBody(required = false) HashMap<String, Object> filters) throws IOException {
        try {
            String[] campos =
                    {"Código",
                            "Categoría",
                            "Incidencia",
                            "Sector",
                            "Dirección",
                            "Latitud",
                            "Longitud",
                            "Usuario",
                            "Fecha de reporte",
                            "Severidad",
                            "Estado",
                            "Comentario"};
            var report = reportService.consultar(filters);
            XSSFWorkbook workbook = reportService.generateExcel(report, campos, filters);
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + "Reporte.xlsx" + "\"");
            OutputStream out = response.getOutputStream();
            workbook.write(out);
            workbook.close();
            response.getOutputStream().flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
