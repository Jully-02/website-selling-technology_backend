package vn.jully.website_selling_technology_backend.services;

public interface IEmailService {
    public void sendMessage (String from, String to, String subject, String text);
}
