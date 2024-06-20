package vn.jully.website_selling_technology_backend.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.jully.website_selling_technology_backend.entities.Category;
import vn.jully.website_selling_technology_backend.entities.Product;
import vn.jully.website_selling_technology_backend.entities.ProductImage;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse extends BaseResponse{

    private long id;

    private String title;

    private float price;

    private String description;

    private String thumbnail;

    private float discount;

    @JsonProperty("average_rate")
    private float averageRate;

    @JsonProperty("brand_id")

    private Long brandId;

    @JsonProperty("categories")
//    private List<Long> categoryIds;
    private List<Category> categories;

    @JsonProperty("specification_id")
    private Long specificationId;

    @JsonProperty("product_images")
    private List<ProductImage> productImages;

    public static ProductResponse convertToProductResponse(Product product) {
//        List<Long> categoryIds = product.getCategoryList().stream()
//                .map(Category::getId)
//                .toList();
        ProductResponse productResponse = ProductResponse.builder()
                .id(product.getId())
                .title(product.getTitle())
                .price(product.getPrice())
                .discount(product.getDiscount())
                .description(product.getDescription())
                .thumbnail(product.getThumbnail())
                .averageRate(product.getAverageRate())
                .brandId(product.getBrand().getId())
                .productImages(product.getProductImageList())
//                .specificationId(product.getSpecification().getId())
                .categories(product.getCategoryList())
                .build();
        productResponse.setCreatedAt(product.getCreatedAt());
        productResponse.setUpdatedAt(product.getUpdatedAt());
        return productResponse;
    }
}
