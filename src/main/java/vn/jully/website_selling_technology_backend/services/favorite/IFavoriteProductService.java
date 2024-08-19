package vn.jully.website_selling_technology_backend.services.favorite;

import vn.jully.website_selling_technology_backend.dtos.CartItemDTO;
import vn.jully.website_selling_technology_backend.dtos.FavoriteProductDTO;
import vn.jully.website_selling_technology_backend.entities.CartItem;
import vn.jully.website_selling_technology_backend.entities.FavoriteProduct;
import vn.jully.website_selling_technology_backend.exceptions.DataNotFoundException;

import java.util.List;

public interface IFavoriteProductService {
    FavoriteProduct insertFavoriteProduct (FavoriteProductDTO favoriteProductDTO) throws Exception;

    FavoriteProduct getFavoriteProductById (Long id) throws Exception;

    List<FavoriteProduct> getAllFavoriteProducts ();

    FavoriteProduct updateFavoriteProduct (Long id, FavoriteProductDTO favoriteProductDTO) throws Exception;

    void deleteFavoriteProduct (long id);

    List<FavoriteProduct> findByUserId (Long userId) throws Exception;

    void deleteByUserIdAndProductId (long userId, long productId);
}
