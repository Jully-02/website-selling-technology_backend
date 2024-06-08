package vn.jully.website_selling_technology_backend.services;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import vn.jully.website_selling_technology_backend.dtos.OrderDTO;
import vn.jully.website_selling_technology_backend.entities.*;
import vn.jully.website_selling_technology_backend.exceptions.DataNotFoundException;
import vn.jully.website_selling_technology_backend.repositories.OrderRepository;
import vn.jully.website_selling_technology_backend.repositories.PaymentMethodRepository;
import vn.jully.website_selling_technology_backend.repositories.ShippingMethodRepository;
import vn.jully.website_selling_technology_backend.repositories.UserRepository;
import vn.jully.website_selling_technology_backend.responses.OrderResponse;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ShippingMethodRepository shippingMethodRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final ModelMapper modelMapper;
    @Override
    public OrderResponse insertOrder(OrderDTO orderDTO) throws DataNotFoundException {
        User existingUser = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find User with ID = " + orderDTO.getUserId()));

        ShippingMethod existingShippingMethod = shippingMethodRepository.findById(orderDTO.getShippingMethodId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find Shipping method with ID = " + orderDTO.getShippingMethodId()));
        PaymentMethod existingPaymentMethod = paymentMethodRepository.findById(orderDTO.getPaymentMethodId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find Payment method with ID = " + orderDTO.getPaymentMethodId()));

        Order order = modelMapper.map(orderDTO, Order.class);

        order.setUser(existingUser);
        order.setShippingMethod(existingShippingMethod);
        order.setPaymentMethod(existingPaymentMethod);
        order.setPaymentCost(existingPaymentMethod.getCost());
        order.setShippingCost(existingShippingMethod.getCost());

        order.setStatus(OrderStatus.PENDING);
        order.setOrderDate(new Date());
        LocalDate shippingDate = orderDTO.getShippingDate() == null ? LocalDate.now() : orderDTO.getShippingDate();
        if (shippingDate.isBefore(LocalDate.now())) {
            throw new DataNotFoundException("Date must be at least today");
        }
        order.setShippingDate(shippingDate);
        order.setActive(true);
        orderRepository.save(order);
        OrderResponse orderResponse = modelMapper.map(order, OrderResponse.class);
        orderResponse.setUserId(order.getUser().getId());
        orderResponse.setShippingMethodId(order.getShippingMethod().getId());
        orderResponse.setPaymentMethodId(order.getPaymentMethod().getId());
//        List<Long> orderDetailIds = order.getOrderDetailList()
//                .stream()
//                .map(OrderDetail::getId)
//                .toList();
//        orderResponse.setOrderDetailIds(orderDetailIds);
        return orderResponse;
    }

    @Override
    public OrderResponse getOrder(Long id) throws DataNotFoundException {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find Order with ID = " + id));
        OrderResponse orderResponse = modelMapper.map(order, OrderResponse.class);
        orderResponse.setShippingMethodId(order.getShippingMethod().getId());
        orderResponse.setPaymentMethodId(order.getPaymentMethod().getId());
        orderResponse.setUserId(order.getUser().getId());
        return orderResponse;
    }

    @Override
    public OrderResponse updateOrder(Long id, OrderDTO orderDTO) throws DataNotFoundException {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find Order with ID = " + id));
        modelMapper.map(orderDTO, existingOrder);
        if (existingOrder.getUser().getId() != orderDTO.getUserId()) {
            User existingUser = userRepository.findById(orderDTO.getUserId())
                    .orElseThrow(() -> new DataNotFoundException("Cannot find User with ID = " + orderDTO.getUserId()));
            existingOrder.setUser(existingUser);
        }
        if (existingOrder.getShippingMethod().getId() != orderDTO.getShippingMethodId()) {
            ShippingMethod existingShippingMethod = shippingMethodRepository.findById(orderDTO.getShippingMethodId())
                    .orElseThrow(() -> new DataNotFoundException("Cannot find Shipping method with ID = " + orderDTO.getShippingMethodId()));
            existingOrder.setShippingMethod(existingShippingMethod);
            existingOrder.setShippingCost(existingShippingMethod.getCost());
        }
        if (existingOrder.getPaymentMethod().getId() != orderDTO.getPaymentMethodId()) {
            PaymentMethod existingPaymentMethod = paymentMethodRepository.findById(orderDTO.getPaymentMethodId())
                    .orElseThrow(() -> new DataNotFoundException("Cannot find Payment method with ID = " + orderDTO.getPaymentMethodId()));
            existingOrder.setPaymentMethod(existingPaymentMethod);
            existingOrder.setPaymentCost(existingPaymentMethod.getCost());
        }
        orderRepository.save(existingOrder);
        OrderResponse orderResponse = modelMapper.map(existingOrder, OrderResponse.class);
        orderResponse.setShippingMethodId(existingOrder.getShippingMethod().getId());
        orderResponse.setPaymentMethodId(existingOrder.getPaymentMethod().getId());
        orderResponse.setUserId(existingOrder.getUser().getId());

        return orderResponse;
    }

    @Override
    public List<OrderResponse> findByUserId (Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders
                .stream()
                .map(order -> {
                    OrderResponse orderResponse = modelMapper.map(order,OrderResponse.class);
                    orderResponse.setShippingMethodId(order.getShippingMethod().getId());
                    orderResponse.setPaymentMethodId(order.getPaymentMethod().getId());
                    orderResponse.setUserId(order.getUser().getId());
                    return orderResponse;
                })
                .toList();
    }

    @Override
    public void deleteOrder(Long id) throws DataNotFoundException {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find Order with ID = " + id));
        existingOrder.setActive(false);
        orderRepository.save(existingOrder);
    }
}
