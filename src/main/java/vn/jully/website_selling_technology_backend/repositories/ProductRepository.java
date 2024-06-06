package vn.jully.website_selling_technology_backend.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.jully.website_selling_technology_backend.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByTitle (String title);

    Page<Product> findAll (Pageable pageable);
}
