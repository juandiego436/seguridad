package pe.gob.muniveintiseisdeoctubre.servicios.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.gob.muniveintiseisdeoctubre.servicios.dto.IncidenceDTO;
import pe.gob.muniveintiseisdeoctubre.servicios.dto.ResponseDTO;
import pe.gob.muniveintiseisdeoctubre.servicios.entity.Category;
import pe.gob.muniveintiseisdeoctubre.servicios.entity.Incidence;
import pe.gob.muniveintiseisdeoctubre.servicios.service.impl.IncidenceService;
import pe.gob.muniveintiseisdeoctubre.servicios.util.Constants;

@RestController
@RequestMapping("web/incidences")
public class IncidenceWebController {

    @Autowired
    IncidenceService incidenceService;

    @Autowired
    private Constants constants;

    @GetMapping
    public ResponseEntity<ResponseDTO> findAll() {
        var result = incidenceService.findAllFull();
        return ResponseEntity.ok(new ResponseDTO<>(result, "OK", constants.buildVersion));
    }

    @PutMapping("/{incidenceId}")
    public ResponseEntity<ResponseDTO> update(@PathVariable Integer incidenceId, @RequestBody IncidenceDTO incidenceDTO) {
        final var incidence = new Incidence();
        incidence.setId(incidenceId);
        incidence.setTitle(incidenceDTO.getTitle());
        incidence.setShortTitle(incidenceDTO.getShortTitle());
        incidence.setSeverity(incidenceDTO.getSeverity());
        incidence.setCategory(new Category(incidenceDTO.getCategoryId()));
        incidence.setDisabledAt(incidenceDTO.getDisabledAt());
        var result = incidenceService.createOrUpdate(incidence);
        return ResponseEntity.ok(new ResponseDTO<>(result, "OK", constants.buildVersion));
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> create(@RequestBody IncidenceDTO incidenceDTO) {
        final var incidence = new Incidence();
        incidence.setTitle(incidenceDTO.getTitle());
        incidence.setShortTitle(incidenceDTO.getShortTitle());
        incidence.setSeverity(incidenceDTO.getSeverity());
        incidence.setCategory(new Category(incidenceDTO.getCategoryId()));
        incidence.setDisabledAt(incidenceDTO.getDisabledAt());
        var result = incidenceService.createOrUpdate(incidence);
        return ResponseEntity.ok(new ResponseDTO<>(result, "OK", constants.buildVersion));
    }

}
