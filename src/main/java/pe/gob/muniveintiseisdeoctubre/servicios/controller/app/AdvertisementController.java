package pe.gob.muniveintiseisdeoctubre.servicios.controller.app;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.gob.muniveintiseisdeoctubre.servicios.dto.ResponseDTO;
import pe.gob.muniveintiseisdeoctubre.servicios.service.impl.AdvertisementService;
import pe.gob.muniveintiseisdeoctubre.servicios.util.Constants;

@RestController
@RequestMapping("app/advertisements")
public class AdvertisementController {
    private static final Log LOG = LogFactory.getLog(AdvertisementController.class);

    @Autowired
    private Constants constants;

    @Autowired
    AdvertisementService advertisementService;

    @GetMapping(value = "latest")
    public ResponseEntity<ResponseDTO> latest() {
        var result = advertisementService.lastActive();
        return ResponseEntity.ok(new ResponseDTO<>(result, "OK", constants.buildVersion));
    }

    @GetMapping(value = "")
    public ResponseEntity<ResponseDTO> list() {
        var result = advertisementService.findActives();
        return ResponseEntity.ok(new ResponseDTO<>(result, "OK", constants.buildVersion));
    }
}
