package vn.jully.website_selling_technology_backend.services;

import vn.jully.website_selling_technology_backend.dtos.CartItemDTO;
import vn.jully.website_selling_technology_backend.entities.CartItem;
import vn.jully.website_selling_technology_backend.exceptions.DataNotFoundException;

import java.util.List;

public interface ICartItemService {
    CartItem insertCartItem (CartItemDTO cartItemDTO) throws DataNotFoundException;

    CartItem getCartItemById (Long id) throws Exception;

    List<CartItem> getAllCartItems ();

    CartItem updateCartItem (Long id, CartItemDTO cartItemDTO) throws Exception;

    void deleteCartItem (long id);

    List<CartItem> findByUserId (Long userId) throws Exception;

    void deleteCartItemByUserIdAndProductId (long userId, long productId);

    void deleteCartItemByUserId (long userId);
}
