package vn.jully.website_selling_technology_backend.services.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.jully.website_selling_technology_backend.dtos.OrderDTO;
import vn.jully.website_selling_technology_backend.dtos.OrderUpdateDTO;
import vn.jully.website_selling_technology_backend.exceptions.DataNotFoundException;
import vn.jully.website_selling_technology_backend.responses.order.OrderResponse;

import java.util.List;

public interface IOrderService {
    OrderResponse insertOrder (OrderDTO orderDTO) throws DataNotFoundException;

    OrderResponse getOrder (Long id) throws DataNotFoundException;

    Page<OrderResponse> getOrders (String keyword, Pageable pageable);

    OrderResponse updateOrder (Long id, OrderUpdateDTO orderDTO) throws DataNotFoundException;

    Page<OrderResponse> findByUserId (Long userId, Pageable pageable);

    void deleteOrder (Long id) throws DataNotFoundException;

    void hardDeleteOrder (Long id);
}
