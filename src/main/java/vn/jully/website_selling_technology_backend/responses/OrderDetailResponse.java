package vn.jully.website_selling_technology_backend.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailResponse {
    private Long id;

    @JsonProperty("product_id")
    private Long productId;

    private float price;

    @JsonProperty("number_of_product")
    private int numberOfProduct;

    @JsonProperty("total_money")
    private float totalMoney;

    @JsonProperty("order_id")
    private Long orderId;
}
