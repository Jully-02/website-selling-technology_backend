package vn.jully.website_selling_technology_backend.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse extends BaseResponse{

    private long id;

    private String title;

    private float price;

    private String description;

    private String thumbnail;

    private float discount;

    @JsonProperty("average_rate")
    private float averageRate;

    @JsonProperty("brand_id")

    private Long brandId;

    @JsonProperty("category_ids")
    private List<Long> categoryIds;
}
