package vn.jully.website_selling_technology_backend.services.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.jully.website_selling_technology_backend.dtos.UserDTO;
import vn.jully.website_selling_technology_backend.dtos.UserUpdateDTO;
import vn.jully.website_selling_technology_backend.entities.User;
import vn.jully.website_selling_technology_backend.exceptions.DataNotFoundException;
import vn.jully.website_selling_technology_backend.responses.user.UserResponse;

public interface IUserService {
    User insertUser (UserDTO userDTO) throws Exception;

    String login (String email, String password) throws Exception;

    boolean emailUnique (String email);

    int activeAccount (String email, String activeCode) throws DataNotFoundException;

    User getUserDetailsFromToken(String token) throws Exception;

    Page<UserResponse> getUsers(String keyword, Pageable pageable);

    UserResponse updateUser (Long id, UserUpdateDTO userDTO) throws Exception;

    User getUserDetailsFromRefreshToken (String refreshToken) throws Exception;

    void resetPassword (Long userId, String newPassword) throws DataNotFoundException;

    void blockOrEnable (Long userId, Boolean active) throws DataNotFoundException;
}
