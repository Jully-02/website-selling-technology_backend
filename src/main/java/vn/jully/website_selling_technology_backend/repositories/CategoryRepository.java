package vn.jully.website_selling_technology_backend.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.jully.website_selling_technology_backend.entities.Category;
import vn.jully.website_selling_technology_backend.responses.CategoryResponse;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT new vn.jully.website_selling_technology_backend.responses.CategoryResponse(c.id, c.name, COUNT(p)) " +
            "FROM Category c " +
            "LEFT JOIN c.productList p " +
            "GROUP BY c.id, c.name")
    Page<CategoryResponse> getAllCategories (Pageable pageable);
}
