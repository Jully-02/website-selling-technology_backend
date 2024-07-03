package vn.jully.website_selling_technology_backend.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.jully.website_selling_technology_backend.components.LocalizationUtils;
import vn.jully.website_selling_technology_backend.dtos.BannerDTO;
import vn.jully.website_selling_technology_backend.entities.Banner;
import vn.jully.website_selling_technology_backend.responses.BannerResponse;
import vn.jully.website_selling_technology_backend.services.BannerService;
import vn.jully.website_selling_technology_backend.services.IBannerService;
import vn.jully.website_selling_technology_backend.utils.MessageKey;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/banners")
@RequiredArgsConstructor
public class BannerController {
    private final IBannerService bannerService;
    private final LocalizationUtils localizationUtils;

    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BannerResponse> insertBanner (
            @Valid @RequestBody BannerDTO bannerDTO,
            BindingResult result
    ) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(
                        BannerResponse
                                .builder()
                                .message(localizationUtils.getLocalizedMessage(MessageKey.INVALID_ERROR,errorMessages.toString()))
                                .build()
                );
            }
            Banner banner = bannerService.insertBanner(bannerDTO);
            return ResponseEntity.ok(
                    BannerResponse
                            .builder()
                            .message(localizationUtils.getLocalizedMessage(MessageKey.INSERT_SUCCESSFULLY))
                            .banner(banner)
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    BannerResponse
                            .builder()
                            .message(localizationUtils.getLocalizedMessage(MessageKey.INSERT_FAILED, e.getMessage()))
                            .build()
            );
        }
    }

    @PostMapping(value = "uploads/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> uploadImages (
            @PathVariable("id") Long id,
            @ModelAttribute("file") MultipartFile file
    ) throws Exception {
        Banner existingBanner = bannerService.getBannerById(id);

        if (file.getSize() > 10 * 1024 * 1024) {
            return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                    .body("File is too large! Maximum size is 10MB");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                    .body("File must be an image");
        }

        String fileName = storeFile(file);
        existingBanner.setThumbnail(fileName);
        return ResponseEntity.ok(bannerService.insertBannerImage(existingBanner));
    }

    private String storeFile(MultipartFile file) throws IOException {
        if (!isImageFile(file) || file.getOriginalFilename() == null) {
            throw new IOException("Invalid image file format");
        }
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        // Add UUID before the file name to ensure the file name is unique
        String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;
        // Path to the folder where you want to save the file
        Path uploadDir = Paths.get("uploads/banners");
        // Check and create the directory if it doesn't exist yet
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        // Full path to file
        Path destination = Paths.get(uploadDir.toString(), uniqueFileName);
        // Copy files to destination directory
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFileName;
    }

    private boolean isImageFile (MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }

    @GetMapping("/images/{imageName}")
    public ResponseEntity<?> viewImage (@PathVariable String imageName) {
        try {
            Path imagePath = Paths.get("uploads/banners/" + imageName);
            UrlResource resource = new UrlResource(imagePath.toUri());

            if (resource.exists()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(resource);
            } else {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(new UrlResource(Paths.get("uploads/banners/notfound.png").toUri()));
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("")
    public ResponseEntity<List<Banner>> getAllBanners () {
        return ResponseEntity.ok(bannerService.getAllBanners());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteBanner (@PathVariable Long id) {
        try {
            bannerService.deleteBanner(id);
            return ResponseEntity.ok("Deleted banner successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBannerById (@PathVariable("id") Long id) throws Exception {
        Banner banner = bannerService.getBannerById(id);
        return ResponseEntity.ok(banner);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateBanner (
            @PathVariable("id") Long id,
            @Valid @RequestBody BannerDTO bannerDTO,
            BindingResult result
    ) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            Banner updatedBanner = bannerService.updateBanner(id, bannerDTO);
            return ResponseEntity.ok(updatedBanner);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
