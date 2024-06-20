package vn.jully.website_selling_technology_backend.services;

import vn.jully.website_selling_technology_backend.dtos.SpecificationDTO;
import vn.jully.website_selling_technology_backend.entities.Specification;
import vn.jully.website_selling_technology_backend.exceptions.DataNotFoundException;

import java.util.List;

public interface ISpecificationService {
    Specification insertSpecification (SpecificationDTO specificationDTO) throws DataNotFoundException;

    Specification updateSpecification (Long id, SpecificationDTO specificationDTO) throws Exception;

    List<Specification> getAllSpecifications ();

    Specification getSpecification (Long id) throws Exception;

    void deleteSpecification (Long id);
}
