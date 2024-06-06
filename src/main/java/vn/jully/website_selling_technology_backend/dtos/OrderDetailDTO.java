package vn.jully.website_selling_technology_backend.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailDTO {
    @JsonProperty("number_of_product")
    @Min(value = 1, message = "Number of products must be > 0")
    private int numberOfProduct;

    @Min(value = 0, message = "Product's ID must be >= 0")
    private float price;

    @Min(value = 0, message = "Total money must be >= 0")
    @JsonProperty("total_money")
    private float totalMoney;

    @JsonProperty("order_id")
    @NotNull(message = "Order's ID is required")
    @Min(value = 1, message = "Order's ID must be > 0")
    private Long orderId;

    @JsonProperty("product_id")
    @NotNull(message = "Product ID is required")
    @Min(value = 1, message = "Product's ID must be > 0")
    private Long productId;
}
