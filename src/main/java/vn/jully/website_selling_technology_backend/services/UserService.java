package vn.jully.website_selling_technology_backend.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.jully.website_selling_technology_backend.components.JwtTokenUtils;
import vn.jully.website_selling_technology_backend.dtos.UserDTO;
import vn.jully.website_selling_technology_backend.dtos.UserUpdateDTO;
import vn.jully.website_selling_technology_backend.entities.Role;
import vn.jully.website_selling_technology_backend.entities.User;
import vn.jully.website_selling_technology_backend.exceptions.DataNotFoundException;
import vn.jully.website_selling_technology_backend.exceptions.PermissionDenyException;
import vn.jully.website_selling_technology_backend.repositories.RoleRepository;
import vn.jully.website_selling_technology_backend.repositories.UserRepository;
import vn.jully.website_selling_technology_backend.responses.UserResponse;

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

    private void sendEmailActive (String email, String activeCode) {
        String subject = "Confirm customer account at Gizmos Website";
        String text = "<!DOCTYPE html>" +
                "<html><body>" +
                "Please use the following code to activate your account <" + email + ">:<br/><br/>" +
                "<h1>" + activeCode + "</h1>" +
                "<br/> Click on the link to activate your account: <br/>" +
                "<a href=\"http://localhost:3000/active-account/" + email + "/" + activeCode + "\">" +
                "http://localhost:3000/active-account/" + email + "/" + activeCode + "</a>" +
                "</body></html>";
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
    public UserResponse getUserDetailsFromToken(String token) throws Exception {
        if (jwtTokenUtil.isTokenExpired(token)) {
            throw new Exception("Token is expired");
        }
        String email = jwtTokenUtil.extractEmail(token);
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            return modelMapper.map(user.get(), UserResponse.class);
        } else {
            throw new Exception("User not found");
        }
    }

    @Override
    public List<UserResponse> getUsers() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(user -> {
                    return modelMapper.map(user, UserResponse.class);
                }).toList();
    }

    @Override
    public UserResponse updateUser(Long id, UserUpdateDTO userDTO) throws Exception {
        User userExisting = userRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(("Cannot find User with ID = " + id)));
        userExisting.setFirstName(userDTO.getFirstName());
        userExisting.setLastName(userDTO.getLastName());
        userExisting.setAddress(userDTO.getAddress());
        userExisting.setPhoneNumber(userDTO.getPhoneNumber());

        userRepository.save(userExisting);

        return modelMapper.map(userExisting, UserResponse.class);
    }
}
