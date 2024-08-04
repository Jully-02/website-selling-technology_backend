package vn.jully.website_selling_technology_backend.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import vn.jully.website_selling_technology_backend.controllers.ProductController;
import vn.jully.website_selling_technology_backend.dtos.CartItemDTO;
import vn.jully.website_selling_technology_backend.dtos.OrderDTO;
import vn.jully.website_selling_technology_backend.dtos.OrderUpdateDTO;
import vn.jully.website_selling_technology_backend.entities.*;
import vn.jully.website_selling_technology_backend.exceptions.DataNotFoundException;
import vn.jully.website_selling_technology_backend.repositories.*;
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
    private final OrderDetailRepository orderDetailRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
    @Override
    @Transactional
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
        order.setPaymentStatus(false);
        order.setOrderDate(new Date());
        order.setTrackingNumber(1);
//        LocalDate shippingDate = orderDTO.getShippingDate() == null ? LocalDate.now() : orderDTO.getShippingDate();
//        if (shippingDate.isBefore(LocalDate.now())) {
//            throw new DataNotFoundException("Date must be at least today");
//        }
//        order.setShippingDate(shippingDate);
        order.setActive(true);
        orderRepository.save(order);


        List<OrderDetail> orderDetails = new ArrayList<>();
        for (CartItemDTO cartItemDTO : orderDTO.getCartItems()) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            Long productId = cartItemDTO.getProductId();
            int quantity = cartItemDTO.getQuantity();

            Product existingProduct = productRepository.findById(productId)
                    .orElseThrow(() -> new DataNotFoundException("Cannot find Product with ID = " + productId));

            orderDetail.setProduct(existingProduct);
            orderDetail.setNumberOfProduct(quantity);
            orderDetail.setPrice(existingProduct.getPrice());
            orderDetail.setTotalMoney(orderDetail.getNumberOfProduct() * orderDetail.getPrice());
            orderDetails.add(orderDetail);
        }
        orderDetailRepository.saveAll(orderDetails);
        
        order.setSubTotal(order.getTotalMoney());
        order.setTotalMoney(order.getTotalMoney() + existingShippingMethod.getCost() + existingPaymentMethod.getCost());

        orderRepository.save(order);
        OrderResponse orderResponse = modelMapper.map(order, OrderResponse.class);
        orderResponse.setUserId(order.getUser().getId());
        orderResponse.setShippingMethodId(order.getShippingMethod().getId());
        orderResponse.setPaymentMethodId(order.getPaymentMethod().getId());
        orderResponse.setShippingMethodName(order.getShippingMethod().getName());
        orderResponse.setPaymentMethodName(order.getPaymentMethod().getName());
        order.setPaymentStatus(orderDTO.isPaymentStatus());

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
        orderResponse.setShippingMethodName(order.getShippingMethod().getName());
        orderResponse.setPaymentMethodName(order.getPaymentMethod().getName());
        return orderResponse;
    }

    @Override
    public List<OrderResponse> getOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(order -> {
                    OrderResponse orderResponse = modelMapper.map(order, OrderResponse.class);
                    orderResponse.setShippingMethodId(order.getShippingMethod().getId());
                    orderResponse.setPaymentMethodId(order.getPaymentMethod().getId());
                    orderResponse.setUserId(order.getUser().getId());
                    orderResponse.setShippingMethodName(order.getShippingMethod().getName());
                    orderResponse.setPaymentMethodName(order.getPaymentMethod().getName());
                    return orderResponse;
                }).toList();
    }

    @Override
    @Transactional
    public OrderResponse updateOrder(Long id, OrderUpdateDTO orderDTO) throws DataNotFoundException {
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
        orderResponse.setShippingMethodName(existingOrder.getShippingMethod().getName());
        orderResponse.setPaymentMethodName(existingOrder.getPaymentMethod().getName());

        return orderResponse;
    }

    @Override
    public List<OrderResponse> findByUserId (Long userId) {
            List<Order> orders = orderRepository.findByUserId(userId);
            return orders.stream()
                    .map(order -> {
                        OrderResponse orderResponse = modelMapper.map(order, OrderResponse.class);
                        orderResponse.setPaymentMethodId(order.getPaymentMethod().getId());
                        orderResponse.setShippingMethodId(order.getShippingMethod().getId());
                        orderResponse.setUserId(order.getUser().getId());
                        orderResponse.setShippingMethodName(order.getShippingMethod().getName());
                        orderResponse.setPaymentMethodName(order.getPaymentMethod().getName());
                        return orderResponse;
                    }).toList();
    }

    @Override
    @Transactional
    public void deleteOrder(Long id) throws DataNotFoundException {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find Order with ID = " + id));
        existingOrder.setActive(false);
        orderRepository.save(existingOrder);
    }
}
