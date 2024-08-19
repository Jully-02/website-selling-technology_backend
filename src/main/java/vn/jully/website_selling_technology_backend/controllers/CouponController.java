package vn.jully.website_selling_technology_backend.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import vn.jully.website_selling_technology_backend.components.LocalizationUtils;
import vn.jully.website_selling_technology_backend.dtos.CouponDTO;
import vn.jully.website_selling_technology_backend.entities.Coupon;
import vn.jully.website_selling_technology_backend.exceptions.DataNotFoundException;
import vn.jully.website_selling_technology_backend.responses.Response;
import vn.jully.website_selling_technology_backend.responses.coupon.CouponResponse;
import vn.jully.website_selling_technology_backend.services.coupon.ICouponService;
import vn.jully.website_selling_technology_backend.utils.MessageKey;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/coupons")
@RequiredArgsConstructor
public class CouponController {
    private final ICouponService couponService;
    private final LocalizationUtils localizationUtils;

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Response> insertCoupon(
            @Valid @RequestBody CouponDTO couponDTO,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(
                    Response.builder()
                            .status(HttpStatus.BAD_REQUEST)
                            .message(localizationUtils.getLocalizedMessage(MessageKey.INVALID_ERROR, errorMessages.toString()))
                            .build()
            );
        }
        Coupon coupon = couponService.insertCoupon(couponDTO);
        return ResponseEntity.ok(
                Response.builder()
                        .status(HttpStatus.CREATED)
                        .message(localizationUtils.getLocalizedMessage(MessageKey.INSERT_SUCCESSFULLY))
                        .data(coupon)
                        .build()
        );

    }

    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<Response> getAllCoupons(
            @RequestParam(value = "keyword", defaultValue = "") String keyword,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "limit", defaultValue = "10") int limit
    ) {
        PageRequest pageRequest = PageRequest.of(
                page, limit,
                Sort.by("id").ascending()
        );
        Page<Coupon> couponPage = couponService.getAllCoupons(keyword, pageRequest);
        return ResponseEntity.ok(
                Response.builder()
                        .data(CouponResponse.builder()
                                .coupons(couponPage.getContent())
                                .totalPages(couponPage.getTotalPages())
                                .build())
                        .message("Get all coupons successfully")
                        .status(HttpStatus.OK)
                        .build()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<Response> getCouponById(@PathVariable("id") Long couponId) throws DataNotFoundException {
        Coupon coupon = couponService.getCouponById(couponId);
        return ResponseEntity.ok(
                Response.builder()
                        .status(HttpStatus.OK)
                        .data(coupon)
                        .message("Get coupon successfully")
                        .build()
        );
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Response> updateCoupon(
            @PathVariable("id") Long couponId,
            @RequestBody CouponDTO couponDTO,
            BindingResult result
    ) throws DataNotFoundException {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(
                    Response.builder()
                            .status(HttpStatus.BAD_REQUEST)
                            .message(localizationUtils.getLocalizedMessage(MessageKey.INVALID_ERROR, errorMessages.toString()))
                            .build()
            );
        }
        Coupon coupon = couponService.updateCoupon(couponId, couponDTO);
        return ResponseEntity.ok(
                Response.builder()
                        .data(coupon)
                        .status(HttpStatus.OK)
                        .message(localizationUtils.getLocalizedMessage(MessageKey.UPDATE_SUCCESSFULLY))
                        .build()
        );

    }

    @GetMapping("/calculate")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<Response> calculateCouponValue(
            @RequestParam("coupon-code") String couponCode,
            @RequestParam("total-amount") double totalAmount
    ) throws DataNotFoundException {
        double finalAmount = couponService.calculateCouponValue(couponCode, totalAmount);
        return ResponseEntity.ok(
                Response.builder()
                        .data(finalAmount)
                        .message("Calculate coupon successfully")
                        .build()
        );
    }
}
