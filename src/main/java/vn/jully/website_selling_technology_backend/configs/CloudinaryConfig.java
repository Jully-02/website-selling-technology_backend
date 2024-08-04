package vn.jully.website_selling_technology_backend.configs;

import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class CloudinaryConfig {
    private final String CLOUD_NAME = "dsr056xbw";
    private final String API_KEY = "667324118628331";
    private final String API_SECRET = "1UVU9sOZWU0G219nanFVkQat8_A";

    @Bean
    public Cloudinary cloudinary () {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", CLOUD_NAME);
        config.put("api_key", API_KEY);
        config.put("api_secret", API_SECRET);
        return new Cloudinary(config);
    }
}
