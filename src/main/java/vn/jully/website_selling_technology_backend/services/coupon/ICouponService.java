package vn.jully.website_selling_technology_backend.services.coupon;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.jully.website_selling_technology_backend.dtos.CouponDTO;
import vn.jully.website_selling_technology_backend.entities.Coupon;
import vn.jully.website_selling_technology_backend.exceptions.DataNotFoundException;

import java.util.List;

public interface ICouponService {

    Coupon insertCoupon (CouponDTO couponDTO);

    Coupon updateCoupon (Long couponId, CouponDTO couponDTO) throws DataNotFoundException;

    Coupon getCouponById (Long couponId) throws DataNotFoundException;

    Page<Coupon> getAllCoupons (String keyword, Pageable pageable);

    void deleteCoupon (Long couponId);

    double calculateCouponValue (String couponCode, double totalAmount) throws DataNotFoundException;
}
