package vn.jully.website_selling_technology_backend.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import vn.jully.website_selling_technology_backend.entities.Order;

import java.util.List;

public interface OrderRepository  extends JpaRepository<Order, Long> {

    Page<Order> findByUserId (Long userId, Pageable pageable);

    @Query("SELECT o FROM Order o WHERE " +
            "(:keyword IS NULL OR :keyword = '' OR " +
            "o.firstName LIKE %:keyword% OR " +
            "o.lastName LIKE %:keyword% OR " +
            "o.note LIKE %:keyword% OR " +
            "o.email LIKE %:keyword% OR " +
            "o.phoneNumber LIKE %:keyword%)")
    Page<Order> findAll(@Param("keyword") String keyword, Pageable pageable);
}
