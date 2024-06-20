package vn.jully.website_selling_technology_backend.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.jully.website_selling_technology_backend.entities.Brand;
import vn.jully.website_selling_technology_backend.responses.BrandResponse;

public interface BrandRepository extends JpaRepository<Brand, Long> {

    @Query("SELECT new vn.jully.website_selling_technology_backend.responses.BrandResponse(b.id, b.name, COUNT(p.id)) " +
            "FROM Brand b " +
            "LEFT JOIN b.productList p " +
            "GROUP BY b.id, b.name")
    Page<BrandResponse> getAllBrands(Pageable pageable);
}
