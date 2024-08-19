package vn.jully.website_selling_technology_backend.services.order_detail;

import vn.jully.website_selling_technology_backend.dtos.OrderDetailDTO;
import vn.jully.website_selling_technology_backend.exceptions.DataNotFoundException;
import vn.jully.website_selling_technology_backend.responses.order_detail.OrderDetailResponse;

import java.util.List;

public interface IOrderDetailService {
    OrderDetailResponse insertOrderDetail (OrderDetailDTO orderDetailDTO) throws DataNotFoundException;

    OrderDetailResponse getOrderDetail (Long id) throws DataNotFoundException;

    OrderDetailResponse updateOrderDetail (Long id, OrderDetailDTO orderDetailDTO) throws DataNotFoundException;

    List<OrderDetailResponse> findByOrderId (Long orderId);

    void deleteOrderDetail(Long id);
}

