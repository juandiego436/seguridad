package pe.gob.muniveintiseisdeoctubre.servicios.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.gob.muniveintiseisdeoctubre.servicios.dto.ResponseDTO;
import pe.gob.muniveintiseisdeoctubre.servicios.service.impl.PositionService;
import pe.gob.muniveintiseisdeoctubre.servicios.util.Constants;

@RestController
@RequestMapping("web/positions")
public class PositionWebController {

    @Autowired
    private Constants constants;

    @Autowired
    private PositionService positionsService;

    @GetMapping("/agents")
    public ResponseEntity<ResponseDTO> getAllAgents(Authentication authentication) {
        var position = positionsService.getLastHour();
        return ResponseEntity.ok(new ResponseDTO<>(position, "OK", constants.buildVersion));
    }
}
