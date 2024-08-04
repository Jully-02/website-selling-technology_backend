package vn.jully.website_selling_technology_backend.components;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;

@Component
public class CustomHealthCheck implements HealthIndicator {
    @Override
    public Health health() {
        // Implement your custom health check logic here
        try {
            String computerName= InetAddress.getLocalHost().getHostName();
            return Health.up().withDetail("computerName", computerName).build();
        } catch (Exception e) {
            // throw new RuntimeException(e);
            return Health.down()
                    .withDetail("Error", e.getMessage()).build();
        }
    }
}
