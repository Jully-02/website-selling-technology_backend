package vn.jully.website_selling_technology_backend.services;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import vn.jully.website_selling_technology_backend.dtos.UserDTO;
import vn.jully.website_selling_technology_backend.entities.Role;
import vn.jully.website_selling_technology_backend.entities.User;
import vn.jully.website_selling_technology_backend.exceptions.DataNotFoundException;
import vn.jully.website_selling_technology_backend.repositories.RoleRepository;
import vn.jully.website_selling_technology_backend.repositories.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    @Override
    public User insertUser(UserDTO userDTO){
        String email = userDTO.getEmail();
        // Check if email exists or not?
        if (userRepository.existsByEmail(email)) {
            throw new DataIntegrityViolationException("Email already exists");
        }
        // Convert userDTO to User
        List<Role> roles = userDTO.getRoleIds().stream()
                .map(roleId -> {
                    try {
                        return roleRepository.findById(roleId)
                                .orElseThrow(() -> new DataNotFoundException("Role ID: " + roleId + " not found"));
                    } catch (DataNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();

        User newUser = User
                .builder()
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .email(userDTO.getEmail())
                .phoneNumber(userDTO.getPhoneNumber())
                .dateOfBirth(userDTO.getDateOfBirth())
                .address(userDTO.getAddress())
                .googleAccountId(userDTO.getGoogleAccountId())
                .facebookAccountId(userDTO.getFacebookAccountId())
                .roleList(roles)
                .build();
        /// Check if there is an accountId -> No password required
        if (userDTO.getFacebookAccountId() == 0 && userDTO.getGoogleAccountId() == 0) {
            String password = userDTO.getPassword();
//            String encodedPassword = passwordEncoder.encode(password);
//            newUser.setPassword(encodedPassword);
        }
        return userRepository.save(newUser);
    }

    @Override
    public String login(String email, String password) {
        return null;
    }
}
