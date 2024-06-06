package vn.jully.website_selling_technology_backend.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.jully.website_selling_technology_backend.dtos.BrandDTO;
import vn.jully.website_selling_technology_backend.entities.Brand;
import vn.jully.website_selling_technology_backend.exceptions.DataNotFoundException;
import vn.jully.website_selling_technology_backend.repositories.BrandRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BrandService implements IBrandService{
    private final BrandRepository brandRepository;
    @Override
    public Brand insertBrand(BrandDTO brandDTO) {
        Brand newBrand = Brand.builder()
                .name(brandDTO.getName())
                .build();
        return brandRepository.save(newBrand);
    }

    @Override
    public Brand getBrandById(Long id) throws DataNotFoundException {
        return brandRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Brand not found"));
    }

    @Override
    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }

    @Override
    public Brand updateBrand(Long id, BrandDTO brandDTO) throws DataNotFoundException {
        Brand existingBrand = getBrandById(id);
        existingBrand.setName(brandDTO.getName());
        return brandRepository.save(existingBrand);
    }

    @Override
    public void deleteBrand(Long id) {
        brandRepository.deleteById(id);
    }
}
