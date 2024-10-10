package vn.jully.website_selling_technology_backend.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.jully.website_selling_technology_backend.dtos.OrderUpdateDTO;
import vn.jully.website_selling_technology_backend.exceptions.DataNotFoundException;
import vn.jully.website_selling_technology_backend.responses.order.OrderResponse;
import vn.jully.website_selling_technology_backend.responses.payment.PaymentResponse;
import vn.jully.website_selling_technology_backend.services.order.IOrderService;
import vn.jully.website_selling_technology_backend.services.order_detail.IOrderDetailService;
import vn.jully.website_selling_technology_backend.services.payment.PaymentService;

import java.io.IOException;

@RestController
@RequestMapping("${api.prefix}/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    private final IOrderService orderService;
    private final IOrderDetailService orderDetailService;
    private final ModelMapper modelMapper;
    @GetMapping("/create-payment")
    public ResponseEntity<PaymentResponse> pay(
            HttpServletRequest request,
            @RequestParam("amount") Long amount,
            @RequestParam("bankCode") String bankCode,
            @RequestParam("orderId") Long orderId

    ) {
        return ResponseEntity.ok(paymentService.createVnPayPayment(amount, bankCode, request, orderId));
    }
    @GetMapping("/vn-pay-callback")
    public void payCallbackHandler(HttpServletRequest request, HttpServletResponse response) throws IOException, DataNotFoundException {
        String status = request.getParameter("vnp_ResponseCode");
        String orderId = request.getParameter("vnp_TxnRef");
        if (status.equals("00")) {
            OrderResponse orderResponse = orderService.getOrder(Long.valueOf(orderId));
            OrderUpdateDTO orderUpdateDTO = modelMapper.map(orderResponse, OrderUpdateDTO.class);
            orderUpdateDTO.setPaymentStatus(true);
            orderService.updateOrder(Long.valueOf(orderId), orderUpdateDTO);
            response.sendRedirect("http://localhost:3000/checkout?vnp_ResponseCode=" + status + "&order_id=" + orderId);
        } else {
            orderDetailService.deleteOrderDetailByOrderId(Long.valueOf(orderId));
            orderService.hardDeleteOrder(Long.valueOf(orderId));
            response.sendRedirect("http://localhost:3000/checkout?vnp_ResponseCode=" + status);
        }
    }
}
