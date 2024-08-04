package vn.jully.website_selling_technology_backend.responses;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeedBackResponse extends BaseResponse{
    @JsonProperty("user_id")
    private long userId;

    @JsonProperty("product_id")
    private long productId;

    @JsonProperty("comment")
    private String comment;

    @JsonProperty("rate")
    private float rate;

    @JsonProperty("name")
    private String name;
}
