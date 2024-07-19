package vn.jully.website_selling_technology_backend.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import vn.jully.website_selling_technology_backend.dtos.FavoriteProductDTO;
import vn.jully.website_selling_technology_backend.entities.FavoriteProduct;
import vn.jully.website_selling_technology_backend.entities.Product;
import vn.jully.website_selling_technology_backend.entities.User;
import vn.jully.website_selling_technology_backend.exceptions.DataNotFoundException;
import vn.jully.website_selling_technology_backend.repositories.FavoriteProductRepository;
import vn.jully.website_selling_technology_backend.repositories.ProductRepository;
import vn.jully.website_selling_technology_backend.repositories.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteProductService implements IFavoriteProductService{
    private final FavoriteProductRepository favoriteProductRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    @Override
    public FavoriteProduct insertFavoriteProduct(FavoriteProductDTO favoriteProductDTO) throws Exception {
        User existingUser = userRepository.findById(favoriteProductDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find User with ID = " + favoriteProductDTO.getUserId()));
        Product exisitingProduct = productRepository.findById(favoriteProductDTO.getProductId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find Product with ID = " + favoriteProductDTO.getProductId()));
        FavoriteProduct exisitingFavoriteProduct = favoriteProductRepository.findByUserIdAndProductId(favoriteProductDTO.getUserId(), favoriteProductDTO.getProductId());
        if (exisitingFavoriteProduct != null) {
            return null;
        }
        FavoriteProduct favoriteProduct = modelMapper.map(favoriteProductDTO, FavoriteProduct.class);
        favoriteProduct.setUser(existingUser);
        favoriteProduct.setProduct(exisitingProduct);
        return favoriteProductRepository.save(favoriteProduct);
    }

    @Override
    public FavoriteProduct getFavoriteProductById(Long id) throws Exception {
        return favoriteProductRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find Favorite Product with ID = " + id));
    }

    @Override
    public List<FavoriteProduct> getAllFavoriteProducts() {
        return favoriteProductRepository.findAll();
    }

    @Override
    public FavoriteProduct updateFavoriteProduct(Long id, FavoriteProductDTO favoriteProductDTO) throws Exception {
        FavoriteProduct existingFavorite = getFavoriteProductById(id);
        User existingUser = userRepository.findById(favoriteProductDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find User with ID = " + favoriteProductDTO.getUserId()));
        Product existingProduct=  productRepository.findById(favoriteProductDTO.getProductId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find Product with ID = " + favoriteProductDTO.getProductId()));

        existingFavorite.setProduct(existingProduct);
        existingFavorite.setUser(existingUser);

        return favoriteProductRepository.save(existingFavorite);
    }

    @Override
    @Transactional
    public void deleteFavoriteProduct(long id) {
        favoriteProductRepository.deleteById(id);
    }

    @Override
    public List<FavoriteProduct> findByUserId(Long userId) throws Exception {
        return favoriteProductRepository.findByUserId(userId);
    }

    @Override
    @Transactional
    public void deleteByUserIdAndProductId(long userId, long productId) {
        favoriteProductRepository.deleteByUserIdAndProductId(userId, productId);
    }
}
