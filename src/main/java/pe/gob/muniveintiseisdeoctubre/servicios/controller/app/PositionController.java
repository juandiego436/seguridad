package pe.gob.muniveintiseisdeoctubre.servicios.controller.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pe.gob.muniveintiseisdeoctubre.servicios.dto.AuthDTO;
import pe.gob.muniveintiseisdeoctubre.servicios.dto.ResponseDTO;
import pe.gob.muniveintiseisdeoctubre.servicios.entity.Position;
import pe.gob.muniveintiseisdeoctubre.servicios.service.impl.PositionService;
import pe.gob.muniveintiseisdeoctubre.servicios.util.Constants;

@RestController
@RequestMapping("app/positions")
public class PositionController {
    @Autowired
    private Constants constants;

    @Autowired
    private PositionService positionsService;

    @PostMapping
    public ResponseEntity<ResponseDTO> push(Authentication authentication, @RequestBody Position position) {
        if (position.getLatitude() == null || position.getLongitude() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDTO<>(null, "Se requiere latitud y longitud", constants.buildVersion));
        }
        var accountId = ((AuthDTO) authentication.getPrincipal()).getAccount().getId();
        position.setAccountId(accountId);
        position = positionsService.createOrUpdate(position);
        return ResponseEntity.ok(new ResponseDTO<>(position, "OK", constants.buildVersion));
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<ResponseDTO> get(Authentication authentication, @PathVariable Integer accountId) {
        var authAccountId = ((AuthDTO) authentication.getPrincipal()).getAccount().getId();
        if (accountId != authAccountId) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseDTO<>(null, "No tiene permisos para acceder", constants.buildVersion));
        }
        var position = positionsService.find(authAccountId);
        return ResponseEntity.ok(new ResponseDTO<>(position, "OK", constants.buildVersion));
    }
}
