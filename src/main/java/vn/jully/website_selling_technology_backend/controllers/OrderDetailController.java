package vn.jully.website_selling_technology_backend.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import vn.jully.website_selling_technology_backend.dtos.OrderDTO;
import vn.jully.website_selling_technology_backend.dtos.OrderDetailDTO;
import vn.jully.website_selling_technology_backend.entities.OrderDetail;
import vn.jully.website_selling_technology_backend.exceptions.DataNotFoundException;
import vn.jully.website_selling_technology_backend.responses.OrderDetailResponse;
import vn.jully.website_selling_technology_backend.services.IOrderDetailService;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/order_details")
@RequiredArgsConstructor
public class OrderDetailController {
    private final IOrderDetailService orderDetailService;
    @PostMapping("")
    public ResponseEntity<?> insertOrderDetail (
            @Valid @RequestBody OrderDetailDTO orderDetailDTO,
            BindingResult result
    ) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            OrderDetailResponse orderDetailResponse = orderDetailService.insertOrderDetail(orderDetailDTO);
            return ResponseEntity.ok(orderDetailResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetail (@PathVariable("id") Long id) throws DataNotFoundException {
        return ResponseEntity.ok(orderDetailService.getOrderDetail(id));
    }

    @GetMapping("/order/{order_id}")
    public ResponseEntity<?> findByOrderId (@PathVariable("order_id") Long orderId) {
        return ResponseEntity.ok(orderDetailService.findByOrderId(orderId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderDetail (
            @PathVariable("id") Long id,
            @Valid @RequestBody OrderDetailDTO orderDetailDTO,
            BindingResult result
    ) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            return ResponseEntity.ok(orderDetailService.updateOrderDetail(id, orderDetailDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderDetail (@PathVariable("id") Long id) {
        orderDetailService.deleteOrderDetail(id);
        return ResponseEntity.ok("Deleted Order detail successfully");
    }
}
