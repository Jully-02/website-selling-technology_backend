package vn.jully.website_selling_technology_backend.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemDTO {

    @Min(value = 1, message = "User's ID must be > 0")
    @JsonProperty("user_id")
    private long userId;

    @JsonProperty("product_id")
    @Min(value = 1, message = "Product's ID must be > 0")
    private long productId;

    @JsonProperty("quantity")
    @Min(value = 1, message = "Quantity must be > 0")
    private int quantity;
}
