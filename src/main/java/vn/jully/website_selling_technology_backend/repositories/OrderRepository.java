package vn.jully.website_selling_technology_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.jully.website_selling_technology_backend.entities.Order;

import java.util.List;

public interface OrderRepository  extends JpaRepository<Order, Long> {
    List<Order> findByUserId (Long userId);
}
