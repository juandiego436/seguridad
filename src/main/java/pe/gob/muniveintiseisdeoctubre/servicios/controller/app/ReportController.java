package pe.gob.muniveintiseisdeoctubre.servicios.controller.app;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pe.gob.muniveintiseisdeoctubre.servicios.dto.AuthDTO;
import pe.gob.muniveintiseisdeoctubre.servicios.dto.ResponseDTO;
import pe.gob.muniveintiseisdeoctubre.servicios.entity.Incidence;
import pe.gob.muniveintiseisdeoctubre.servicios.entity.Report;
import pe.gob.muniveintiseisdeoctubre.servicios.service.impl.IncidenceService;
import pe.gob.muniveintiseisdeoctubre.servicios.service.impl.ReportService;
import pe.gob.muniveintiseisdeoctubre.servicios.util.Constants;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("app/reports")
public class ReportController {

    private static final Log LOG = LogFactory.getLog(ReportController.class);

    @Autowired
    ReportService reportService;

    @Autowired
    IncidenceService incidenceService;

    @Autowired
    private Constants constants;

    @GetMapping
    public ResponseEntity<ResponseDTO> list() {
        var result = reportService.findAllPrimary();
        return ResponseEntity.ok(new ResponseDTO<>(result, "OK", constants.buildVersion));
    }

    @GetMapping(value = "/{reportId}")
    public ResponseEntity<ResponseDTO> get(@PathVariable Integer reportId) {
        var report = reportService.find(reportId);
        if (report.isPresent()) {
            return ResponseEntity.ok(new ResponseDTO<>(report, "OK", constants.buildVersion));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDTO<>(report, "OK", constants.buildVersion));
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> create(Authentication authentication, @RequestBody Report report) {
        report.setAccount(((AuthDTO) authentication.getPrincipal()).getAccount());
        report = reportService.createOrUpdate(report);
        return ResponseEntity.ok(new ResponseDTO<>(report, "OK", constants.buildVersion));
    }

    @PutMapping(value = "/{reportId}")
    public ResponseEntity<ResponseDTO> update(@PathVariable("reportId") Integer reportId, @RequestParam(value = "comment", required = false) String comment,
                                              @RequestParam(value = "incidenceId") Integer incidenceId,
                                              @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        Report report = reportService.find(reportId).get();
        String fileName;

        if (file != null) {
            fileName = UUID.randomUUID() + "." + file.getOriginalFilename().split("\\.")[1];
            final var bytes = file.getBytes();
            final var path = Paths.get(constants.uploadReportPath + fileName);
            Files.write(path, bytes);
            report.setPhotoUrl(constants.uploadReportUrl + fileName);
        }

        report.setIncidence(new Incidence(incidenceId));
        report.setComment(comment);
        reportService.createOrUpdate(report);
        return ResponseEntity.ok(new ResponseDTO<>(report, "OK", constants.buildVersion));
    }

}
