package vn.jully.website_selling_technology_backend.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    @NotBlank(message = "Title is required")
    @Size(min = 10, max = 500, message = "Title must be between 10 and 500 characters")
    private String title;

    @Min(value = 0, message = "Price must be greater than or equal to 0")
    @Max(value = 10000000, message = "Price must be less than or equal to 10,000,000")
    private float price;

    private String description;

    private String thumbnail;

    @Min(value = 0, message = "Discount must be greater than or equal to 0")
    @Max(value = 100, message = "Discount must be less than or equal to 100")
    private float discount;

    @Min(value = 0, message = "Average rate must be greater than or equal to 0")
    @Max(value = 5, message = "Average must be less than or equal to 5")
    @JsonProperty("average_rate")
    private float averageRate;

//    @NotNull(message = "Brand ID is required")
    @JsonProperty("brand_id")
    @Min(value = 1, message = "Brand's ID must be > 0")
    private Long brandId;

    @NotNull(message = "Category ID are required")
    @JsonProperty("category_ids")
    @Size(min = 1, message = "There must be at least one Category ID")
    private List<Long> categoryIds;
}
