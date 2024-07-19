package vn.jully.website_selling_technology_backend.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import vn.jully.website_selling_technology_backend.components.LocalizationUtils;
import vn.jully.website_selling_technology_backend.dtos.FavoriteProductDTO;
import vn.jully.website_selling_technology_backend.entities.FavoriteProduct;
import vn.jully.website_selling_technology_backend.responses.CartItemResponse;
import vn.jully.website_selling_technology_backend.responses.FavoriteProductResponse;
import vn.jully.website_selling_technology_backend.responses.Response;
import vn.jully.website_selling_technology_backend.services.IFavoriteProductService;
import vn.jully.website_selling_technology_backend.utils.MessageKey;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/favorites")
@RequiredArgsConstructor
public class FavoriteProductController {
    private final IFavoriteProductService favoriteProductService;
    private final LocalizationUtils localizationUtils;

    @PostMapping("")
    public ResponseEntity<?> insertFavoriteProduct (
            @Valid @RequestBody FavoriteProductDTO favoriteProductDTO,
            BindingResult result
    ) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(
                        Response.<FavoriteProductResponse>
                                builder()
                                .message(localizationUtils.getLocalizedMessage(MessageKey.INVALID_ERROR, errorMessages.toString()))
                                .build()
                );
            }
            FavoriteProduct favoriteProduct = favoriteProductService.insertFavoriteProduct(favoriteProductDTO);
            return ResponseEntity.ok(
                    Response.<FavoriteProductResponse>
                            builder()
                            .message(localizationUtils.getLocalizedMessage(MessageKey.INSERT_SUCCESSFULLY))
                            .data(FavoriteProductResponse
                                    .builder()
                                    .productId(favoriteProduct.getProduct().getId())
                                    .userId(favoriteProduct.getUser().getId())
                                    .build())
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Response.<FavoriteProductResponse>
                            builder()
                            .message(localizationUtils.getLocalizedMessage(MessageKey.INSERT_FAILED, e.getMessage()))
                            .build()
            );
        }
    }

    @GetMapping("")
    public ResponseEntity<?> getAllFavoriteProducts () {
        try {
            return ResponseEntity.ok(favoriteProductService.getAllFavoriteProducts());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFavoriteProductById (@PathVariable("id") Long id) {
        try {
            FavoriteProduct favoriteProduct = favoriteProductService.getFavoriteProductById(id);
            return ResponseEntity.ok(
                    Response.<FavoriteProductResponse>
                            builder()
                            .data(FavoriteProductResponse
                                    .builder()
                                    .userId(favoriteProduct.getUser().getId())
                                    .productId(favoriteProduct.getProduct().getId())
                                    .build())
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateFavoriteProduct (
            @PathVariable("id") Long id,
            @Valid @RequestBody FavoriteProductDTO favoriteProductDTO,
            BindingResult result
    ) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(
                        Response.<FavoriteProductResponse>
                                builder()
                                .message(localizationUtils.getLocalizedMessage(MessageKey.INVALID_ERROR, errorMessages.toString()))
                                .build()
                );
            }
            FavoriteProduct favoriteProduct = favoriteProductService.updateFavoriteProduct(id, favoriteProductDTO);
            return ResponseEntity.ok(
                    Response.<FavoriteProductResponse>
                            builder()
                            .message(localizationUtils.getLocalizedMessage(MessageKey.UPDATE_SUCCESSFULLY))
                            .data(FavoriteProductResponse
                                    .builder()
                                    .productId(favoriteProduct.getProduct().getId())
                                    .userId(favoriteProduct.getUser().getId())
                                    .build())
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Response.<FavoriteProductResponse>
                            builder()
                            .message(localizationUtils.getLocalizedMessage(MessageKey.UPDATE_FAILED, e.getMessage()))
                            .build()
            );
        }
    }

    @GetMapping("/users/{user-id}")
    public ResponseEntity<?> getFavoriteProductByUserId (@PathVariable("user-id") Long userId) {
        try {
            List<FavoriteProduct> favoriteProducts = favoriteProductService.findByUserId(userId);
            List<FavoriteProductResponse> favoriteProductResponseList = favoriteProducts.stream()
                    .map(favoriteProduct -> FavoriteProductResponse
                            .builder()
                            .productId(favoriteProduct.getProduct().getId())
                            .userId(favoriteProduct.getUser().getId())
                            .build()
                    )
                    .toList();
            return ResponseEntity.ok(
                    Response.<FavoriteProductResponse>
                            builder()
                            .dataList(favoriteProductResponseList)
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFavoriteProduct (@PathVariable("id") Long id) {
        favoriteProductService.deleteFavoriteProduct(id);
        return ResponseEntity.ok(
                Response.<FavoriteProductResponse>
                        builder()
                        .message(localizationUtils.getLocalizedMessage(MessageKey.DELETE_SUCCESSFULLY))
                        .build()
        );
    }

    @DeleteMapping("/user-product")
    public ResponseEntity<?> deleteFavoriteByUserIdAndProductId (
            @RequestParam("user-id") Long userId,
            @RequestParam("product-id") Long productId
    ) {
        try {
            favoriteProductService.deleteByUserIdAndProductId(userId, productId);
            return ResponseEntity.ok (
                    Response.<FavoriteProductResponse>
                            builder()
                            .message(localizationUtils.getLocalizedMessage(MessageKey.DELETE_SUCCESSFULLY))
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Response.<FavoriteProductResponse>
                                    builder()
                            .message(localizationUtils.getLocalizedMessage(MessageKey.DELETE_FAILED, e.getMessage()))
                            .build()
            );
        }
    }
}
