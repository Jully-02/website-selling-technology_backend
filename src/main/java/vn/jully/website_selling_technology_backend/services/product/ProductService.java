package vn.jully.website_selling_technology_backend.services.product;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.jully.website_selling_technology_backend.dtos.ProductDTO;
import vn.jully.website_selling_technology_backend.dtos.ProductImageDTO;
import vn.jully.website_selling_technology_backend.entities.Brand;
import vn.jully.website_selling_technology_backend.entities.Category;
import vn.jully.website_selling_technology_backend.entities.Product;
import vn.jully.website_selling_technology_backend.entities.ProductImage;
import vn.jully.website_selling_technology_backend.exceptions.DataNotFoundException;
import vn.jully.website_selling_technology_backend.exceptions.InvalidParamException;
import vn.jully.website_selling_technology_backend.repositories.BrandRepository;
import vn.jully.website_selling_technology_backend.repositories.CategoryRepository;
import vn.jully.website_selling_technology_backend.repositories.ProductImageRepository;
import vn.jully.website_selling_technology_backend.repositories.ProductRepository;
import vn.jully.website_selling_technology_backend.responses.cloudinary.CloudinaryResponse;
import vn.jully.website_selling_technology_backend.responses.product.ProductResponse;
import vn.jully.website_selling_technology_backend.services.file_upload.IFileUploadService;
import vn.jully.website_selling_technology_backend.utils.FileUploadUtil;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final ProductImageRepository productImageRepository;
    private final IFileUploadService fileUploadService;
    @Override
    @Transactional
    public Product insertProduct(ProductDTO productDTO) throws DataNotFoundException {
        List<Category> categoryList = productDTO.getCategoryIds().stream()
                .map(categoryId -> {
                    try {
                        return categoryRepository.findById(categoryId)
                                .orElseThrow(() -> new DataNotFoundException("Cannot find category ID = " + categoryId));
                    } catch (DataNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();
        Brand existingBrand = brandRepository.findById(productDTO.getBrandId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find brand ID = " + productDTO.getBrandId()));
        Product newProduct = Product.builder()
                .title(productDTO.getTitle())
                .price(productDTO.getPrice())
                .description(productDTO.getDescription())
                .thumbnail(productDTO.getThumbnail())
                .discount(productDTO.getDiscount())
                .averageRate(productDTO.getAverageRate())
                .brand(existingBrand)
                .categoryList(categoryList)
                .build();

        return productRepository.save(newProduct);
    }

    @Override
    public Product getProductById(Long id) throws DataNotFoundException {
        return productRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find Product with ID = " + id));
    }

    @Override
    public Page<ProductResponse> searchProducts(
            List<Long> categoryIds,
            long categoryCount,
            List<Long> brandIds,
            String keyword,
            PageRequest pageRequest
    ) {
        Page<Product> productsPage;
        productsPage = productRepository.searchProducts(categoryIds, categoryCount,brandIds, keyword, pageRequest);

        return productsPage.map(ProductResponse::convertToProductResponse);
    }

    @Override
    @Transactional
    public Product updateProduct(Long id, ProductDTO productDTO) throws DataNotFoundException {
        Product existingProduct = getProductById(id);
        if (existingProduct != null) {
            List<Category> categoryList = productDTO.getCategoryIds().stream()
                    .map(categoryId -> {
                        try {
                            return categoryRepository.findById(categoryId)
                                    .orElseThrow(() -> new DataNotFoundException("Cannot find category ID = " + categoryId));
                        } catch (DataNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .toList();
            Brand existingBrand = brandRepository.findById(productDTO.getBrandId())
                    .orElseThrow(() -> new DataNotFoundException("Cannot find brand ID = " + productDTO.getBrandId()));

            existingProduct.setTitle(productDTO.getTitle());
            existingProduct.setPrice(productDTO.getPrice());
            existingProduct.setThumbnail(productDTO.getThumbnail());
            existingProduct.setDescription(productDTO.getDescription());
            existingProduct.setDiscount(productDTO.getDiscount());
            existingProduct.setAverageRate(productDTO.getAverageRate());
//            existingProduct.setBrand(existingBrand);
//            existingProduct.setCategoryList(categoryList);
            return productRepository.save(existingProduct);
        }
        return null;
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        optionalProduct.ifPresent(productRepository::delete);
    }

    @Override
    public boolean existsByTitle(String title) {
        return productRepository.existsByTitle(title);
    }

    @Override
    @Transactional
    public ProductImage insertProductImage (
            Long productId,
            ProductImageDTO productImageDTO
    ) throws DataNotFoundException, InvalidParamException {
        Product existingProduct = productRepository
                .findById(productImageDTO.getProductId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find Product with ID = " + productImageDTO.getProductId()));
        ProductImage newProductImage = ProductImage.builder()
                .product(existingProduct)
                .imageName(productImageDTO.getImageName())
                .imageUrl(productImageDTO.getImageUrl())
                .build();
        // Do not insert more than 5 images for 1 product
        int size = productImageRepository.findByProductId(productId).size();
        if (size >= ProductImage.MAXIMUM_IMAGES_PER_PRODUCT) {
            throw new InvalidParamException("Number of images must be <= " + ProductImage.MAXIMUM_IMAGES_PER_PRODUCT);
        }
        if (Objects.equals(existingProduct.getThumbnail(), "") || existingProduct.getThumbnail() == null) {
            existingProduct.setThumbnail(productImageDTO.getImageUrl());
        }
        productRepository.save(existingProduct);
        return productImageRepository.save(newProductImage);
    }

    public CloudinaryResponse uploadImage (MultipartFile file) throws Exception {
        FileUploadUtil.assertAllowed(file, FileUploadUtil.IMAGE_PATTERN);
        String fileName = FileUploadUtil.getFileName(file.getOriginalFilename());
        CloudinaryResponse response = fileUploadService.uploadFile(file, fileName);
        return response;
    }

    @Override
    public List<ProductResponse> getProductsByIds(List<Long> productIds) {
        List<Product> products = productRepository.getProductsByIds(productIds);
        return products.stream().map(ProductResponse::convertToProductResponse).toList();
    }

}
