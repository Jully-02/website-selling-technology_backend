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
public class CategoryListResponse {
    @JsonProperty("category_responses")
    private List<CategoryResponse> categoryResponses;

    @JsonProperty("total_pages")
    private int totalPages;
}
