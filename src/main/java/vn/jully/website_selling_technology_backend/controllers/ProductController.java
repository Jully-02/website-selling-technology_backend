package vn.jully.website_selling_technology_backend.controllers;

import com.github.javafaker.Faker;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.jully.website_selling_technology_backend.dtos.ProductDTO;
import vn.jully.website_selling_technology_backend.dtos.ProductImageDTO;
import vn.jully.website_selling_technology_backend.entities.Product;
import vn.jully.website_selling_technology_backend.entities.ProductImage;
import vn.jully.website_selling_technology_backend.exceptions.DataNotFoundException;
import vn.jully.website_selling_technology_backend.responses.ProductListResponse;
import vn.jully.website_selling_technology_backend.responses.ProductResponse;
import vn.jully.website_selling_technology_backend.services.IProductService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
public class ProductController {
    private final IProductService productService;
    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> insertProduct(
            @Valid @RequestBody ProductDTO productDTO,
            BindingResult result
    ) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            Product newProduct = productService.insertProduct(productDTO);
            return ResponseEntity.ok(newProduct);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "uploads/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> uploadImages (
            @PathVariable("id") Long id,
            @ModelAttribute("files") List<MultipartFile> files
    ) {
        try {
            Product existingProduct = productService.getProductById(id);

            files = files == null ? new ArrayList<MultipartFile>() : files;
            if (files.size() > ProductImage.MAXIMUM_IMAGES_PER_PRODUCT) {
                return ResponseEntity.badRequest().body("You can only upload maximum " + ProductImage.MAXIMUM_IMAGES_PER_PRODUCT + " images");
            }
            List<ProductImage> productImages = new ArrayList<>();
            for (MultipartFile file : files) {
                if (file.getSize() == 0) {
                    continue;
                }
                // Check file size and format
                if (file.getSize() > 10 * 1024 * 1024) { // Size > 10 MB
                    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                            .body("File is too large! Maximum size is 10MB");
                }
                String contentType = file.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                            .body("File must be an image");
                }
                // Save file and update thumbnail in DTO
                String fileName = storeFile(file);
                String name = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
                ProductImage productImage = productService.insertProductImage(
                        existingProduct.getId(),
                        ProductImageDTO.builder()
                                .productId(existingProduct.getId())
                                .imageUrl(fileName)
                                .imageName(name)
                                .build()
                );
                productImages.add(productImage);
            }
            return ResponseEntity.ok(productImages);
        }  catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private String storeFile(MultipartFile file) throws IOException {
        if (!isImageFile(file) || file.getOriginalFilename() == null) {
            throw new IOException("Invalid image file format");
        }
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        // Add UUID before the file name to ensure the file name is unique
        String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;
        // Path to the folder where you want to save the file
        Path uploadDir = Paths.get("uploads");
        // Check and create the directory if it doesn't exist yet
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        // Full path to file
        Path destination = Paths.get(uploadDir.toString(), uniqueFileName);
        // Copy files to destination directory
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFileName;
    }

    private boolean isImageFile (MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }

    @GetMapping("/images/{imageName}")
    public ResponseEntity<?> viewImage (@PathVariable String imageName) {
        try {
            Path imagePath = Paths.get("uploads/" + imageName);
            UrlResource resource = new UrlResource(imagePath.toUri());

            if (resource.exists()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(resource);
            } else {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(new UrlResource(Paths.get("uploads/notfound.png").toUri()));
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("")
    public ResponseEntity<ProductListResponse> getAllProducts(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "", name = "category-ids") String categoryIds,
            @RequestParam(defaultValue = "", name = "brand-ids") String brandIds,
            @RequestParam(defaultValue = "default", name ="sort") String sortOption,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "16") int limit
    ) {
        Sort sort = switch (sortOption) {
            case "popularity" -> Sort.by("id").descending();
            case "latest" -> Sort.by("createdAt").descending();
            case "high" -> Sort.by("price").descending();
            case "low" -> Sort.by("price").ascending();
            default -> Sort.by("id").ascending(); // Default sorting
        };
        // Create Pageable from page and limit information
        PageRequest pageRequest = PageRequest.of(
                page, limit,
//                Sort.by("createdAt").descending());
                sort);
        Page<ProductResponse> productPage;
        List<Long> brandList = null;
        if (!brandIds.equals("")) {
            brandList = Arrays.stream(brandIds.split(","))
                    .map(Long::parseLong)
                    .toList();
            if (brandList.isEmpty()) {
                brandList = null;
            }
        }

        List<Long> categoryList = null;
        if (!categoryIds.equals("")) {
            categoryList = Arrays.stream(categoryIds.split(","))
                    .map(Long::parseLong)
                    .toList();
            if (categoryList.isEmpty()) {
                categoryList = null;
            }
        }
        productPage = productService.searchProducts(categoryList, categoryList == null ? 0 : categoryList.size(), brandList, keyword, pageRequest);
        // Get total pages
        int totalPages = productPage.getTotalPages();
        List<ProductResponse> productResponses = productPage.getContent();
        return ResponseEntity.ok(ProductListResponse.builder()
                        .productResponses(productResponses)
                        .totalPages(totalPages)
                        .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable("id") Long productId) throws DataNotFoundException {
        Product product = productService.getProductById(productId);
        return ResponseEntity.ok(ProductResponse.convertToProductResponse(product));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return  ResponseEntity.ok("Deleted product successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateProduct (
            @PathVariable("id") Long id,
            @Valid @RequestBody ProductDTO productDTO,
            BindingResult result
    ) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            Product updatedProduct = productService.updateProduct(id, productDTO);
            return ResponseEntity.ok(updatedProduct);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

//    @PostMapping("/generateFakeProducts")
    private ResponseEntity<String> generateFakeProducts () {
        Faker faker = new Faker();
        for (long i = 0; i < 1_000_000; i++) {
            String title = faker.commerce().productName();
            if (productService.existsByTitle(title)) {
                continue;
            }
            List<Long> categoryIds = new ArrayList<>();
            for (int j = 0; j < faker.number().numberBetween(1,5); j++) {
                categoryIds.add((long) faker.number().numberBetween(1, 29));
            }
            ProductDTO productDTO = ProductDTO
                    .builder()
                    .title(title)
                    .price(faker.number().numberBetween(10, 90_000_000))
                    .description(faker.lorem().sentence())
                    .discount(faker.number().numberBetween(0, 10))
                    .averageRate(faker.number().numberBetween(0, 5))
                    .brandId((long)faker.number().numberBetween(1,5))
                    .categoryIds(categoryIds)
                    .thumbnail("")
                    .build();
            try {
                productService.insertProduct(productDTO);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }
        return ResponseEntity.ok("Fake Products insert successfully");
    }
}
