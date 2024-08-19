package vn.jully.website_selling_technology_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.jully.website_selling_technology_backend.entities.CouponCondition;

import java.util.List;

@Repository
public interface CouponConditionRepository extends JpaRepository<CouponCondition, Long> {

    List<CouponCondition> findByCouponId (Long couponId);
}
