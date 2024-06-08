package vn.jully.website_selling_technology_backend.services;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import vn.jully.website_selling_technology_backend.dtos.ShippingMethodDTO;
import vn.jully.website_selling_technology_backend.entities.ShippingMethod;
import vn.jully.website_selling_technology_backend.exceptions.DataNotFoundException;
import vn.jully.website_selling_technology_backend.repositories.ShippingMethodRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShippingMethodService implements IShippingMethodService{
    private final ShippingMethodRepository shippingMethodRepository;
    private final ModelMapper modelMapper;
    @Override
    public ShippingMethod insertShippingMethod(ShippingMethodDTO shippingMethodDTO) {
        ShippingMethod shippingMethod = modelMapper.map(shippingMethodDTO, ShippingMethod.class);
        return shippingMethodRepository.save(shippingMethod);
    }

    @Override
    public ShippingMethod updateShippingMethod(Long id, ShippingMethodDTO shippingMethodDTO) throws DataNotFoundException {
        ShippingMethod existingShippingMethod = getShippingMethod(id);
        modelMapper.map(shippingMethodDTO, existingShippingMethod);
        shippingMethodRepository.save(existingShippingMethod);
        return existingShippingMethod;
    }

    @Override
    public ShippingMethod getShippingMethod(Long id) throws DataNotFoundException {
        return shippingMethodRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find Shipping method with ID = " + id));
    }

    @Override
    public List<ShippingMethod> getAllShippingMethod() {
        return shippingMethodRepository.findAll();
    }

    @Override
    public void deleteShippingMethod(Long id) {
        shippingMethodRepository.deleteById(id);
    }
}
