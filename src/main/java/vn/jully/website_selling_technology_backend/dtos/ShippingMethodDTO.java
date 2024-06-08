package vn.jully.website_selling_technology_backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShippingMethodDTO {
    private String name;

    private String description;

    private float cost;
}
