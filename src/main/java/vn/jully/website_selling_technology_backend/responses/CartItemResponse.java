package vn.jully.website_selling_technology_backend.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.jully.website_selling_technology_backend.entities.CartItem;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemResponse {
    @JsonProperty("message")
    private String message;

    @JsonProperty("cart_item")
    private CartItem cartItem;
}
