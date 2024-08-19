package vn.jully.website_selling_technology_backend.responses.cloudinary;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CloudinaryResponse {
    @JsonProperty("public_id")
    private String publicId;

    private String url;
}
