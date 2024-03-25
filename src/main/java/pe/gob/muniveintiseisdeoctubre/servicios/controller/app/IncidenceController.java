package pe.gob.muniveintiseisdeoctubre.servicios.controller.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.gob.muniveintiseisdeoctubre.servicios.dto.ResponseDTO;
import pe.gob.muniveintiseisdeoctubre.servicios.entity.Incidence;
import pe.gob.muniveintiseisdeoctubre.servicios.service.impl.IncidenceService;
import pe.gob.muniveintiseisdeoctubre.servicios.util.Constants;

import java.util.List;

@RestController
@RequestMapping("app")
public class IncidenceController {

    private static final Logger LOG = LoggerFactory.getLogger(IncidenceController.class);

    @Autowired
    IncidenceService incidenceService;

    @Autowired
    private Constants constants;

    @GetMapping(value = "/categories/{catId}/incidences")
    public ResponseEntity<ResponseDTO> findByCat(@PathVariable("catId") Integer categoryId) {
        List<Incidence> result = incidenceService.findByCategoryId(categoryId);
        LOG.debug(result.toString());
        return ResponseEntity.ok(new ResponseDTO<>(result, "OK", constants.buildVersion));
    }

}
