package vn.jully.website_selling_technology_backend.services.banner;

import vn.jully.website_selling_technology_backend.dtos.BannerDTO;
import vn.jully.website_selling_technology_backend.entities.Banner;

import java.util.List;

public interface IBannerService {
    Banner insertBanner (BannerDTO bannerDTO) throws Exception;

    Banner getBannerById (Long id) throws Exception;

    Banner updateBanner (Long id, BannerDTO bannerDTO) throws Exception;

    void deleteBanner (Long id) throws Exception;

    Banner insertBannerImage (
            Banner banner
    ) throws Exception;

    List<Banner> getAllBanners ();
}
