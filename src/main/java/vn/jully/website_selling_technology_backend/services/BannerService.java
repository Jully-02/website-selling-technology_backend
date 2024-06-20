package vn.jully.website_selling_technology_backend.services;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import vn.jully.website_selling_technology_backend.dtos.BannerDTO;
import vn.jully.website_selling_technology_backend.entities.Banner;
import vn.jully.website_selling_technology_backend.exceptions.DataNotFoundException;
import vn.jully.website_selling_technology_backend.repositories.BannerRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BannerService implements IBannerService{
    private final BannerRepository bannerRepository;
    private final ModelMapper modelMapper;

    @Override
    public Banner insertBanner(BannerDTO bannerDTO) throws Exception {
        Banner newBanner = modelMapper.map(bannerDTO, Banner.class);
        return bannerRepository.save(newBanner);
    }

    @Override
    public Banner getBannerById(Long id) throws Exception {
        return bannerRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find Banner with ID = " + id));
    }

    @Override
    public Banner updateBanner(Long id, BannerDTO bannerDTO) throws Exception {
        Banner existingBanner = getBannerById(id);
        modelMapper.map(bannerDTO, existingBanner);
        return bannerRepository.save(existingBanner);
    }

    @Override
    public void deleteBanner(Long id) throws Exception {
        bannerRepository.deleteById(id);
    }

    @Override
    public Banner insertBannerImage(Banner banner) throws Exception {
        return bannerRepository.save(banner);
    }

    @Override
    public List<Banner> getAllBanners() {
        return bannerRepository.findAll();
    }
}
