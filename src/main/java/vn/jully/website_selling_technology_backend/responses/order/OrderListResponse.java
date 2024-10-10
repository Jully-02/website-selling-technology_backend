package vn.jully.website_selling_technology_backend.responses.order;

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
public class OrderListResponse {

    @JsonProperty("order_responses")
    private List<OrderResponse> orderResponses;

    @JsonProperty("total_pages")
    private int totalPages;
}
