package vn.jully.website_selling_technology_backend.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import vn.jully.website_selling_technology_backend.components.LocalizationUtils;
import vn.jully.website_selling_technology_backend.dtos.FavoriteProductDTO;
import vn.jully.website_selling_technology_backend.entities.FavoriteProduct;
import vn.jully.website_selling_technology_backend.responses.favorite.FavoriteProductResponse;
import vn.jully.website_selling_technology_backend.responses.Response;
import vn.jully.website_selling_technology_backend.services.favorite.IFavoriteProductService;
import vn.jully.website_selling_technology_backend.utils.MessageKey;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/favorites")
@RequiredArgsConstructor
public class FavoriteProductController {
    private final IFavoriteProductService favoriteProductService;
    private final LocalizationUtils localizationUtils;

    @PostMapping("")
    public ResponseEntity<Response> insertFavoriteProduct(
            @Valid @RequestBody FavoriteProductDTO favoriteProductDTO,
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
        FavoriteProduct favoriteProduct = favoriteProductService.insertFavoriteProduct(favoriteProductDTO);
        return ResponseEntity.ok(
                Response.builder()
                        .data(FavoriteProductResponse
                                .builder()
                                .productId(favoriteProduct.getProduct().getId())
                                .userId(favoriteProduct.getUser().getId())
                                .build())
                        .message(localizationUtils.getLocalizedMessage(MessageKey.INSERT_SUCCESSFULLY))
                        .status(HttpStatus.CREATED)
                        .build()
        );

    }

    @GetMapping("")
    public ResponseEntity<Response> getAllFavoriteProducts() {

        List<FavoriteProduct> favoriteProducts = favoriteProductService.getAllFavoriteProducts();
        return ResponseEntity.ok(
                Response.builder()
                        .status(HttpStatus.OK)
                        .data(favoriteProducts)
                        .message("Get all favorite products successfully")
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getFavoriteProductById(@PathVariable("id") Long id) throws Exception {
        FavoriteProduct favoriteProduct = favoriteProductService.getFavoriteProductById(id);
        return ResponseEntity.ok(
                Response.builder()
                        .data(FavoriteProductResponse
                                .builder()
                                .userId(favoriteProduct.getUser().getId())
                                .productId(favoriteProduct.getProduct().getId())
                                .build())
                        .message("Get favorite product successfully")
                        .status(HttpStatus.OK)
                        .build()
        );

    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> updateFavoriteProduct(
            @PathVariable("id") Long id,
            @Valid @RequestBody FavoriteProductDTO favoriteProductDTO,
            BindingResult result
    ) throws Exception {
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
        FavoriteProduct favoriteProduct = favoriteProductService.updateFavoriteProduct(id, favoriteProductDTO);
        return ResponseEntity.ok(
                Response.builder()
                        .status(HttpStatus.OK)
                        .message(localizationUtils.getLocalizedMessage(MessageKey.UPDATE_SUCCESSFULLY))
                        .data(FavoriteProductResponse
                                .builder()
                                .productId(favoriteProduct.getProduct().getId())
                                .userId(favoriteProduct.getUser().getId())
                                .build())
                        .build()
        );
    }

    @GetMapping("/users/{user-id}")
    public ResponseEntity<Response> getFavoriteProductByUserId(@PathVariable("user-id") Long userId) throws Exception {
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
                Response.builder()
                        .data(favoriteProductResponseList)
                        .message("Get favorite product successfully")
                        .status(HttpStatus.OK)
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteFavoriteProduct(@PathVariable("id") Long id) {
        favoriteProductService.deleteFavoriteProduct(id);
        return ResponseEntity.ok(
                Response.builder()
                        .message(localizationUtils.getLocalizedMessage(MessageKey.DELETE_SUCCESSFULLY))
                        .status(HttpStatus.OK)
                        .build()
        );
    }

    @DeleteMapping("/user-product")
    public ResponseEntity<Response> deleteFavoriteByUserIdAndProductId(
            @RequestParam("user-id") Long userId,
            @RequestParam("product-id") Long productId
    ) {
        favoriteProductService.deleteByUserIdAndProductId(userId, productId);
        return ResponseEntity.ok(
                Response.builder()
                        .message(localizationUtils.getLocalizedMessage(MessageKey.DELETE_SUCCESSFULLY))
                        .status(HttpStatus.OK)
                        .build()
        );

    }
}
