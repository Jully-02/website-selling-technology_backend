package vn.jully.website_selling_technology_backend.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import vn.jully.website_selling_technology_backend.components.LocalizationUtils;
import vn.jully.website_selling_technology_backend.dtos.CartItemDTO;
import vn.jully.website_selling_technology_backend.entities.CartItem;
import vn.jully.website_selling_technology_backend.responses.CartItemResponse;
import vn.jully.website_selling_technology_backend.services.ICartItemService;
import vn.jully.website_selling_technology_backend.utils.MessageKey;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/cart-items")
@RequiredArgsConstructor
public class CartItemController {
    private final ICartItemService cartItemService;
    private final LocalizationUtils localizationUtils;

    @PostMapping("")
    public ResponseEntity<CartItemResponse> insertCartItem (
            @Valid @RequestBody CartItemDTO cartItemDTO,
            BindingResult result
    ) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(
                        CartItemResponse.
                                builder()
                                .message(localizationUtils.getLocalizedMessage(MessageKey.INVALID_ERROR, errorMessages.toString()))
                                .build()
                );
            }
            CartItem cartItem = cartItemService.insertCartItem(cartItemDTO);
            return ResponseEntity.ok(
                    CartItemResponse
                            .builder()
                            .message(localizationUtils.getLocalizedMessage(MessageKey.INSERT_SUCCESSFULLY))
                            .cartItem(cartItem)
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    CartItemResponse.
                            builder()
                            .message(localizationUtils.getLocalizedMessage(MessageKey.INVALID_ERROR, e.getMessage()))
                            .build()
            );
        }
    }

    @GetMapping("")
    public ResponseEntity<?> getAllCartItems () {
        return ResponseEntity.ok(cartItemService.getAllCartItems());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartItem> getCartItemById (@PathVariable("id") Long id) throws Exception {
        return ResponseEntity.ok(cartItemService.getCartItemById(id));
    }

    @GetMapping("/users/{user-id}")
    public ResponseEntity<?> getCartItemByUserId (@PathVariable("user-id") Long userId) throws Exception {
        return ResponseEntity.ok(cartItemService.findByUserId(userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCartItem (
            @PathVariable("id") Long id,
            @Valid @RequestBody CartItemDTO cartItemDTO,
            BindingResult result
    )
    {
        try {
            if (result.hasErrors()) {
                List<String> errorMessage = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(
                        CartItemResponse
                                .builder()
                                .message(localizationUtils.getLocalizedMessage(MessageKey.INVALID_ERROR, errorMessage.toString()))
                                .build()
                );
            }
            CartItem cartItem = cartItemService.updateCartItem(id, cartItemDTO);
            return ResponseEntity.ok(
                    CartItemResponse
                            .builder()
                            .message(localizationUtils.getLocalizedMessage(MessageKey.UPDATE_SUCCESSFULLY))
                            .cartItem(cartItem)
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    CartItemResponse
                            .builder()
                            .message(localizationUtils.getLocalizedMessage(MessageKey.UPDATE_FAILED, e.getMessage()))
                            .build()
            );
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCartItem (@PathVariable("id") Long id) {
        return ResponseEntity.ok(
                CartItemResponse
                        .builder()
                        .message(localizationUtils.getLocalizedMessage(MessageKey.DELETE_SUCCESSFULLY))
                        .build()
        );
    }
}
