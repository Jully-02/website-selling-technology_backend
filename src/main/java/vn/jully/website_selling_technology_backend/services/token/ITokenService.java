package vn.jully.website_selling_technology_backend.services.token;

import vn.jully.website_selling_technology_backend.entities.Token;
import vn.jully.website_selling_technology_backend.entities.User;

public interface ITokenService {

    Token addToken (User user, String token, boolean isMobileDevice);

    Token refreshToken (String refreshToken, User user) throws Exception;
}
