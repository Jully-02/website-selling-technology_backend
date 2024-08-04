package vn.jully.website_selling_technology_backend.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import vn.jully.website_selling_technology_backend.components.LocalizationUtils;
import vn.jully.website_selling_technology_backend.dtos.FeedBackDTO;
import vn.jully.website_selling_technology_backend.responses.FeedBackResponse;
import vn.jully.website_selling_technology_backend.responses.Response;
import vn.jully.website_selling_technology_backend.services.IFeedBackService;
import vn.jully.website_selling_technology_backend.utils.MessageKey;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/feedbacks")
@RequiredArgsConstructor
public class FeedBackController {
    private final IFeedBackService feedBackService;
    private final LocalizationUtils localizationUtils;

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Response<FeedBackResponse>> insertFeedback (
            @Valid @RequestBody FeedBackDTO feedBackDTO,
            BindingResult result
    ) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(
                        Response.<FeedBackResponse>
                                builder()
                                .message(localizationUtils.getLocalizedMessage(MessageKey.INVALID_ERROR, errorMessages.toString()))
                                .build()
                );
            }
            return ResponseEntity.ok(
                    Response.<FeedBackResponse>
                            builder()
                            .message(localizationUtils.getLocalizedMessage(MessageKey.INSERT_SUCCESSFULLY))
                            .data(feedBackService.insertFeedBack(feedBackDTO))
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Response.<FeedBackResponse>
                            builder()
                            .message(localizationUtils.getLocalizedMessage(MessageKey.INSERT_FAILED, e.getMessage()))
                            .build()
            );
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<FeedBackResponse> getFeedback (@PathVariable("id") Long id) throws Exception {
        return ResponseEntity.ok(feedBackService.getFeedBackById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<Response<FeedBackResponse>> updateFeedback (
            @PathVariable("id") Long id,
            @Valid @RequestBody FeedBackDTO feedBackDTO,
            BindingResult result
    ) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(
                        Response.<FeedBackResponse>
                                        builder()
                                .message(localizationUtils.getLocalizedMessage(MessageKey.UPDATE_FAILED, errorMessages.toString()))
                                .build()
                );
            }
            return ResponseEntity.ok(
                    Response.<FeedBackResponse>
                                    builder()
                            .message(localizationUtils.getLocalizedMessage(MessageKey.UPDATE_SUCCESSFULLY))
                            .data(feedBackService.updateFeedBack(id, feedBackDTO))
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Response.<FeedBackResponse>
                                    builder()
                            .message(localizationUtils.getLocalizedMessage(MessageKey.UPDATE_FAILED, e.getMessage()))
                            .build()
            );
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<Response<FeedBackResponse>> deleteFeedback (@PathVariable("id") Long id) {
        feedBackService.deleteFeedBackById(id);
        return ResponseEntity.ok(
                Response.<FeedBackResponse>
                        builder()
                        .message(localizationUtils.getLocalizedMessage(MessageKey.DELETE_SUCCESSFULLY))
                        .build()
        );
    }

    @GetMapping("/user/{user_id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<Response<FeedBackResponse>> getFeedbackByUserId (@PathVariable("user_id") Long userId) {
        return ResponseEntity.ok(
                Response.<FeedBackResponse>
                        builder()
                        .dataList(feedBackService.getFeedBackByUserId(userId))
                        .build()
        );
    }

    @GetMapping("/product/{product_id}")
    public ResponseEntity<Response<FeedBackResponse>> getFeedbackByProductId (@PathVariable("product_id") Long productId) {
        return ResponseEntity.ok(
                Response.<FeedBackResponse>
                                builder()
                        .dataList(feedBackService.getFeedBackByProductId(productId))
                        .build()
        );
    }
}
