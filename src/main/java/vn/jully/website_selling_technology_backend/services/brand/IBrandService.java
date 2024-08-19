package vn.jully.website_selling_technology_backend.services.brand;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import vn.jully.website_selling_technology_backend.dtos.BrandDTO;
import vn.jully.website_selling_technology_backend.entities.Brand;
import vn.jully.website_selling_technology_backend.exceptions.DataNotFoundException;
import vn.jully.website_selling_technology_backend.responses.brand.BrandResponse;

public interface IBrandService {
    Brand insertBrand (BrandDTO brandDTO);

    Brand getBrandById (Long id) throws DataNotFoundException;

    public Page<BrandResponse> getAllBrands(PageRequest pageRequest);

    Brand updateBrand (Long id, BrandDTO brandDTO) throws DataNotFoundException;

    void deleteBrand (Long id);
}
