package vn.jully.website_selling_technology_backend.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import vn.jully.website_selling_technology_backend.dtos.BrandDTO;
import vn.jully.website_selling_technology_backend.entities.Brand;
import vn.jully.website_selling_technology_backend.exceptions.DataNotFoundException;
import vn.jully.website_selling_technology_backend.services.IBrandService;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/brands")
@RequiredArgsConstructor
public class BrandController {
    private final IBrandService brandService;

    @PostMapping("")
    public ResponseEntity<?> insertBrand (
            @Valid @RequestBody BrandDTO brandDTO,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errorMessages);
        }
        brandService.insertBrand(brandDTO);
        return ResponseEntity.ok("Insert brand successfully");
    }

    @GetMapping("")
    public ResponseEntity<List<Brand>> getAllBrands (
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) {
        List<Brand> brands = brandService.getAllBrands();
        return ResponseEntity.ok(brands);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateBrand (
            @PathVariable("id") Long id,
            @RequestBody BrandDTO brandDTO
    ) throws DataNotFoundException {
        brandService.updateBrand(id, brandDTO);
        return ResponseEntity.ok("Update brand successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBrand (@PathVariable("id") Long id) {
        brandService.deleteBrand(id);
        return ResponseEntity.ok("Delete brand with ID = " + id);
    }
}
