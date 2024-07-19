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
import vn.jully.website_selling_technology_backend.responses.Response;
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
    public ResponseEntity<Response<CartItemResponse>> insertCartItem (
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
                        Response.<CartItemResponse>
                                builder()
                                .message(localizationUtils.getLocalizedMessage(MessageKey.INVALID_ERROR, errorMessages.toString()))
                                .build()
                );
            }
            CartItem cartItem = cartItemService.insertCartItem(cartItemDTO);
            return ResponseEntity.ok(
                    Response.<CartItemResponse>
                            builder()
                            .message(localizationUtils.getLocalizedMessage(MessageKey.INSERT_SUCCESSFULLY))
                            .data(CartItemResponse
                                    .builder()
                                    .productId(cartItem.getProduct().getId())
                                    .quantity(cartItem.getQuantity())
                                    .build())
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Response.<CartItemResponse>
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
    public ResponseEntity<Response<CartItemResponse>> getCartItemByUserId (@PathVariable("user-id") Long userId) throws Exception {
        List<CartItem> cartItems = cartItemService.findByUserId(userId);
        List<CartItemResponse> cartItemResponseList = cartItems.stream()
                .map(cartItem -> CartItemResponse.builder()
                        .id(cartItem.getId())
                        .userId(cartItem.getUser().getId())
                        .productId(cartItem.getProduct().getId())
                        .quantity(cartItem.getQuantity())
                        .build())
                .toList();
        return ResponseEntity.ok(
                Response.<CartItemResponse>
                        builder()
                        .dataList(cartItemResponseList)
                        .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response<CartItemResponse>> updateCartItem (
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
                        Response.<CartItemResponse>
                                builder()
                                .message(localizationUtils.getLocalizedMessage(MessageKey.INVALID_ERROR, errorMessage.toString()))
                                .build()
                );
            }
            CartItem cartItem = cartItemService.updateCartItem(id, cartItemDTO);
            return ResponseEntity.ok(
                    Response.<CartItemResponse>
                            builder()
                            .message(localizationUtils.getLocalizedMessage(MessageKey.UPDATE_SUCCESSFULLY))
                            .data(CartItemResponse
                                    .builder()
                                    .productId(cartItem.getProduct().getId())
                                    .quantity(cartItem.getQuantity())
                                    .build()
                            )
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Response.<CartItemResponse>
                            builder()
                            .message(localizationUtils.getLocalizedMessage(MessageKey.UPDATE_FAILED, e.getMessage()))
                            .build()
            );
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCartItem (@PathVariable("id") Long id) {
        return ResponseEntity.ok(
                Response.<CartItemResponse>
                        builder()
                        .message(localizationUtils.getLocalizedMessage(MessageKey.DELETE_SUCCESSFULLY))
                        .build()
        );
    }

    @DeleteMapping("/user-product")
    public ResponseEntity<?> deleteCartItemByUserIdAndProductId (
            @RequestParam("user-id") Long userId,
            @RequestParam("product-id") Long productId
    ) {
        try {
            cartItemService.deleteCartItemByUserIdAndProductId(userId, productId);
            return ResponseEntity.ok(
                    Response.<CartItemResponse>
                                    builder()
                            .message(localizationUtils.getLocalizedMessage(MessageKey.DELETE_SUCCESSFULLY))
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Response.<CartItemResponse>
                            builder()
                            .message(localizationUtils.getLocalizedMessage(MessageKey.DELETE_FAILED, e.getMessage()))
                            .build()
            );
        }
    }

    @DeleteMapping("/user/{user-id}")
    public ResponseEntity<?> deleteCartItemByUserId (
            @PathVariable("user-id") Long userId
    ) {
        try {
            cartItemService.deleteCartItemByUserId(userId);
            return ResponseEntity.ok(
                    Response.<CartItemResponse>
                                    builder()
                            .message(localizationUtils.getLocalizedMessage(MessageKey.DELETE_SUCCESSFULLY))
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Response.<CartItemResponse>
                                    builder()
                            .message(localizationUtils.getLocalizedMessage(MessageKey.DELETE_FAILED, e.getMessage()))
                            .build()
            );
        }
    }
}
