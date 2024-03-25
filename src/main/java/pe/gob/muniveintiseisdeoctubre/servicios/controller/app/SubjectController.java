package pe.gob.muniveintiseisdeoctubre.servicios.controller.app;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.gob.muniveintiseisdeoctubre.servicios.dto.ResponseDTO;
import pe.gob.muniveintiseisdeoctubre.servicios.entity.Subject;
import pe.gob.muniveintiseisdeoctubre.servicios.service.impl.SubjectService;
import pe.gob.muniveintiseisdeoctubre.servicios.util.Constants;

@RestController
@RequestMapping("app/subjects")
public class SubjectController {

    private static final Log LOG = LogFactory.getLog(SubjectController.class);

    @Autowired
    private Constants constants;

    @Autowired
    SubjectService subjectService;

    @PostMapping
    public ResponseEntity<ResponseDTO> create(@RequestBody Subject subject) {
        subjectService.createOrUpdate(subject);
        return ResponseEntity.ok(new ResponseDTO<>(subject, "OK", constants.buildVersion));
    }

    @GetMapping
    public ResponseEntity<ResponseDTO> list() {
        var result = subjectService.findAll();
        return ResponseEntity.ok(new ResponseDTO<>(result, "OK", constants.buildVersion));
    }
}
