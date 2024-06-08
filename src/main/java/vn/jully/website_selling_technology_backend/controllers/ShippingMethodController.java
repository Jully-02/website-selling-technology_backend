package vn.jully.website_selling_technology_backend.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import vn.jully.website_selling_technology_backend.dtos.ShippingMethodDTO;
import vn.jully.website_selling_technology_backend.entities.ShippingMethod;
import vn.jully.website_selling_technology_backend.exceptions.DataNotFoundException;
import vn.jully.website_selling_technology_backend.services.IShippingMethodService;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/shipping-methods")
@RequiredArgsConstructor
public class ShippingMethodController {
    private final IShippingMethodService shippingMethodService;
    @PostMapping("")
    public ResponseEntity<?> insertShippingMethod (
            @Valid @RequestBody ShippingMethodDTO shippingMethodDTO,
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
            return ResponseEntity.ok(shippingMethodService.insertShippingMethod(shippingMethodDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getShippingMethod (@PathVariable("id") Long id) throws DataNotFoundException {
        return ResponseEntity.ok(shippingMethodService.getShippingMethod(id));
    }

    @GetMapping("")
    public ResponseEntity<List<ShippingMethod>> getAllShippingMethod () {
        return ResponseEntity.ok(shippingMethodService.getAllShippingMethod());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateShippingMethod (
            @PathVariable("id") Long id,
            @Valid @RequestBody ShippingMethodDTO shippingMethodDTO,
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
            return ResponseEntity.ok(shippingMethodService.updateShippingMethod(id, shippingMethodDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteShippingMethod (@PathVariable("id") Long id) {
        shippingMethodService.deleteShippingMethod(id);
        return ResponseEntity.ok("Deleted Shipping method successfully");
    }
}
