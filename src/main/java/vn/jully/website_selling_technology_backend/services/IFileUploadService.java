package vn.jully.website_selling_technology_backend.services;

import org.springframework.web.multipart.MultipartFile;
import vn.jully.website_selling_technology_backend.responses.CloudinaryResponse;

import java.io.IOException;

public interface IFileUploadService {
    CloudinaryResponse uploadFile (MultipartFile multipartFile, String fileName) throws Exception;



}
