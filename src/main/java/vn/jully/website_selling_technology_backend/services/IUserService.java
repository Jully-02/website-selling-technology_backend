package vn.jully.website_selling_technology_backend.services;

import vn.jully.website_selling_technology_backend.dtos.UserDTO;
import vn.jully.website_selling_technology_backend.entities.User;
import vn.jully.website_selling_technology_backend.exceptions.DataNotFoundException;

public interface IUserService {
    User insertUser (UserDTO userDTO);

    String login (String email, String password);
}
