package vn.jully.website_selling_technology_backend.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import vn.jully.website_selling_technology_backend.dtos.ProductDTO;
import vn.jully.website_selling_technology_backend.dtos.ProductImageDTO;
import vn.jully.website_selling_technology_backend.entities.Product;
import vn.jully.website_selling_technology_backend.entities.ProductImage;
import vn.jully.website_selling_technology_backend.exceptions.DataNotFoundException;
import vn.jully.website_selling_technology_backend.exceptions.InvalidParamException;
import vn.jully.website_selling_technology_backend.responses.ProductResponse;

public interface IProductService{
    Product insertProduct (ProductDTO productDTO) throws DataNotFoundException;

    Product getProductById (Long id) throws DataNotFoundException;

    Page<ProductResponse> getAllProducts (PageRequest pageRequest);

    Product updateProduct (Long id, ProductDTO productDTO) throws DataNotFoundException;

    void deleteProduct (Long id) throws DataNotFoundException;

    boolean existsByTitle (String title);
    ProductImage insertProductImage (
            Long productId,
            ProductImageDTO productImageDTO
    ) throws DataNotFoundException, InvalidParamException;

    ProductResponse convertToProductResponse (Product product);
}