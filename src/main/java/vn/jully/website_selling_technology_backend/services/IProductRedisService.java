package vn.jully.website_selling_technology_backend.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import vn.jully.website_selling_technology_backend.responses.ProductResponse;

import java.util.List;

public interface IProductRedisService {
    // Clear cached data in Redis
    void  clear();
    Page<ProductResponse> getAllProducts (
            String categoryIds,
            String brandIds,
            String keyword,
            PageRequest pageRequest) throws JsonProcessingException;

    void saveAllProducts(List<ProductResponse> productResponses,
                         String categoryIds,
                         String brandIds,
                         String keyword,
                         PageRequest pageRequest) throws JsonProcessingException;
}
