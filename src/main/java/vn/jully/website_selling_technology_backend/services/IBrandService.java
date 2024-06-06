package vn.jully.website_selling_technology_backend.services;

import vn.jully.website_selling_technology_backend.dtos.BrandDTO;
import vn.jully.website_selling_technology_backend.entities.Brand;
import vn.jully.website_selling_technology_backend.exceptions.DataNotFoundException;

import java.util.List;

public interface IBrandService {
    Brand insertBrand (BrandDTO brandDTO);

    Brand getBrandById (Long id) throws DataNotFoundException;

    List<Brand> getAllBrands ();

    Brand updateBrand (Long id, BrandDTO brandDTO) throws DataNotFoundException;

    void deleteBrand (Long id);
}
