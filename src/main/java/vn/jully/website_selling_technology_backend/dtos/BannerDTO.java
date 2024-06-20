package vn.jully.website_selling_technology_backend.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BannerDTO {
    @JsonProperty("name")
    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 200, message = "Name must be between 3 and 200 characters")
    private String name;

    @JsonProperty("model_code")
    @Size(min = 0, max = 100, message = "Model code must be between 0 and 100 characters")
    private String modelCode;

    @JsonProperty("promotion_title")
    @Size(min = 0, max = 200, message = "Promotion title must be between 0 and 200 characters")
    private String promotionTitle;

    @JsonProperty("discount")
    @Size(min = 0, max = 100, message = "Discount/Price code must be between 0 and 100 characters")
    private String discount;
}
