package vn.jully.website_selling_technology_backend.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import vn.jully.website_selling_technology_backend.dtos.SpecificationDTO;
import vn.jully.website_selling_technology_backend.entities.Specification;
import vn.jully.website_selling_technology_backend.exceptions.DataNotFoundException;
import vn.jully.website_selling_technology_backend.services.ISpecificationService;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/specifications")
@RequiredArgsConstructor
public class SpecificationController {
    private final ISpecificationService specificationService;
    @PostMapping("")
    public ResponseEntity<?> insertSpecification (
            @Valid @RequestBody SpecificationDTO specificationDTO,
            BindingResult result
    ) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessage = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessage);
            }
            return ResponseEntity.ok(specificationService.insertSpecification(specificationDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Specification> getSpecification (
            @PathVariable("id") Long id
    ) throws DataNotFoundException {
        Specification specification = specificationService.getSpecification(id);
        return ResponseEntity.ok(specification);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSpecification (
            @PathVariable("id") Long id,
            @Valid @RequestBody SpecificationDTO specificationDTO,
            BindingResult result
    ) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            return ResponseEntity.ok(specificationService.updateSpecification(id, specificationDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSpecification (
            @PathVariable("id") Long id
    ) {
        specificationService.deleteSpecification(id);
        return ResponseEntity.ok("Specification deleted successfully");
    }
}
