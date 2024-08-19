package vn.jully.website_selling_technology_backend.services.category;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import vn.jully.website_selling_technology_backend.dtos.CategoryDTO;
import vn.jully.website_selling_technology_backend.entities.Category;
import vn.jully.website_selling_technology_backend.responses.categogy.CategoryResponse;

public interface ICategoryService {
    Category insertCategory (CategoryDTO categoryDTO);

    Category getCategoryById (Long id);

    Category updateCategory (Long id, CategoryDTO categoryDTO);

    void deleteCategory (Long id);

    Page<CategoryResponse> getAllCategories (PageRequest pageRequest);
}
