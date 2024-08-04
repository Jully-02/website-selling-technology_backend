package vn.jully.website_selling_technology_backend.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import vn.jully.website_selling_technology_backend.filters.JwtTokenFilter;

import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpMethod.GET;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class WebSecurityConfig {
    private final JwtTokenFilter jwtTokenFilter;

    @Value("${api.prefix}")
    private String apiPrefix;
    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {
        http
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(requests -> {
                       requests
                               .requestMatchers(
                                       String.format("%s/users/register", apiPrefix),
                                       String.format("%s/users/login", apiPrefix),
                                       String.format("%s/users/details", apiPrefix),
                                       String.format("%s/users/email-unique", apiPrefix),
                                       String.format("%s/users/active-account", apiPrefix)

                               )
                               .permitAll()
//                               .requestMatchers(HttpMethod.GET,
//                                       String.format("%s/cart-items", apiPrefix)).permitAll()
//                               .requestMatchers(HttpMethod.GET,
//                                       String.format("%s/cart-items/**", apiPrefix)).permitAll()
//                               .requestMatchers(HttpMethod.PUT,
//                                       String.format("%s/cart-items/**", apiPrefix)).permitAll()
//                               .requestMatchers(HttpMethod.POST,
//                                       String.format("%s/cart-items", apiPrefix)).permitAll()
//                               .requestMatchers(HttpMethod.DELETE,
//                                       String.format("%s/cart-items/**", apiPrefix)).permitAll()

//                               .requestMatchers(HttpMethod.GET,
//                                       String.format("%s/favorites", apiPrefix)).permitAll()
//                               .requestMatchers(HttpMethod.GET,
//                                       String.format("%s/favorites/**", apiPrefix)).permitAll()
//                               .requestMatchers(HttpMethod.PUT,
//                                       String.format("%s/favorites/**", apiPrefix)).permitAll()
//                               .requestMatchers(HttpMethod.POST,
//                                       String.format("%s/favorites", apiPrefix)).permitAll()
//                               .requestMatchers(HttpMethod.DELETE,
//                                       String.format("%s/favorites/**", apiPrefix)).permitAll()
//                               .requestMatchers(HttpMethod.DELETE,
//                                       String.format("%s/favorites/user-product**", apiPrefix)).permitAll()

                               .requestMatchers(HttpMethod.GET,
                                       String.format("%s/payment-methods", apiPrefix)).permitAll()
                               .requestMatchers(HttpMethod.GET,
                                       String.format("%s/payment-methods/**", apiPrefix)).permitAll()
                               .requestMatchers(HttpMethod.GET,
                                       String.format("%s/payments/vn-pay-callback**", apiPrefix)).permitAll()

                               .requestMatchers(HttpMethod.GET,
                                       String.format("%s/shipping-methods", apiPrefix)).permitAll()
                               .requestMatchers(HttpMethod.GET,
                                       String.format("%s/shipping-methods/**", apiPrefix)).permitAll()

                               .requestMatchers(HttpMethod.GET,
                                       String.format("%s/roles**", apiPrefix)).permitAll()

                               .requestMatchers(HttpMethod.GET,
                                       String.format("%s/categories/**", apiPrefix)).permitAll()

                               .requestMatchers(HttpMethod.GET,
                                       String.format("%s/brands/**", apiPrefix)).permitAll()

                               .requestMatchers(HttpMethod.GET,
                                       String.format("%s/specifications/*", apiPrefix)).permitAll()

                               .requestMatchers(HttpMethod.GET,
                                        String.format("%s/products**", apiPrefix)).permitAll()
                               .requestMatchers(HttpMethod.GET,
                                       String.format("%s/products/*", apiPrefix)).permitAll()
                               .requestMatchers(HttpMethod.GET,
                                       String.format("%s/products/get-products**", apiPrefix)).permitAll()
                               .requestMatchers(HttpMethod.GET,
                                       String.format("%s/products/images/*", apiPrefix)).permitAll()

                               .requestMatchers(HttpMethod.GET,
                                       String.format("%s/banners**", apiPrefix)).permitAll()
                               .requestMatchers(HttpMethod.GET,
                                       String.format("%s/banners/*", apiPrefix)).permitAll()
                               .requestMatchers(HttpMethod.GET,
                                       String.format("%s/banners/images/*", apiPrefix)).permitAll()

                               .requestMatchers(GET,
                                       String.format("%s/orders/**", apiPrefix)).permitAll()

                               .requestMatchers(GET,
                                       String.format("%s/orders/user/**", apiPrefix)).permitAll()

                               .requestMatchers(GET,
                                       String.format("%s/feedbacks/product/**", apiPrefix)).permitAll()

                               .requestMatchers(GET,
                                       String.format("%s/actuator/**", apiPrefix)).permitAll()
                               .anyRequest()
                               .authenticated();


                })
                .csrf(AbstractHttpConfigurer::disable);
        http.securityMatcher(String.valueOf(EndpointRequest.toAnyEndpoint()));
        http.cors(new Customizer<CorsConfigurer<HttpSecurity>>() {
            @Override
            public void customize(CorsConfigurer<HttpSecurity> httpSecurityCorsConfigurer) {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(List.of("*"));
                configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
                configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
                configuration.setExposedHeaders(List.of("x-auth-token"));
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                httpSecurityCorsConfigurer.configurationSource(source);
            }
        });


        return http.build();
    }

}
