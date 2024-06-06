package vn.jully.website_selling_technology_backend.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BrandDTO {
    @NotEmpty(message = "Brand's name cannot be empty")
    private String name;
}
