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
import vn.jully.website_selling_technology_backend.dtos.FeedBackDTO;
import vn.jully.website_selling_technology_backend.responses.feedback.FeedBackResponse;
import vn.jully.website_selling_technology_backend.responses.Response;
import vn.jully.website_selling_technology_backend.services.feedback.IFeedBackService;
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
    public ResponseEntity<Response> insertFeedback (
            @Valid @RequestBody FeedBackDTO feedBackDTO,
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
            return ResponseEntity.ok(
                    Response
                            .builder()
                            .status(HttpStatus.CREATED)
                            .message(localizationUtils.getLocalizedMessage(MessageKey.INSERT_SUCCESSFULLY))
                            .data(feedBackService.insertFeedBack(feedBackDTO))
                            .build()
            );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getFeedback (@PathVariable("id") Long id) throws Exception {
        FeedBackResponse feedBackResponse = feedBackService.getFeedBackById(id);
        return ResponseEntity.ok(
                Response.builder()
                        .status(HttpStatus.OK)
                        .message("Get feedback successfully")
                        .data(feedBackResponse)
                        .build()
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<Response> updateFeedback (
            @PathVariable("id") Long id,
            @Valid @RequestBody FeedBackDTO feedBackDTO,
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
                                .message(localizationUtils.getLocalizedMessage(MessageKey.UPDATE_FAILED, errorMessages.toString()))
                                .build()
                );
            }
            FeedBackResponse feedBackResponse = feedBackService.updateFeedBack(id, feedBackDTO);
            return ResponseEntity.ok(
                    Response
                            .builder()
                            .status(HttpStatus.OK)
                            .message(localizationUtils.getLocalizedMessage(MessageKey.UPDATE_SUCCESSFULLY))
                            .data(feedBackResponse)
                            .build()
            );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<Response> deleteFeedback (@PathVariable("id") Long id) {
        feedBackService.deleteFeedBackById(id);
        return ResponseEntity.ok(
                Response
                        .builder()
                        .status(HttpStatus.OK)
                        .message(localizationUtils.getLocalizedMessage(MessageKey.DELETE_SUCCESSFULLY))
                        .build()
        );
    }

    @GetMapping("/user/{user_id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<Response> getFeedbackByUserId (@PathVariable("user_id") Long userId) {
        List<FeedBackResponse> response = feedBackService.getFeedBackByUserId(userId);
        return ResponseEntity.ok(
                Response
                        .builder()
                        .data(response)
                        .status(HttpStatus.OK)
                        .message("Get feedback successfully")
                        .build()
        );
    }

    @GetMapping("/product/{product_id}")
    public ResponseEntity<Response> getFeedbackByProductId (@PathVariable("product_id") Long productId) {
        List<FeedBackResponse> responses = feedBackService.getFeedBackByProductId(productId);
        return ResponseEntity.ok(
                Response
                        .builder()
                        .data(responses)
                        .message("Get feedback successfully")
                        .status(HttpStatus.OK)
                        .build()
        );
    }
}
