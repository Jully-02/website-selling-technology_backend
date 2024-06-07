package vn.jully.website_selling_technology_backend.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpecificationDTO {

    @JsonProperty("weight")
    @Min(value = 0, message = "Weight must be greater than or equal to 0")
    @Max(value = 1000, message = "Weight must be less than or equal to 1000")
    private float weight;

    @JsonProperty("dimension")
    @Size(min = 3, max = 100, message = "Dimension must be between 3 and 100 characters")
    private String dimension;

    @JsonProperty("size")
    @Size(min = 3, max = 100, message = "Size must be between 3 and 100 characters")
    private String size;

    @JsonProperty("color")
    @Size(min = 3, max = 100, message = "Color must be between 3 and 100 characters")
    private String color;

    @JsonProperty("guarantee")
    @Min(value = 0, message = "Guarantee must be greater than or equal to 0")
    @Max(value = 10, message = "Guarantee must be less than or equal to 10")
    private float guarantee;

    @JsonProperty("item_height")
    @Size(min = 3, max = 100, message = "Item height must be between 3 and 100 characters")
    private String itemHeight;

    @JsonProperty("item_width")
    @Size(min = 3, max = 100, message = "Item width must be between 3 and 100 characters")
    private String itemWidth;

    @JsonProperty("screen_size")
    @Min(value = 0, message = "Screen size must be greater than or equal to 0")
    @Max(value = 1000, message = "Screen size must be less than or equal to 1000")
    private float screenSize;

    @JsonProperty("item_weight")
    @Min(value = 0, message = "Item weight must be greater than or equal to 0")
    @Max(value = 1000, message = "Item weight must be less than or equal to 1000")
    private float itemWeight;

    @JsonProperty("product_dimension")
    @Size(min = 3, max = 100, message = "Product dimension must be between 3 and 100 characters")
    private String productDimension;

    @JsonProperty("item_model_number")
    @Size(min = 3, max = 100, message = "Item model number must be between 3 and 100 characters")
    private String itemModelNumber;

    @JsonProperty("processor_brand")
    @Size(min = 3, max = 100, message = "Processor brand must be between 3 and 100 characters")
    private String processorBrand;

    @JsonProperty("processor_type")
    @Size(min = 3, max = 100, message = "Processor type must be between 3 and 100 characters")
    private String processorType;

    @JsonProperty("processor_speed")
    @Min(value = 0, message = "Processor speed must be greater than or equal to 0")
    @Max(value = 1000, message = "Processor speed must be less than or equal to 1000")
    private float processorSpeed;

    @JsonProperty("ram_size")
    @Min(value = 0, message = "Ram size must be greater than or equal to 0")
    @Max(value = 1000, message = "Ram size must be less than or equal to 1000")
    private int ramSize;

    @JsonProperty("hard_drive_size")
    @Min(value = 0, message = "Hard drive size must be greater than or equal to 0")
    @Max(value = 1000, message = "Hard drive size must be less than or equal to 1000")
    private int hardDriveSize;

    @JsonProperty("hard_disk_technology")
    @Size(min = 3, max = 100, message = "Hard disk technology must be between 3 and 100 characters")
    private String hardDiskTechnology;

    @JsonProperty("graphics_coprocessor")
    @Size(min = 3, max = 100, message = "Graphics coprocessor must be between 3 and 100 characters")
    private String graphicsCoprocessor;

    @JsonProperty("graphic_card_desc")
    @Size(min = 3, max = 100, message = "Graphic card desc must be between 3 and 100 characters")
    private String graphicCardDesc;

    @JsonProperty("hardware_platform")
    @Size(min = 3, max = 100, message = "Hardware platform must be between 3 and 100 characters")
    private String hardwarePlatform;

    @JsonProperty("operating_system")
    @Size(min = 3, max = 100, message = "Operating system platform must be between 3 and 100 characters")
    private String operatingSystem;

    @JsonProperty("average_battery_life")
    @Min(value = 1, message = "Average battery life must be greater than or equal to 1")
    @Max(value = 2000, message = "Average battery life must be less than or equal to 2000")
    private float averageBatteryLife;

    @JsonProperty("product_id")
    @Min(value = 1, message = "Product's ID must be > 0")
    private Long productId;
}
