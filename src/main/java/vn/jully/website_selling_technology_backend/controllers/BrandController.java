package vn.jully.website_selling_technology_backend.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import vn.jully.website_selling_technology_backend.dtos.BrandDTO;
import vn.jully.website_selling_technology_backend.entities.Brand;
import vn.jully.website_selling_technology_backend.exceptions.DataNotFoundException;
import vn.jully.website_selling_technology_backend.responses.BrandListResponse;
import vn.jully.website_selling_technology_backend.responses.BrandResponse;
import vn.jully.website_selling_technology_backend.services.IBrandService;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/brands")
@RequiredArgsConstructor
public class BrandController {
    private final IBrandService brandService;

    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN')")
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
    public ResponseEntity<BrandListResponse> getAllBrands (
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        PageRequest pageRequest = PageRequest.of(
                page, limit,
//                Sort.by("createdAt").descending());
                Sort.by("id").ascending());
        Page<BrandResponse> brandPages = brandService.getAllBrands(pageRequest);

        int totalPages = brandPages.getTotalPages();
        List<BrandResponse> brandResponses = brandPages.getContent();
        return ResponseEntity.ok(BrandListResponse
                .builder()
                .brandResponses(brandResponses)
                .totalPages(totalPages)
                .build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateBrand (
            @PathVariable("id") Long id,
            @RequestBody BrandDTO brandDTO
    ) throws DataNotFoundException {
        brandService.updateBrand(id, brandDTO);
        return ResponseEntity.ok("Update brand successfully");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteBrand (@PathVariable("id") Long id) {
        brandService.deleteBrand(id);
        return ResponseEntity.ok("Delete brand with ID = " + id);
    }
}
