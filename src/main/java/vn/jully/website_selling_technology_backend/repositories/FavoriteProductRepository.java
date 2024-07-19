package vn.jully.website_selling_technology_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.jully.website_selling_technology_backend.entities.CartItem;
import vn.jully.website_selling_technology_backend.entities.FavoriteProduct;

import java.util.List;

public interface FavoriteProductRepository extends JpaRepository<FavoriteProduct, Long> {
    List<FavoriteProduct> findByUserId (Long userId);

    @Query("SELECT fp FROM FavoriteProduct fp WHERE fp.user.id = :userId AND fp.product.id = :productId")
    FavoriteProduct findByUserIdAndProductId (Long userId, Long productId);

    @Query("DELETE FROM FavoriteProduct fp WHERE fp.user.id = :userId AND fp.product.id = :productId")
    @Modifying
    void deleteByUserIdAndProductId(@Param("userId") Long userId, @Param("productId") Long productId);
}
