package vn.jully.website_selling_technology_backend.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import vn.jully.website_selling_technology_backend.responses.ProductResponse;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductRedisService implements IProductRedisService{
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper redisObjectMapper;

    private String getKeyFrom (String categoryIds,
                               String brandIds,
                               String keyword,
                               PageRequest pageRequest) {
        int pageNumber = pageRequest.getPageNumber();
        int pageSize = pageRequest.getPageSize();
        Sort sort = pageRequest.getSort();
        String sortDirection = sort.getOrderFor("id")
                .getDirection() == Sort.Direction.ASC ? "asc" : "desc";
        String key = String.format("all_products:%s:%s:%d:%d:%s:%s", categoryIds, brandIds, pageNumber, pageSize, keyword, sortDirection);
        return key;
        /*
        {
            "all_products:1:10:asc": "list of products object"
        }
         */
    }
    @Override
    public void clear() {
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }

    @Override
    public Page<ProductResponse> getAllProducts(String categoryIds, String brandIds, String keyword, PageRequest pageRequest) throws JsonProcessingException {
        String key = this.getKeyFrom(categoryIds, brandIds, keyword, pageRequest);
        String json = (String) redisTemplate.opsForValue().get(key);
        List<ProductResponse>  productResponses =
                json != null ?
                        redisObjectMapper.readValue(json, new TypeReference<List<ProductResponse>>() {})
                        : null;

        return productResponses == null ? null : new PageImpl<>(productResponses, pageRequest, productResponses.size());
    }

    @Override
    public void saveAllProducts(List<ProductResponse> productResponses, String categoryIds, String brandIds, String keyword, PageRequest pageRequest) throws JsonProcessingException {
        String key = this.getKeyFrom(categoryIds, brandIds, keyword, pageRequest);
        String json = redisObjectMapper.writeValueAsString(productResponses);
        redisTemplate.opsForValue().set(key, json);
    }
}
