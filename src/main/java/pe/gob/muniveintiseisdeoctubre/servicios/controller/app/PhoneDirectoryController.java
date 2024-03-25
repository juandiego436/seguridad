package pe.gob.muniveintiseisdeoctubre.servicios.controller.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.gob.muniveintiseisdeoctubre.servicios.dto.ResponseDTO;
import pe.gob.muniveintiseisdeoctubre.servicios.service.impl.PhoneDirectoryService;
import pe.gob.muniveintiseisdeoctubre.servicios.util.Constants;

@RestController
@RequestMapping("app/phone-directory")
public class PhoneDirectoryController {

    @Autowired
    PhoneDirectoryService phoneDirectoryService;

    @Autowired
    private Constants constants;

    @GetMapping
    public ResponseEntity<ResponseDTO> list() {
        var result = phoneDirectoryService.list();
        return ResponseEntity.ok(new ResponseDTO<>(result, "OK", constants.buildVersion));
    }

}
