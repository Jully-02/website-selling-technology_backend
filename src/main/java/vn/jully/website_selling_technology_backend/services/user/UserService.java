package vn.jully.website_selling_technology_backend.services.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.jully.website_selling_technology_backend.components.JwtTokenUtils;
import vn.jully.website_selling_technology_backend.dtos.UserDTO;
import vn.jully.website_selling_technology_backend.dtos.UserUpdateDTO;
import vn.jully.website_selling_technology_backend.entities.Role;
import vn.jully.website_selling_technology_backend.entities.Token;
import vn.jully.website_selling_technology_backend.entities.User;
import vn.jully.website_selling_technology_backend.exceptions.DataNotFoundException;
import vn.jully.website_selling_technology_backend.exceptions.PermissionDenyException;
import vn.jully.website_selling_technology_backend.repositories.RoleRepository;
import vn.jully.website_selling_technology_backend.repositories.TokenRepository;
import vn.jully.website_selling_technology_backend.repositories.UserRepository;
import vn.jully.website_selling_technology_backend.responses.user.UserResponse;
import vn.jully.website_selling_technology_backend.services.email.IEmailService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtils jwtTokenUtil;
    private final AuthenticationManager authenticationManager;
    private final IEmailService emailService;
    private final ModelMapper modelMapper;
    private final TokenRepository tokenRepository;
    @Override
    @Transactional
    public User insertUser(UserDTO userDTO) throws Exception{
        String email = userDTO.getEmail();
        // Check if email exists or not?
        if (userRepository.existsByEmail(email)) {
            throw new DataIntegrityViolationException("Email already exists");
        }
        // Convert userDTO to User
        List<Role> roles = userDTO.getRoleIds().stream()
                .map(roleId -> {
                    try {
                        Role role = roleRepository.findById(roleId)
                                .orElseThrow(() -> new DataNotFoundException("Role ID: " + roleId + " not found"));
                        if (role.getName().equalsIgnoreCase("ADMIN")) {
                            throw new PermissionDenyException("You cannot register a admin account");
                        }
                        return role;
                    } catch (DataNotFoundException | PermissionDenyException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();

        User newUser = User
                .builder()
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .fullName(userDTO.getFirstName() + " " + userDTO.getLastName())
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
            String encodedPassword = passwordEncoder.encode(password);
            newUser.setPassword(encodedPassword);
        }

        newUser.setActiveCode(UUID.randomUUID().toString());
        newUser.setActive(false);

        userRepository.save(newUser);

        sendEmailActive(newUser.getEmail(), newUser.getActiveCode());

        return newUser;
    }

    @Override
    public String login(String email, String password) throws Exception {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new DataNotFoundException("Invalid email / password");
        }
        // return optionalUser.get(); //want to return JWT token?
        User existingUser = optionalUser.get();
        // Check password
        if (existingUser.getFacebookAccountId() == 0
                && existingUser.getGoogleAccountId() == 0) {
            if (!passwordEncoder.matches(password, existingUser.getPassword())) {
                throw new BadCredentialsException("Wrong email or password");
            }
        }

//        if (!optionalUser.get().isActive()) {
//            throw new DataNotFoundException()
//        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                email, password,
                existingUser.getAuthorities()
        );
        // Authenticate with Java Spring security
        authenticationManager.authenticate(authenticationToken);
        return jwtTokenUtil.generateToken(existingUser);
    }

    @Override
    public boolean emailUnique (String email) {
        return userRepository.existsByEmail(email);
    };

    private void sendEmailActive(String email, String activeCode) {
        String subject = "Confirm customer account at Gizmos Website";
        String text = "<!DOCTYPE html>" +
                "<html>" +
                "<body style=\"font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 0;\">" +
                "<div style=\"max-width: 600px; margin: 20px auto; background-color: #ffffff; padding: 20px; border-radius: 8px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);\">" +
                "<div style=\"text-align: center; padding-bottom: 20px; border-bottom: 1px solid #dddddd;\">" +
                "<h1 style=\"color: #333333;\">Gizmos Website</h1>" +
                "</div>" +
                "<div style=\"margin-top: 20px;\">" +
                "<p style=\"font-size: 16px; line-height: 1.6; color: #666666;\">Please use the following code to activate your account associated with this email (<strong>" + email + "</strong>):</p>" +
                "<h1 style=\"font-size: 20px; color: #4CAF50; text-align: center;\">" + activeCode + "</h1>" +
                "<p style=\"font-size: 16px; line-height: 1.6; color: #666666;\">Click on the link below to activate your account:</p>" +
                "<p style=\"text-align: center;\"><a href=\"http://localhost:3000/active-account/" + email + "/" + activeCode + "\" style=\"font-size: 18px; color: #ffffff; background-color: #007BFF; padding: 10px 20px; text-decoration: none; border-radius: 4px;\">Activate Account</a></p>" +
                "</div>" +
                "<div style=\"margin-top: 30px; padding-top: 20px; border-top: 1px solid #dddddd; text-align: center; font-size: 14px; color: #aaaaaa;\">" +
                "<p>&copy; 2024 Gizmos. All rights reserved.</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
        emailService.sendMessage("gizmos.services@gmail.com", email, subject, text);
    }

    private void sendEmailResetPassword(String email, String newPassword) {
        String subject = "Your New Password for Gizmos Website";
        String text = "<!DOCTYPE html>" +
                "<html>" +
                "<body style=\"font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 0;\">" +
                "<div style=\"max-width: 600px; margin: 20px auto; background-color: #ffffff; padding: 20px; border-radius: 8px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);\">" +
                "<div style=\"text-align: center; padding-bottom: 20px; border-bottom: 1px solid #dddddd;\">" +
                "<h1 style=\"color: #333333;\">Gizmos Website</h1>" +
                "</div>" +
                "<div style=\"margin-top: 20px;\">" +
                "<p style=\"font-size: 16px; line-height: 1.6; color: #666666;\">Your password has been reset. Your new password for the Gizmos account associated with this email (<strong>" + email + "</strong>) is:</p>" +
                "<p style=\"font-size: 20px; color: #ff5722; font-weight: bold; text-align: center;\">" + newPassword + "</p>" +
                "<p style=\"font-size: 16px; line-height: 1.6; color: #666666;\">Please change this password after logging in for security purposes.</p>" +
                "<p style=\"font-size: 16px; line-height: 1.6; color: #666666;\">If you did not request this change, please contact our support immediately.</p>" +
                "</div>" +
                "<div style=\"margin-top: 30px; padding-top: 20px; border-top: 1px solid #dddddd; text-align: center; font-size: 14px; color: #aaaaaa;\">" +
                "<p>&copy; 2024 Gizmos. All rights reserved.</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
        emailService.sendMessage("gizmos.services@gmail.com", email, subject, text);
    }


    @Override
    public int activeAccount (String email, String activeCode) throws DataNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new DataNotFoundException("Cannot find User with email = " + email));

        if (user.isActive()) {
            return 1;
        }

        if (activeCode.equals(user.getActiveCode())) {
            user.setActive(true);
            userRepository.save(user);
            return 2;
        }
        return 0;
    }

    @Override
    public User getUserDetailsFromToken(String token) throws Exception {
        if (jwtTokenUtil.isTokenExpired(token)) {
            throw new Exception("Token is expired");
        }
        String email = jwtTokenUtil.extractEmail(token);
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new Exception("User not found");
        }
    }

    @Override
    public User getUserDetailsFromRefreshToken (String refreshToken) throws Exception {
        Token existingToken = tokenRepository.findByRefreshToken(refreshToken);
        return getUserDetailsFromToken(existingToken.getToken());
    }

    @Override
    public Page<UserResponse> getUsers(String keyword, Pageable pageable) {
        Page<User> userPage = userRepository.findAll(keyword, pageable);

        return userPage.map(
                user -> {
                    return modelMapper.map(user, UserResponse.class);
                }
        );
    }

    @Override
    public UserResponse updateUser(Long id, UserUpdateDTO userDTO) throws Exception {
        User userExisting = userRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(("Cannot find User with ID = " + id)));
        userExisting.setFirstName(userDTO.getFirstName());
        userExisting.setLastName(userDTO.getLastName());
        userExisting.setAddress(userDTO.getAddress());
        userExisting.setPhoneNumber(userDTO.getPhoneNumber());
        userExisting.setFullName(userDTO.getFullName() + " " + userDTO.getLastName());
        if (userDTO.getNewPassword() != null) {
            String encodedPassword = passwordEncoder.encode(userDTO.getNewPassword());
            userExisting.setPassword(encodedPassword);
        }

        userRepository.save(userExisting);

        return modelMapper.map(userExisting, UserResponse.class);
    }


    @Override
    @Transactional
    public void resetPassword (Long userId, String newPassword) throws DataNotFoundException {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("Cannot find User with ID = " + userId));
        String encodedPassword = passwordEncoder.encode(newPassword);
        existingUser.setPassword(encodedPassword);
        userRepository.save(existingUser);
        // Reset password -> clear token
        List<Token> tokens = tokenRepository.findByUser(existingUser);
        for (Token token : tokens) {
            tokenRepository.delete(token);
        }
        sendEmailResetPassword(existingUser.getEmail(), newPassword);
    }

    @Override
    @Transactional
    public void blockOrEnable (Long userId, Boolean active) throws DataNotFoundException {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("Cannot find User with ID = " + userId));
        existingUser.setActive(active);
        userRepository.save(existingUser);
    }
}
