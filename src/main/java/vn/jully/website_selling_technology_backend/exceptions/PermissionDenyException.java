package vn.jully.website_selling_technology_backend.exceptions;

public class PermissionDenyException extends Exception{
    public  PermissionDenyException (String message) {
        super(message);
    }
}
