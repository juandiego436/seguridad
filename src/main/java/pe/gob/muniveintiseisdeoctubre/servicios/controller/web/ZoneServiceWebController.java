package pe.gob.muniveintiseisdeoctubre.servicios.controller.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.gob.muniveintiseisdeoctubre.servicios.dto.ResponseDTO;
import pe.gob.muniveintiseisdeoctubre.servicios.service.impl.ServiceZoneService;
import pe.gob.muniveintiseisdeoctubre.servicios.util.Constants;

@RestController
@RequestMapping("web/zone-service")
public class ZoneServiceWebController {

    private static final Log LOG = LogFactory.getLog(ZoneServiceWebController.class);

    @Autowired
    ServiceZoneService serviceZoneService;

    @Autowired
    private Constants constants;

    @GetMapping
    public ResponseEntity<ResponseDTO> list() {
        var result = serviceZoneService.getFirst();
        return ResponseEntity.ok(new ResponseDTO<>(result, "OK", constants.buildVersion));
    }
}
