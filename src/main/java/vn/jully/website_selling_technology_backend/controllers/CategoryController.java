package vn.jully.website_selling_technology_backend.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import vn.jully.website_selling_technology_backend.components.LocalizationUtils;
import vn.jully.website_selling_technology_backend.components.converters.CategoryMessageConverter;
import vn.jully.website_selling_technology_backend.dtos.CategoryDTO;
import vn.jully.website_selling_technology_backend.entities.Category;
import vn.jully.website_selling_technology_backend.responses.Response;
import vn.jully.website_selling_technology_backend.responses.categogy.CategoryListResponse;
import vn.jully.website_selling_technology_backend.responses.categogy.CategoryResponse;
import vn.jully.website_selling_technology_backend.services.category.ICategoryService;
import vn.jully.website_selling_technology_backend.utils.MessageKey;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/categories")
// Dependency Injection
@RequiredArgsConstructor
public class CategoryController {
    private final ICategoryService categoryService;
    private final LocalizationUtils localizationUtils;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> insertCategory(
            @Valid @RequestBody CategoryDTO categoryDTO,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(
                    Response.builder()
                            .message(localizationUtils.getLocalizedMessage(MessageKey.INVALID_ERROR, errorMessages.toString()))
                            .status(HttpStatus.BAD_REQUEST)
                            .build()
            );
        }
        Category category = categoryService.insertCategory(categoryDTO);
        this.kafkaTemplate.send("insert-a-category", category); // producer
        this.kafkaTemplate.setMessageConverter(new CategoryMessageConverter());
        return ResponseEntity.ok(
                Response.builder()
                        .data(category)
                        .status(HttpStatus.CREATED)
                        .message(localizationUtils.getLocalizedMessage(MessageKey.INSERT_SUCCESSFULLY))
                        .build()
        );
    }

    @GetMapping("")
    public ResponseEntity<Response> getAllCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        PageRequest pageRequest = PageRequest.of(
                page, limit,
//                Sort.by("createdAt").descending());
                Sort.by("id").ascending());
        Page<CategoryResponse> categoryPages = categoryService.getAllCategories(pageRequest);

        int totalPages = categoryPages.getTotalPages();
        List<CategoryResponse> categoryResponses = categoryPages.getContent();
        this.kafkaTemplate.send("get-all-categories", categoryResponses);
        return ResponseEntity.ok(
                Response.builder()
                        .message("Get all categories successfully")
                        .status(HttpStatus.OK)
                        .data(CategoryListResponse
                                .builder()
                                .categoryResponses(categoryResponses)
                                .totalPages(totalPages)
                                .build())
                        .build()
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> updateCategory(
            @PathVariable Long id,
            @RequestBody CategoryDTO categoryDTO
    ) {
        Category category = categoryService.updateCategory(id, categoryDTO);
        return ResponseEntity.ok(
                Response.builder()
                        .data(category)
                        .message(localizationUtils.getLocalizedMessage(MessageKey.UPDATE_SUCCESSFULLY))
                        .status(HttpStatus.OK)
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> deleteCategory(@PathVariable long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(
                Response.builder()
                        .message(localizationUtils.getLocalizedMessage(MessageKey.DELETE_SUCCESSFULLY))
                        .status(HttpStatus.OK)
                        .build()
        );
    }
}
