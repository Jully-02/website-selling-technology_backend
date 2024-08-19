package vn.jully.website_selling_technology_backend.responses.favorite;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FavoriteProductResponse {
    @JsonProperty("user_id")
    private long userId;

    @JsonProperty("product_id")
    private long productId;
}
