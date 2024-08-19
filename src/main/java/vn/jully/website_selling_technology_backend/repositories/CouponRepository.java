package vn.jully.website_selling_technology_backend.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.jully.website_selling_technology_backend.entities.Coupon;

import java.util.Optional;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {
    Optional<Coupon> findByCode (String couponCode);

    @Query("SELECT c FROM Coupon c WHERE " +
            "(:keyword IS NULL OR :keyword = '' OR " +
            "c.code LIKE %:keyword%)")
    Page<Coupon> findAll (@Param("keyword") String keyword, Pageable pageable);
}
