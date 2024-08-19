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
import vn.jully.website_selling_technology_backend.dtos.ShippingMethodDTO;
import vn.jully.website_selling_technology_backend.entities.ShippingMethod;
import vn.jully.website_selling_technology_backend.exceptions.DataNotFoundException;
import vn.jully.website_selling_technology_backend.responses.Response;
import vn.jully.website_selling_technology_backend.services.shipping.IShippingMethodService;
import vn.jully.website_selling_technology_backend.utils.MessageKey;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/shipping-methods")
@RequiredArgsConstructor
public class ShippingMethodController {
    private final IShippingMethodService shippingMethodService;
    private final LocalizationUtils localizationUtils;
    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> insertShippingMethod (
            @Valid @RequestBody ShippingMethodDTO shippingMethodDTO,
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
            ShippingMethod response = shippingMethodService.insertShippingMethod(shippingMethodDTO);
            return ResponseEntity.ok(
                    Response.builder()
                            .data(response)
                            .message(localizationUtils.getLocalizedMessage(MessageKey.INSERT_SUCCESSFULLY))
                            .status(HttpStatus.CREATED)
                            .build()
            );

    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getShippingMethod (@PathVariable("id") Long id) throws DataNotFoundException {
        ShippingMethod response = shippingMethodService.getShippingMethod(id);
        return ResponseEntity.ok(
                Response.builder()
                        .status(HttpStatus.OK)
                        .data(response)
                        .message("Get shipping method successfully")
                        .build()
        );
    }

    @GetMapping("")
    public ResponseEntity<Response> getAllShippingMethod () {
        List<ShippingMethod> response = shippingMethodService.getAllShippingMethod();
        return ResponseEntity.ok(
                Response.builder()
                        .data(response)
                        .message("Get all shipping methods successfully")
                        .status(HttpStatus.OK)
                        .build()
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> updateShippingMethod (
            @PathVariable("id") Long id,
            @Valid @RequestBody ShippingMethodDTO shippingMethodDTO,
            BindingResult result
    ) throws DataNotFoundException {

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
            ShippingMethod response = shippingMethodService.updateShippingMethod(id, shippingMethodDTO);
            return ResponseEntity.ok(
                    Response.builder()
                            .data(response)
                            .message(localizationUtils.getLocalizedMessage(MessageKey.UPDATE_SUCCESSFULLY))
                            .status(HttpStatus.OK)
                            .build()
            );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> deleteShippingMethod (@PathVariable("id") Long id) {
        shippingMethodService.deleteShippingMethod(id);
        return ResponseEntity.ok(
                Response.builder()
                        .status(HttpStatus.OK)
                        .message(localizationUtils.getLocalizedMessage(MessageKey.DELETE_SUCCESSFULLY))
                        .build()
        );
    }
}
