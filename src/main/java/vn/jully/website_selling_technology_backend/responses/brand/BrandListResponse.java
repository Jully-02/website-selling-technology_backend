package vn.jully.website_selling_technology_backend.responses.brand;

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
public class BrandListResponse {
    @JsonProperty("brand_responses")
    private List<BrandResponse> brandResponses;

    @JsonProperty("total_pages")
    private int totalPages;
}