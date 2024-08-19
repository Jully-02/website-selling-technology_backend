package vn.jully.website_selling_technology_backend.services.specification;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import vn.jully.website_selling_technology_backend.dtos.SpecificationDTO;
import vn.jully.website_selling_technology_backend.entities.Product;
import vn.jully.website_selling_technology_backend.entities.Specification;
import vn.jully.website_selling_technology_backend.exceptions.DataNotFoundException;
import vn.jully.website_selling_technology_backend.repositories.ProductRepository;
import vn.jully.website_selling_technology_backend.repositories.SpecificationRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SpecificationService implements ISpecificationService{
    private final SpecificationRepository specificationRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    @Override
    @Transactional
    public Specification insertSpecification(SpecificationDTO specificationDTO) throws DataNotFoundException {
        Product existingProduct = productRepository.findById(specificationDTO.getProductId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find Product with ID = " + specificationDTO.getProductId()));
        Specification newSpecification = modelMapper.map(specificationDTO, Specification.class);
        newSpecification.setProduct(existingProduct);
        existingProduct.setSpecification(newSpecification);
        return specificationRepository.save(newSpecification);
    }

    @Override
    public Specification getSpecification (Long id) throws Exception {
        return specificationRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find Specification with ID = " + id));
    }

    @Override
    @Transactional
    public Specification updateSpecification(Long id, SpecificationDTO specificationDTO) throws Exception {
        Specification existingSpecification = getSpecification(id);
        Product existingProduct = productRepository.findById(specificationDTO.getProductId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find Product with ID = " + specificationDTO.getProductId()));

        if (existingSpecification.getProduct().getId() != specificationDTO.getProductId()) {
            Product product = existingSpecification.getProduct();
            product.setSpecification(null);
            productRepository.save(product);

            existingSpecification.setProduct(existingProduct);
            existingProduct.setSpecification(existingSpecification);
        }
        modelMapper.map(specificationDTO, existingSpecification);
        productRepository.save(existingProduct);
        return specificationRepository.save(existingSpecification);
    }

    @Override
    public List<Specification> getAllSpecifications() {
        return specificationRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteSpecification(Long id) {
        specificationRepository.deleteById(id);
    }
}
