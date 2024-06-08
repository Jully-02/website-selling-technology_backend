package vn.jully.website_selling_technology_backend.services;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import vn.jully.website_selling_technology_backend.dtos.OrderDetailDTO;
import vn.jully.website_selling_technology_backend.entities.Order;
import vn.jully.website_selling_technology_backend.entities.OrderDetail;
import vn.jully.website_selling_technology_backend.entities.Product;
import vn.jully.website_selling_technology_backend.exceptions.DataNotFoundException;
import vn.jully.website_selling_technology_backend.repositories.OrderDetailRepository;
import vn.jully.website_selling_technology_backend.repositories.OrderRepository;
import vn.jully.website_selling_technology_backend.repositories.ProductRepository;
import vn.jully.website_selling_technology_backend.responses.OrderDetailResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailService implements IOrderDetailService{
    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    @Override
    public OrderDetailResponse insertOrderDetail(OrderDetailDTO orderDetailDTO) throws DataNotFoundException {
        Order existingOrder = orderRepository.findById(orderDetailDTO.getOrderId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find Order with ID = " + orderDetailDTO.getOrderId()));
        Product existingProduct = productRepository.findById(orderDetailDTO.getProductId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find Product with ID = " + orderDetailDTO.getProductId()));
        OrderDetail orderDetail = modelMapper.map(orderDetailDTO, OrderDetail.class);
        orderDetail.setOrder(existingOrder);
        orderDetail.setProduct(existingProduct);
        orderDetailRepository.save(orderDetail);

        OrderDetailResponse orderDetailResponse = modelMapper.map(orderDetail, OrderDetailResponse.class);
        orderDetailResponse.setOrderId(orderDetail.getOrder().getId());
        orderDetailResponse.setProductId(orderDetail.getProduct().getId());
        return orderDetailResponse;
    }

    @Override
    public OrderDetailResponse getOrderDetail(Long id) throws DataNotFoundException {
        OrderDetail orderDetail = orderDetailRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find Order detail with ID = " + id));
        OrderDetailResponse orderDetailResponse = modelMapper.map(orderDetail, OrderDetailResponse.class);
        orderDetailResponse.setOrderId(orderDetail.getOrder().getId());
        orderDetailResponse.setProductId(orderDetail.getProduct().getId());
        return orderDetailResponse;
    }

    @Override
    public OrderDetailResponse updateOrderDetail(Long id, OrderDetailDTO orderDetailDTO) throws DataNotFoundException {
        OrderDetail existingOrderDetail = orderDetailRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find Order detail with ID = " + id));

        if (existingOrderDetail.getProduct().getId() != orderDetailDTO.getProductId()) {
            Product product = productRepository.findById(orderDetailDTO.getProductId())
                    .orElseThrow(() -> new DataNotFoundException("Cannot find Product with ID = " + orderDetailDTO.getProductId()));
            existingOrderDetail.setProduct(product);
        }
        if (existingOrderDetail.getOrder().getId() != orderDetailDTO.getOrderId()) {
            Order order = orderRepository.findById(orderDetailDTO.getOrderId())
                    .orElseThrow(() -> new DataNotFoundException("Cannot find Order with ID " + orderDetailDTO.getOrderId()));
            existingOrderDetail.setOrder(order);
        }
        modelMapper.map(orderDetailDTO, existingOrderDetail);
        orderDetailRepository.save(existingOrderDetail);
        OrderDetailResponse orderDetailResponse = modelMapper.map(existingOrderDetail, OrderDetailResponse.class);
        orderDetailResponse.setProductId(existingOrderDetail.getProduct().getId());
        orderDetailResponse.setOrderId(existingOrderDetail.getOrder().getId());
        return orderDetailResponse;
    }

    @Override
    public List<OrderDetailResponse> findByOrderId(Long orderId) {
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(orderId);
        return orderDetails
                .stream()
                .map(orderDetail -> {
                    OrderDetailResponse orderDetailResponse = modelMapper.map(orderDetail, OrderDetailResponse.class);
                    orderDetailResponse.setOrderId(orderDetail.getOrder().getId());
                    orderDetailResponse.setProductId(orderDetail.getProduct().getId());
                    return orderDetailResponse;
                })
                .toList();
    }

    @Override
    public void deleteOrderDetail(Long id) {
        orderDetailRepository.deleteById(id);
    }
}
