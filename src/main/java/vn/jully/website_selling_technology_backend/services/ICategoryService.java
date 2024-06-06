package vn.jully.website_selling_technology_backend.services;

import vn.jully.website_selling_technology_backend.dtos.CategoryDTO;
import vn.jully.website_selling_technology_backend.entities.Category;

import java.util.List;

public interface ICategoryService {
    Category insertCategory (CategoryDTO categoryDTO);

    Category getCategoryById (Long id);

    List<Category> getAllCategories ();

    Category updateCategory (Long id, CategoryDTO categoryDTO);

    void deleteCategory (Long id);
}
