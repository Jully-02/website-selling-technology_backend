package vn.jully.website_selling_technology_backend.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import vn.jully.website_selling_technology_backend.components.LocalizationUtils;
import vn.jully.website_selling_technology_backend.dtos.BrandDTO;
import vn.jully.website_selling_technology_backend.entities.Brand;
import vn.jully.website_selling_technology_backend.exceptions.DataNotFoundException;
import vn.jully.website_selling_technology_backend.responses.Response;
import vn.jully.website_selling_technology_backend.responses.brand.BrandListResponse;
import vn.jully.website_selling_technology_backend.responses.brand.BrandResponse;
import vn.jully.website_selling_technology_backend.services.brand.IBrandService;
import vn.jully.website_selling_technology_backend.utils.MessageKey;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/brands")
@RequiredArgsConstructor
public class BrandController {
    private final IBrandService brandService;
    private final LocalizationUtils localizationUtils;

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
            return ResponseEntity.badRequest().body(
                    Response.builder()
                            .status(HttpStatus.BAD_REQUEST)
                            .message(localizationUtils.getLocalizedMessage(MessageKey.INVALID_ERROR, errorMessages.toString()))
                            .build()
            );
        }
        Brand brand = brandService.insertBrand(brandDTO);
        return ResponseEntity.ok(
                Response.builder()
                        .data(brand)
                        .message(localizationUtils.getLocalizedMessage(MessageKey.INSERT_SUCCESSFULLY))
                        .status(HttpStatus.CREATED)
                        .build()
        );
    }

    @GetMapping("")
    public ResponseEntity<Response> getAllBrands (
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
        BrandListResponse response = BrandListResponse
                .builder()
                .brandResponses(brandResponses)
                .totalPages(totalPages)
                .build();
        return ResponseEntity.ok(
                Response.builder()
                        .data(response)
                        .message("Get all brands successfully")
                        .status(HttpStatus.OK)
                        .build()
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> updateBrand (
            @PathVariable("id") Long id,
            @RequestBody BrandDTO brandDTO
    ) throws DataNotFoundException {
        Brand brand = brandService.updateBrand(id, brandDTO);
        return ResponseEntity.ok(
                Response.builder()
                        .status(HttpStatus.OK)
                        .data(brand)
                        .message(localizationUtils.getLocalizedMessage(MessageKey.UPDATE_SUCCESSFULLY))
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> deleteBrand (@PathVariable("id") Long id) {
        brandService.deleteBrand(id);
        return ResponseEntity.ok(
                Response.builder()
                        .message(localizationUtils.getLocalizedMessage(MessageKey.DELETE_SUCCESSFULLY))
                        .status(HttpStatus.OK)
                        .build()
        );
    }
}
