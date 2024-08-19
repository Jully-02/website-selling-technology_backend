package vn.jully.website_selling_technology_backend.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import vn.jully.website_selling_technology_backend.components.LocalizationUtils;
import vn.jully.website_selling_technology_backend.dtos.SpecificationDTO;
import vn.jully.website_selling_technology_backend.entities.Specification;
import vn.jully.website_selling_technology_backend.exceptions.DataNotFoundException;
import vn.jully.website_selling_technology_backend.responses.Response;
import vn.jully.website_selling_technology_backend.services.specification.ISpecificationService;
import vn.jully.website_selling_technology_backend.utils.MessageKey;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/specifications")
@RequiredArgsConstructor
public class SpecificationController {
    private final ISpecificationService specificationService;
    private  final LocalizationUtils localizationUtils;
    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> insertSpecification (
            @Valid @RequestBody SpecificationDTO specificationDTO,
            BindingResult result
    ) throws DataNotFoundException {
            if (result.hasErrors()) {
                List<String> errorMessage = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(
                        Response.builder()
                                .status(HttpStatus.BAD_REQUEST)
                                .message(localizationUtils.getLocalizedMessage(MessageKey.INVALID_ERROR, errorMessage.toString()))
                                .build()
                );
            }
            Specification specification = specificationService.insertSpecification(specificationDTO);
            return ResponseEntity.ok(
                    Response.builder()
                            .data(specification)
                            .message(localizationUtils.getLocalizedMessage(MessageKey.INSERT_SUCCESSFULLY))
                            .status(HttpStatus.CREATED)
                            .build()
            );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getSpecification (
            @PathVariable("id") Long id
    ) throws Exception {
        Specification specification = specificationService.getSpecification(id);
        return ResponseEntity.ok(
                Response.builder()
                        .data(specification)
                        .status(HttpStatus.OK)
                        .message("Get specification successfully")
                        .build()
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> updateSpecification (
            @PathVariable("id") Long id,
            @Valid @RequestBody SpecificationDTO specificationDTO,
            BindingResult result
    ) throws Exception {
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
            Specification specification = specificationService.updateSpecification(id, specificationDTO);
            return ResponseEntity.ok(
                    Response.builder()
                            .message(localizationUtils.getLocalizedMessage(MessageKey.UPDATE_SUCCESSFULLY))
                            .status(HttpStatus.OK)
                            .data(specification)
                            .build()
            );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> deleteSpecification (
            @PathVariable("id") Long id
    ) {
        specificationService.deleteSpecification(id);
        return ResponseEntity.ok(
                Response.builder()
                        .status(HttpStatus.OK)
                        .message(localizationUtils.getLocalizedMessage(MessageKey.DELETE_SUCCESSFULLY))
                        .build()
        );
    }
}
