package vn.jully.website_selling_technology_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.jully.website_selling_technology_backend.entities.FavoriteProduct;

import java.util.List;

public interface FavoriteProductRepository extends JpaRepository<FavoriteProduct, Long> {
    List<FavoriteProduct> findByUserId (Long userId);
}
