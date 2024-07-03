package vn.jully.website_selling_technology_backend.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.jully.website_selling_technology_backend.entities.CartItem;
import vn.jully.website_selling_technology_backend.responses.CategoryResponse;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUserId (Long userId);

    @Query("SELECT ci FROM CartItem ci WHERE ci.user.id = :userId AND ci.product.id = :productId")
    CartItem findByUserIdAndProductId (Long userId, Long productId);
}
