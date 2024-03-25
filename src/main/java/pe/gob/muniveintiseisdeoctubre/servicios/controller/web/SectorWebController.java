package pe.gob.muniveintiseisdeoctubre.servicios.controller.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.gob.muniveintiseisdeoctubre.servicios.dto.ResponseDTO;
import pe.gob.muniveintiseisdeoctubre.servicios.dto.SectorDTO;
import pe.gob.muniveintiseisdeoctubre.servicios.service.impl.SectorService;
import pe.gob.muniveintiseisdeoctubre.servicios.util.Constants;

import java.util.Map;

@RestController
@RequestMapping("web/sectors")
public class SectorWebController {

    private static final Log LOG = LogFactory.getLog(SectorWebController.class);

    @Autowired
    private Constants constants;

    @Autowired
    SectorService sectorService;

    @GetMapping
    public ResponseEntity<ResponseDTO> getAll() {
        var result = sectorService.findAll();
        return ResponseEntity.ok(new ResponseDTO<>(result, "OK", constants.buildVersion));
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> create(@RequestBody SectorDTO sector) {
        sector.setId(null);
        var _sector = sectorService.createOrUpdate(sector);
        return ResponseEntity.ok(new ResponseDTO<>(sector, "OK", constants.buildVersion));
    }

    @PutMapping(value = "/{sectorId}")
    public ResponseEntity<ResponseDTO> update(@RequestBody SectorDTO sector) {
        var _sector = sectorService.createOrUpdate(sector);
        return ResponseEntity.ok(new ResponseDTO<>(_sector, "OK", constants.buildVersion));
    }

    @DeleteMapping(value = "/{sectorId}")
    public ResponseEntity<ResponseDTO> deleteById(@PathVariable Integer sectorId) {
        sectorService.delete(sectorId);
        return ResponseEntity.ok(new ResponseDTO<>(null, "OK", constants.buildVersion));
    }

    @PostMapping(value = "/sectorCategoryStats")
    public ResponseEntity<ResponseDTO> getSectorCategoryStats(@RequestBody(required = false) Map<String, Object> filters) {
        var lStatistic = sectorService.sectorCategoryStatistics(filters);
        return ResponseEntity.ok(new ResponseDTO<>(lStatistic, "OK", constants.buildVersion));
    }
}
