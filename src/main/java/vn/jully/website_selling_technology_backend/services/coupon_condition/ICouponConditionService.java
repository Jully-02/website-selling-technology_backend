package vn.jully.website_selling_technology_backend.services.coupon_condition;

import vn.jully.website_selling_technology_backend.dtos.CouponConditionDTO;
import vn.jully.website_selling_technology_backend.entities.CouponCondition;
import vn.jully.website_selling_technology_backend.exceptions.DataNotFoundException;

import java.util.List;

public interface ICouponConditionService {
    CouponCondition insertCouponCondition (CouponConditionDTO couponConditionDTO) throws DataNotFoundException;

    CouponCondition updateCouponCondition (Long conditionId, CouponConditionDTO conditionDTO) throws DataNotFoundException;

    CouponCondition getCouponCondition (Long conditionId) throws DataNotFoundException;

    List<CouponCondition> getAllConditions ();

    List<CouponCondition> getConditionByCouponId (Long couponId);

    void deleteCouponCondition (Long conditionId);
}
