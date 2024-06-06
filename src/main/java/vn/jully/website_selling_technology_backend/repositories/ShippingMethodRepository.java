package vn.jully.website_selling_technology_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.jully.website_selling_technology_backend.entities.ShippingMethod;

public interface ShippingMethodRepository extends JpaRepository<ShippingMethod, Long> {
}
