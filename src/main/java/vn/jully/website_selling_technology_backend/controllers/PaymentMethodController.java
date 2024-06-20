package vn.jully.website_selling_technology_backend.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import vn.jully.website_selling_technology_backend.dtos.PaymentMethodDTO;
import vn.jully.website_selling_technology_backend.entities.PaymentMethod;
import vn.jully.website_selling_technology_backend.exceptions.DataNotFoundException;
import vn.jully.website_selling_technology_backend.services.IPaymentMethodService;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/payment-methods")
@RequiredArgsConstructor
public class PaymentMethodController {
    private final IPaymentMethodService paymentMethodService;
    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> insertPaymentMethod (
            @Valid @RequestBody PaymentMethodDTO paymentMethodDTO,
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
            PaymentMethod paymentMethod = paymentMethodService.insertPaymentMethod(paymentMethodDTO);
            return ResponseEntity.ok(paymentMethod);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPaymentMethod (
            @PathVariable("id") Long id
    ) throws DataNotFoundException {
        PaymentMethod paymentMethod = paymentMethodService.getPaymentMethod(id);
        return ResponseEntity.ok(paymentMethod);
    }

    @GetMapping("")
    public ResponseEntity<List<PaymentMethod>> getAllPaymentMethod () {
        return ResponseEntity.ok(paymentMethodService.getAllPaymentMethod());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updatePaymentMethod (
            @PathVariable("id") Long id,
            @Valid @RequestBody PaymentMethodDTO paymentMethodDTO,
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
            return ResponseEntity.ok(paymentMethodService.updatePaymentMethod(id, paymentMethodDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deletePaymentMethod (@PathVariable("id") Long id) {
        paymentMethodService.deletePaymentMethod(id);
        return ResponseEntity.ok("Deleted Payment method successfully");
    }
}
