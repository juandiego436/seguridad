package pe.gob.muniveintiseisdeoctubre.servicios.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.gob.muniveintiseisdeoctubre.servicios.dto.ResponseDTO;
import pe.gob.muniveintiseisdeoctubre.servicios.service.impl.RoleService;
import pe.gob.muniveintiseisdeoctubre.servicios.util.Constants;

@RestController
@RequestMapping("web/roles")
public class RoleWebController {

    @Autowired
    private Constants constants;

    @Autowired
    RoleService roleService;

    @GetMapping
    public ResponseEntity<ResponseDTO> list() {
        var result = roleService.findAll();
        return ResponseEntity.ok(new ResponseDTO<>(result, "OK", constants.buildVersion));
    }
}
