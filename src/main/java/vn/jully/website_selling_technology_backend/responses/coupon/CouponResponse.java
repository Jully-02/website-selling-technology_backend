package vn.jully.website_selling_technology_backend.responses.coupon;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.jully.website_selling_technology_backend.entities.Coupon;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CouponResponse {

    @JsonProperty("coupons")
    private List<Coupon> coupons;

    @JsonProperty("total_pages")
    private int totalPages;
}
