package vn.jully.website_selling_technology_backend.services;

import com.cloudinary.Cloudinary;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.jully.website_selling_technology_backend.responses.CloudinaryResponse;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class FileUploadService implements IFileUploadService {
    private final Cloudinary cloudinary;
    @Override
    @Transactional
    public CloudinaryResponse uploadFile(MultipartFile file, String fileName) throws Exception {
        try {
            Map result = cloudinary.uploader().upload(file.getBytes(), Map.of("public_id", "gizmos/product/" + fileName));
            String url = (String) result.get("secure_url");
            String publicId = (String) result.get("public_id");
            return CloudinaryResponse.builder()
                    .publicId(publicId)
                    .url(url)
                    .build();
        } catch (Exception e) {
            throw new Exception("Failed to upload file");
        }
    }
}
