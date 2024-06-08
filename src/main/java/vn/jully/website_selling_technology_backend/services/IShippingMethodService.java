package vn.jully.website_selling_technology_backend.services;

import vn.jully.website_selling_technology_backend.dtos.ShippingMethodDTO;
import vn.jully.website_selling_technology_backend.entities.ShippingMethod;
import vn.jully.website_selling_technology_backend.exceptions.DataNotFoundException;

import java.util.List;

public interface IShippingMethodService {
    ShippingMethod insertShippingMethod (ShippingMethodDTO shippingMethodDTO);

    ShippingMethod updateShippingMethod (Long id, ShippingMethodDTO shippingMethodDTO) throws DataNotFoundException;

    ShippingMethod getShippingMethod (Long id) throws DataNotFoundException;

    List<ShippingMethod> getAllShippingMethod ();

    void deleteShippingMethod (Long id);
}
