package vn.jully.website_selling_technology_backend.services;

import vn.jully.website_selling_technology_backend.dtos.UserDTO;
import vn.jully.website_selling_technology_backend.entities.User;
import vn.jully.website_selling_technology_backend.exceptions.DataNotFoundException;
import vn.jully.website_selling_technology_backend.responses.UserResponse;

public interface IUserService {
    User insertUser (UserDTO userDTO) throws Exception;

    String login (String email, String password) throws Exception;

    boolean emailUnique (String email);

    public int activeAccount (String email, String activeCode) throws DataNotFoundException;

    public UserResponse getUserDetailsFromToken(String token) throws Exception;
}
