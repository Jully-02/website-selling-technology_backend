package vn.jully.website_selling_technology_backend.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.jully.website_selling_technology_backend.entities.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByTitle (String title);

//    Page<Product> getAllProduct (Pageable pageable);

    @Query("SELECT p FROM Product p " +
            "LEFT JOIN p.categoryList c " +
            "LEFT JOIN p.brand b " +
            "WHERE (:categoryIds IS NULL OR c.id IN :categoryIds) " +
            "AND (:brandIds IS NULL OR b.id IN :brandIds) " +
            "AND (:keyword IS NULL OR :keyword = '' OR p.title LIKE %:keyword% OR p.description LIKE %:keyword%) " +
            "GROUP BY p.id " +
            "HAVING (:categoryIds IS NULL OR COUNT(DISTINCT c.id) = :categoryCount)")
    Page<Product> searchProducts(
            @Param("categoryIds") List<Long> categoryIds,
            @Param("categoryCount") long categoryCount,
            @Param("brandIds") List<Long> brandIds,
            @Param("keyword") String keyword,
            Pageable pageable);


}
