package vn.jully.website_selling_technology_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import vn.jully.website_selling_technology_backend.entities.OrderDetail;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    List<OrderDetail> findByOrderId (Long orderId);


    @Modifying
    @Query("DELETE FROM OrderDetail od WHERE od.order.id = :orderId")
    void deleteOrderDetailByOrderId(Long orderId);
}
