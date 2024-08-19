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
import vn.jully.website_selling_technology_backend.dtos.CouponConditionDTO;
import vn.jully.website_selling_technology_backend.entities.CouponCondition;
import vn.jully.website_selling_technology_backend.exceptions.DataNotFoundException;
import vn.jully.website_selling_technology_backend.responses.Response;
import vn.jully.website_selling_technology_backend.services.coupon_condition.ICouponConditionService;
import vn.jully.website_selling_technology_backend.utils.MessageKey;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/coupon-conditions")
@RequiredArgsConstructor
public class CouponConditionController {
    private final ICouponConditionService conditionService;
    private final LocalizationUtils localizationUtils;

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Response> insertCouponCondition (
            @Valid @RequestBody CouponConditionDTO conditionDTO,
            BindingResult result
    ) throws DataNotFoundException {
             if (result.hasErrors()) {
                 List<String> errorMessages = result.getFieldErrors().stream()
                         .map(FieldError::getDefaultMessage)
                         .toList();
                 return ResponseEntity.badRequest().body(
                         Response.builder()
                                 .message(localizationUtils.getLocalizedMessage(MessageKey.INVALID_ERROR, errorMessages.toString()))
                                 .status(HttpStatus.BAD_REQUEST)
                                 .build()
                 );
             }
        CouponCondition condition = conditionService.insertCouponCondition(conditionDTO);
             return ResponseEntity.ok(
                     Response.builder()
                             .message(localizationUtils.getLocalizedMessage(MessageKey.INSERT_SUCCESSFULLY))
                             .status(HttpStatus.CREATED)
                             .data(condition)
                             .build()
             );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> getCouponConditionById (@PathVariable("id") Long conditionId) throws DataNotFoundException {
        CouponCondition condition = conditionService.getCouponCondition(conditionId);
        return ResponseEntity.ok(
                Response.builder()
                        .status(HttpStatus.OK)
                        .message("Get coupon condition successfully")
                        .data(condition)
                        .build()
        );
    }

    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<Response> getAllCouponCondition () {
        List<CouponCondition> conditions = conditionService.getAllConditions();
        return ResponseEntity.ok(
                Response.builder()
                        .data(conditions)
                        .message("Get all coupon conditions successfully")
                        .status(HttpStatus.OK)
                        .build()
        );
    }

    @GetMapping("/coupons/{coupon-id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<Response> getConditionByCouponId (@PathVariable("coupon-id") Long couponId) {
        List<CouponCondition> conditions = conditionService.getConditionByCouponId(couponId);
        return ResponseEntity.ok(
                Response.builder()
                        .data(conditions)
                        .status(HttpStatus.OK)
                        .message("Get coupon condition by coupon id successfully")
                        .build()
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Response> updateCouponCondition (
            @PathVariable("id") Long conditionId,
            @RequestBody CouponConditionDTO conditionDTO,
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
             CouponCondition condition = conditionService.updateCouponCondition(conditionId, conditionDTO);
             return ResponseEntity.ok(
                     Response.builder()
                             .data(condition)
                             .message(localizationUtils.getLocalizedMessage(MessageKey.UPDATE_SUCCESSFULLY))
                             .status(HttpStatus.OK)
                             .build()
             );

    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Response> deleteCouponCondition (@PathVariable("id") Long conditionId) {
        conditionService.deleteCouponCondition(conditionId);
        return ResponseEntity.ok(
                Response.builder()
                        .message(localizationUtils.getLocalizedMessage(MessageKey.DELETE_SUCCESSFULLY))
                        .status(HttpStatus.OK)
                        .build()
        );
    }
}
