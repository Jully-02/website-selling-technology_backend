package vn.jully.website_selling_technology_backend.services.file_upload;

import org.springframework.web.multipart.MultipartFile;
import vn.jully.website_selling_technology_backend.responses.cloudinary.CloudinaryResponse;

public interface IFileUploadService {
    CloudinaryResponse uploadFile (MultipartFile multipartFile, String fileName) throws Exception;
}
