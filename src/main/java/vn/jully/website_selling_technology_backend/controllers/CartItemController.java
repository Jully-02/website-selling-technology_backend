package vn.jully.website_selling_technology_backend.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import vn.jully.website_selling_technology_backend.components.LocalizationUtils;
import vn.jully.website_selling_technology_backend.dtos.CartItemDTO;
import vn.jully.website_selling_technology_backend.entities.CartItem;
import vn.jully.website_selling_technology_backend.exceptions.DataNotFoundException;
import vn.jully.website_selling_technology_backend.responses.cart_item.CartItemResponse;
import vn.jully.website_selling_technology_backend.responses.Response;
import vn.jully.website_selling_technology_backend.services.cart_item.ICartItemService;
import vn.jully.website_selling_technology_backend.utils.MessageKey;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/cart-items")
@RequiredArgsConstructor
public class CartItemController {
    private final ICartItemService cartItemService;
    private final LocalizationUtils localizationUtils;

    @PostMapping("")
    public ResponseEntity<Response> insertCartItem(
            @Valid @RequestBody CartItemDTO cartItemDTO,
            BindingResult result
    ) throws DataNotFoundException {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(
                    Response
                            .builder()
                            .status(HttpStatus.BAD_REQUEST)
                            .message(localizationUtils.getLocalizedMessage(MessageKey.INVALID_ERROR, errorMessages.toString()))
                            .build()
            );
        }
        CartItem cartItem = cartItemService.insertCartItem(cartItemDTO);
        return ResponseEntity.ok(
                Response
                        .builder()
                        .status(HttpStatus.CREATED)
                        .message(localizationUtils.getLocalizedMessage(MessageKey.INSERT_SUCCESSFULLY))
                        .data(CartItemResponse
                                .builder()
                                .productId(cartItem.getProduct().getId())
                                .quantity(cartItem.getQuantity())
                                .build())
                        .build()
        );
    }

    @GetMapping("")
    public ResponseEntity<Response> getAllCartItems() {
        List<CartItem> cartItems = cartItemService.getAllCartItems();
        return ResponseEntity.ok(
                Response
                        .builder()
                        .status(HttpStatus.OK)
                        .message("Get all cart item successfully")
                        .data(cartItems)
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getCartItemById(@PathVariable("id") Long id) throws Exception {
        CartItem cartItem = cartItemService.getCartItemById(id);
        return ResponseEntity.ok(
                Response.builder()
                        .status(HttpStatus.OK)
                        .data(cartItem)
                        .message("Get cart item successfully")
                        .build()
        );
    }

    @GetMapping("/users/{user-id}")
    public ResponseEntity<Response> getCartItemByUserId(@PathVariable("user-id") Long userId) throws Exception {
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
                Response
                        .builder()
                        .message("Get cart item successfully")
                        .status(HttpStatus.OK)
                        .data(cartItemResponseList)
                        .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> updateCartItem(
            @PathVariable("id") Long id,
            @Valid @RequestBody CartItemDTO cartItemDTO,
            BindingResult result
    ) throws Exception {
        if (result.hasErrors()) {
            List<String> errorMessage = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(
                    Response
                            .builder()
                            .status(HttpStatus.BAD_REQUEST)
                            .message(localizationUtils.getLocalizedMessage(MessageKey.INVALID_ERROR, errorMessage.toString()))
                            .build()
            );
        }
        CartItem cartItem = cartItemService.updateCartItem(id, cartItemDTO);
        return ResponseEntity.ok(
                Response
                        .builder()
                        .message(localizationUtils.getLocalizedMessage(MessageKey.UPDATE_SUCCESSFULLY))
                        .data(CartItemResponse
                                .builder()
                                .productId(cartItem.getProduct().getId())
                                .quantity(cartItem.getQuantity())
                                .build()
                        )
                        .status(HttpStatus.OK)
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteCartItem(@PathVariable("id") Long id) {
        return ResponseEntity.ok(
                Response
                        .builder()
                        .status(HttpStatus.OK)
                        .message(localizationUtils.getLocalizedMessage(MessageKey.DELETE_SUCCESSFULLY))
                        .build()
        );
    }

    @DeleteMapping("/user-product")
    public ResponseEntity<Response> deleteCartItemByUserIdAndProductId(
            @RequestParam("user-id") Long userId,
            @RequestParam("product-id") Long productId
    ) {
        cartItemService.deleteCartItemByUserIdAndProductId(userId, productId);
        return ResponseEntity.ok(
                Response
                        .builder()
                        .status(HttpStatus.OK)
                        .message(localizationUtils.getLocalizedMessage(MessageKey.DELETE_SUCCESSFULLY))
                        .build()
        );
    }

    @DeleteMapping("/user/{user-id}")
    public ResponseEntity<Response> deleteCartItemByUserId(
            @PathVariable("user-id") Long userId
    ) {
        cartItemService.deleteCartItemByUserId(userId);
        return ResponseEntity.ok(
                Response
                        .builder()
                        .status(HttpStatus.OK)
                        .message(localizationUtils.getLocalizedMessage(MessageKey.DELETE_SUCCESSFULLY))
                        .build()
        );
    }
}
