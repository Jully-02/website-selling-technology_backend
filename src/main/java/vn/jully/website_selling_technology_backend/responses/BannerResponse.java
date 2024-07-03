package vn.jully.website_selling_technology_backend.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.jully.website_selling_technology_backend.dtos.BannerDTO;
import vn.jully.website_selling_technology_backend.entities.Banner;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BannerResponse {
    @JsonProperty("message")
    private String message;

    @JsonProperty("banner")
    private Banner banner;
}
