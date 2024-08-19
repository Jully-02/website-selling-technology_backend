package vn.jully.website_selling_technology_backend.services.cart_item;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import vn.jully.website_selling_technology_backend.dtos.CartItemDTO;
import vn.jully.website_selling_technology_backend.entities.CartItem;
import vn.jully.website_selling_technology_backend.entities.Product;
import vn.jully.website_selling_technology_backend.entities.User;
import vn.jully.website_selling_technology_backend.exceptions.DataNotFoundException;
import vn.jully.website_selling_technology_backend.repositories.CartItemRepository;
import vn.jully.website_selling_technology_backend.repositories.ProductRepository;
import vn.jully.website_selling_technology_backend.repositories.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartItemService implements ICartItemService {
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    @Override
    @Transactional
    public CartItem insertCartItem(CartItemDTO cartItemDTO) throws DataNotFoundException {
        User existingUser = userRepository.findById(cartItemDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find User with ID = " + cartItemDTO.getUserId()));

        Product existinngProduct = productRepository.findById(cartItemDTO.getProductId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find Product with ID = " + cartItemDTO.getProductId()));

        CartItem exsitingCartItem = cartItemRepository.findByUserIdAndProductId(cartItemDTO.getUserId(), cartItemDTO.getProductId());
        if (exsitingCartItem != null) {
            exsitingCartItem.setQuantity(exsitingCartItem.getQuantity() + cartItemDTO.getQuantity());
            return cartItemRepository.save(exsitingCartItem);
        }
        CartItem newCartItem = modelMapper.map(cartItemDTO, CartItem.class);
        newCartItem.setUser(existingUser);
        newCartItem.setProduct(existinngProduct);

        return cartItemRepository.save(newCartItem);
    }

    @Override
    public CartItem getCartItemById(Long id) throws Exception {
        return cartItemRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find Cart Item with ID = " + id));
    }

    @Override
    public List<CartItem> getAllCartItems() {
        return cartItemRepository.findAll();
    }

    @Override
    public CartItem updateCartItem(Long id, CartItemDTO cartItemDTO) throws Exception {
        CartItem existingCartItem = getCartItemById(id);

        User existingUser = userRepository.findById(cartItemDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find User with ID = " + cartItemDTO.getUserId()));

        Product existingProduct = productRepository.findById(cartItemDTO.getProductId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find Product with ID = " + cartItemDTO.getProductId()));

//        modelMapper.map(cartItemDTO, existingCartItem);
        existingCartItem.setQuantity(cartItemDTO.getQuantity());
        existingCartItem.setUser(existingUser);
        existingCartItem.setProduct(existingProduct);

        return cartItemRepository.save(existingCartItem);
    }

    @Override
    public void deleteCartItem(long id) {
        cartItemRepository.deleteById(id);
    }

    @Override
    public List<CartItem> findByUserId(Long userId) throws Exception {
        return cartItemRepository.findByUserId(userId);
    }

    @Override
    @Transactional
    public void deleteCartItemByUserIdAndProductId(long userId, long productId) {
        cartItemRepository.deleteByUserIdAndProductId(userId, productId);
    }

    @Override
    @Transactional
    public void deleteCartItemByUserId(long userId) {
        cartItemRepository.deleteCartItemByUserId(userId);
    }
}
