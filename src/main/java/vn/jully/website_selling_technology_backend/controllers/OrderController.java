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
import vn.jully.website_selling_technology_backend.dtos.OrderDTO;
import vn.jully.website_selling_technology_backend.dtos.OrderUpdateDTO;
import vn.jully.website_selling_technology_backend.exceptions.DataNotFoundException;
import vn.jully.website_selling_technology_backend.responses.Response;
import vn.jully.website_selling_technology_backend.responses.order.OrderListResponse;
import vn.jully.website_selling_technology_backend.responses.order.OrderResponse;
import vn.jully.website_selling_technology_backend.services.order.IOrderService;
import vn.jully.website_selling_technology_backend.utils.MessageKey;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/orders")
@RequiredArgsConstructor
public class OrderController {
    private final IOrderService orderService;
    private final LocalizationUtils localizationUtils;
    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<Response> insertOrder (
            @RequestBody @Valid OrderDTO orderDTO,
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
            OrderResponse orderResponse = orderService.insertOrder(orderDTO);
            return ResponseEntity.ok(
                    Response.builder()
                            .data(orderResponse)
                            .message(localizationUtils.getLocalizedMessage(MessageKey.INSERT_SUCCESSFULLY))
                            .status(HttpStatus.CREATED)
                            .build()
            );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<Response> getOrder (@PathVariable("id") Long id) throws DataNotFoundException {
        OrderResponse response = orderService.getOrder(id);
        return ResponseEntity.ok(
                Response.builder()
                        .data(response)
                        .message("Get order successfully")
                        .status(HttpStatus.OK)
                        .build()
        );
    }

    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<Response> getOrders (
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "limit", defaultValue = "16") int limit,
            @RequestParam(value = "keyword", defaultValue = "") String keyword
    ) {
        PageRequest pageRequest = PageRequest.of(
                page, limit,
                Sort.by("id").ascending()
        );
        Page<OrderResponse> orderPages = orderService.getOrders(keyword, pageRequest);
        int totalPages = orderPages.getTotalPages();
        return ResponseEntity.ok(
                Response.builder()
                        .data(OrderListResponse.builder()
                                .totalPages(totalPages)
                                .orderResponses(orderPages.getContent())
                                .build())
                        .message("Get orders successfully")
                        .status(HttpStatus.OK)
                        .build()
        );
    }

    @GetMapping("/user/{user_id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<Response> findByUserId (
            @Valid @PathVariable("user_id") Long userId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "limit", defaultValue = "16") int limit
    ) {
        PageRequest pageRequest = PageRequest.of(
                page, limit,
                Sort.by("id").ascending()
        );
        Page<OrderResponse> responses =  orderService.findByUserId(userId, pageRequest);
            return ResponseEntity.ok(
                    Response.builder()
                            .data(OrderListResponse.builder()
                                    .totalPages(responses.getTotalPages())
                                    .orderResponses(responses.getContent())
                                    .build())
                            .status(HttpStatus.OK)
                            .message("Get orders successfully")
                            .build()
            );

    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> updateOrder (
        @Valid @PathVariable Long id,
        @Valid @RequestBody OrderUpdateDTO orderDTO,
        BindingResult result
    ) throws DataNotFoundException {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(
                        Response.builder()
                                .message(localizationUtils.getLocalizedMessage(MessageKey.INVALID_ERROR, errorMessages.toString()))
                                .status(HttpStatus.BAD_REQUEST)
                                .build()
                );
            }
            OrderResponse response = orderService.updateOrder(id, orderDTO);
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
    public ResponseEntity<Response> deleteOrder (@Valid @PathVariable Long id) throws DataNotFoundException {
        // Delete soft => update field active
        orderService.deleteOrder(id);
        return ResponseEntity.ok(
                Response.builder()
                        .status(HttpStatus.OK)
                        .message(localizationUtils.getLocalizedMessage(MessageKey.DELETE_SUCCESSFULLY))
                        .build()
        );
    }
}
