package pe.gob.muniveintiseisdeoctubre.servicios.controller.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pe.gob.muniveintiseisdeoctubre.servicios.dto.ResponseDTO;
import pe.gob.muniveintiseisdeoctubre.servicios.entity.Advertisement;
import pe.gob.muniveintiseisdeoctubre.servicios.service.impl.AdvertisementService;
import pe.gob.muniveintiseisdeoctubre.servicios.util.Constants;

import java.io.IOException;
import java.text.ParseException;

@RestController
@RequestMapping("web/advertisements")
public class AdvertisementWebController {

    private static final Log LOG = LogFactory.getLog(AdvertisementWebController.class);

    @Autowired
    private Constants constants;

    @Autowired
    AdvertisementService advertisementService;

    @GetMapping
    public ResponseEntity<ResponseDTO> getAll() {
        var result = advertisementService.findAll();
        return ResponseEntity.ok(new ResponseDTO<>(result, "OK", constants.buildVersion));
    }

    @GetMapping(value = "last")
    public ResponseEntity<ResponseDTO> getLast() {
        var result = advertisementService.last();
        return ResponseEntity.ok(new ResponseDTO<>(result, "OK", constants.buildVersion));
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> create(@RequestParam("url") String url, @RequestParam("endDate") String endDate, @RequestParam(value = "file", required = false) MultipartFile file) throws IOException, ParseException {
        var adversitement = advertisementService.register(url, endDate, file);
        return ResponseEntity.ok(new ResponseDTO<>(adversitement, "OK", constants.buildVersion));
    }

    @PutMapping(value = "/{advertisementId}")
    public ResponseEntity<ResponseDTO> update(@PathVariable Integer advertisementId, @RequestPart("advertisement")
            Advertisement advertisement, @RequestPart(name = "file", required = false) MultipartFile file) throws IOException, ParseException {
        var adversitement = advertisementService.update(advertisementId, advertisement, file);
        return ResponseEntity.ok(new ResponseDTO<>(adversitement, "OK", constants.buildVersion));
    }

    @GetMapping(value = "/{advertisementId}")
    public ResponseEntity<ResponseDTO> findById(@PathVariable Integer advertisementId) {
        var advertisement = advertisementService.find(advertisementId).get();
        return ResponseEntity.ok(new ResponseDTO<>(advertisement, "OK", constants.buildVersion));
    }

    @DeleteMapping(value = "/{advertisementId}")
    public ResponseEntity<ResponseDTO> deleteById(@PathVariable Integer advertisementId) {
        advertisementService.delete(advertisementId);
        return ResponseEntity.ok(new ResponseDTO<>(null, "OK", constants.buildVersion));
    }
}
