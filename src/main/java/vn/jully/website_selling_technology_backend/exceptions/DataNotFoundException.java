package vn.jully.website_selling_technology_backend.exceptions;

import org.springframework.web.bind.annotation.ControllerAdvice;


public class DataNotFoundException extends Exception {
    public DataNotFoundException (String message) {
        super(message);
    }
}
