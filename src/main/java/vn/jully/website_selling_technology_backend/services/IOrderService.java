package vn.jully.website_selling_technology_backend.services;

import vn.jully.website_selling_technology_backend.dtos.OrderDTO;
import vn.jully.website_selling_technology_backend.entities.Order;
import vn.jully.website_selling_technology_backend.exceptions.DataNotFoundException;
import vn.jully.website_selling_technology_backend.responses.OrderResponse;

import java.util.List;

public interface IOrderService {
    OrderResponse insertOrder (OrderDTO orderDTO) throws DataNotFoundException;

    OrderResponse getOrder (Long id) throws DataNotFoundException;

    OrderResponse updateOrder (Long id, OrderDTO orderDTO) throws DataNotFoundException;

    List<OrderResponse> findByUserId (Long userId);

    void deleteOrder (Long id) throws DataNotFoundException;
}
