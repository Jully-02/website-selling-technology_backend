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
import vn.jully.website_selling_technology_backend.dtos.PaymentMethodDTO;
import vn.jully.website_selling_technology_backend.entities.PaymentMethod;
import vn.jully.website_selling_technology_backend.exceptions.DataNotFoundException;
import vn.jully.website_selling_technology_backend.responses.Response;
import vn.jully.website_selling_technology_backend.services.payment.IPaymentMethodService;
import vn.jully.website_selling_technology_backend.utils.MessageKey;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/payment-methods")
@RequiredArgsConstructor
public class PaymentMethodController {
    private final IPaymentMethodService paymentMethodService;
    private final LocalizationUtils localizationUtils;
    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> insertPaymentMethod (
            @Valid @RequestBody PaymentMethodDTO paymentMethodDTO,
            BindingResult result
    ) {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(
                        Response.builder()
                                .message(localizationUtils.getLocalizedMessage(MessageKey.INVALID_ERROR, errorMessages.toString()))
                                .status(HttpStatus.BAD_REQUEST)
                                .build()
                );
            }
            PaymentMethod paymentMethod = paymentMethodService.insertPaymentMethod(paymentMethodDTO);
            return ResponseEntity.ok(
                    Response.builder()
                            .data(paymentMethod)
                            .status(HttpStatus.CREATED)
                            .message(localizationUtils.getLocalizedMessage(MessageKey.INSERT_SUCCESSFULLY))
                            .build()
            );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getPaymentMethod (
            @PathVariable("id") Long id
    ) throws DataNotFoundException {
        PaymentMethod paymentMethod = paymentMethodService.getPaymentMethod(id);
        return ResponseEntity.ok(
                Response.builder()
                        .message("Get payment method successfully")
                        .data(paymentMethod)
                        .status(HttpStatus.OK)
                        .build()
        );
    }

    @GetMapping("")
    public ResponseEntity<Response> getAllPaymentMethod () {
        List<PaymentMethod> response = paymentMethodService.getAllPaymentMethod();
        return ResponseEntity.ok(
                Response.builder()
                        .status(HttpStatus.OK)
                        .data(response)
                        .message("Get all payment methods successfully")
                        .build()
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> updatePaymentMethod (
            @PathVariable("id") Long id,
            @Valid @RequestBody PaymentMethodDTO paymentMethodDTO,
            BindingResult result
    ) throws DataNotFoundException {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(
                        Response.builder()
                                .message(localizationUtils.getLocalizedMessage(MessageKey.INVALID_ERROR, errorMessages.toString()))
                                .status(HttpStatus.BAD_REQUEST)
                                .build()
                );
            }
            PaymentMethod paymentMethod = paymentMethodService.updatePaymentMethod(id, paymentMethodDTO);
            return ResponseEntity.ok(
                    Response.builder()
                            .status(HttpStatus.OK)
                            .data(paymentMethod)
                            .message(localizationUtils.getLocalizedMessage(MessageKey.UPDATE_SUCCESSFULLY))
                            .build()
            );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> deletePaymentMethod (@PathVariable("id") Long id) {
        paymentMethodService.deletePaymentMethod(id);
        return ResponseEntity.ok(
                Response.builder()
                        .status(HttpStatus.OK)
                        .message(localizationUtils.getLocalizedMessage(MessageKey.DELETE_SUCCESSFULLY))
                        .build()
        );
    }
}