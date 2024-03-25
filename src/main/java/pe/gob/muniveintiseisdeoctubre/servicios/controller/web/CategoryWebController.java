package pe.gob.muniveintiseisdeoctubre.servicios.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.gob.muniveintiseisdeoctubre.servicios.dto.ResponseDTO;
import pe.gob.muniveintiseisdeoctubre.servicios.service.impl.CategoryService;
import pe.gob.muniveintiseisdeoctubre.servicios.util.Constants;

@RestController
@RequestMapping("web/categories")
public class CategoryWebController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private Constants constants;

    @GetMapping("/listForFilter")
    public ResponseEntity<ResponseDTO> listForFilter() {
        var result = categoryService.findAllForFilter();
        return ResponseEntity.ok(new ResponseDTO<>(result, "ok", constants.buildVersion));
    }

    @GetMapping
    public ResponseEntity<ResponseDTO> findAll() {
        var result = categoryService.findAll();
        return ResponseEntity.ok(new ResponseDTO<>(result, "ok", constants.buildVersion));
    }
}
