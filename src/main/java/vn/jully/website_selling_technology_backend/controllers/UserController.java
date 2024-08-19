package vn.jully.website_selling_technology_backend.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import vn.jully.website_selling_technology_backend.dtos.RefreshTokenDTO;
import vn.jully.website_selling_technology_backend.dtos.UserDTO;
import vn.jully.website_selling_technology_backend.dtos.UserLoginDTO;
import vn.jully.website_selling_technology_backend.dtos.UserUpdateDTO;
import vn.jully.website_selling_technology_backend.entities.Token;
import vn.jully.website_selling_technology_backend.entities.User;
import vn.jully.website_selling_technology_backend.exceptions.DataNotFoundException;
import vn.jully.website_selling_technology_backend.responses.user.*;
import vn.jully.website_selling_technology_backend.services.token.ITokenService;
import vn.jully.website_selling_technology_backend.services.user.IUserService;
import vn.jully.website_selling_technology_backend.components.LocalizationUtils;
import vn.jully.website_selling_technology_backend.utils.MessageKey;

import java.security.SecureRandom;
import java.util.List;

@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;
    private final LocalizationUtils localizationUtils;
    private final ModelMapper modelMapper;
    private final ITokenService tokenService;
    private static final String UPPER_CASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER_CASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL_CHARACTERS = "@$!%*?&";

    private static final SecureRandom RANDOM = new SecureRandom();
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> insertUser (
        @Valid @RequestBody UserDTO userDTO,
        BindingResult result
    ) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(
                        RegisterResponse
                                .builder()
                                .message(localizationUtils.getLocalizedMessage(MessageKey.INVALID_ERROR, errorMessages.toString()))
                                .build()
                );
            }
            if (!userDTO.getPassword().equals(userDTO.getRetypePassword())) {
                return ResponseEntity.badRequest().body(
                        RegisterResponse
                                .builder()
                                .message(localizationUtils.getLocalizedMessage(MessageKey.PASSWORD_NOT_MATCH))
                                .build()
                );
            }
            User user = userService.insertUser(userDTO);
            return ResponseEntity.ok(
                    RegisterResponse
                            .builder()
                            .message(localizationUtils.getLocalizedMessage(MessageKey.REGISTER_SUCCESSFULLY))
                            .user(user)
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    RegisterResponse
                            .builder()
                            .message(localizationUtils.getLocalizedMessage(MessageKey.REGISTER_FAILED, e.getMessage()))
                            .build()
            );
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login (
       @Valid @RequestBody UserLoginDTO userLoginDTO,
       HttpServletRequest request
    ) throws Exception{
        try {
            String token = userService.login(userLoginDTO.getEmail(), userLoginDTO.getPassword());
            String userAgent = request.getHeader("User-Agent");
            User user = userService.getUserDetailsFromToken(token);
            Token jwtToken = tokenService.addToken(user, token, userAgent.toLowerCase().contains("mobile"));

            return ResponseEntity.ok(
                    LoginResponse
                            .builder()
                            .message(localizationUtils.getLocalizedMessage(MessageKey.LOGIN_SUCCESSFULLY))
                            .token(token)
                            .tokenType(jwtToken.getTokenType())
                            .refreshToken(jwtToken.getRefreshToken())
                            .username(user.getUsername())
                            .roles(user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                            .id(user.getId())
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(LoginResponse
                    .builder()
                    .message(localizationUtils.getLocalizedMessage(MessageKey.LOGIN_FAILED, e.getMessage()))
                    .build());
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<LoginResponse> refreshToken (
            @Valid @RequestBody RefreshTokenDTO refreshTokenDTO,
            BindingResult result
    ) {
        try {
            User userDetail = userService.getUserDetailsFromRefreshToken(refreshTokenDTO.getRefreshToken());
            Token jwtToken = tokenService.refreshToken(refreshTokenDTO.getRefreshToken(), userDetail);
            return ResponseEntity.ok(
                    LoginResponse.builder()
                            .message("Refresh token successfully")
                            .token(jwtToken.getToken())
                            .tokenType(jwtToken.getTokenType())
                            .refreshToken(jwtToken.getRefreshToken())
                            .username(userDetail.getUsername())
                            .roles(userDetail.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                            .id(userDetail.getId())
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    LoginResponse.builder()
                            .message(localizationUtils.getLocalizedMessage(MessageKey.LOGIN_FAILED, e.getMessage()))
                            .build()
            );
        }
    }

    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserListResponse> getUsers (
            @RequestParam("page") int page,
            @RequestParam("limit") int limit,
            @RequestParam("keyword") String keyword
    ) {
        PageRequest pageRequest = PageRequest.of(
                page, limit,
                Sort.by("id").ascending()
        );
        Page<UserResponse> userPages = userService.getUsers(keyword, pageRequest);
        int totalPages = userPages.getTotalPages();
        List<UserResponse> userResponses = userPages.getContent();
        return ResponseEntity.ok(
                UserListResponse.builder()
                        .userResponses(userResponses)
                        .totalPages(totalPages)
                        .build()
        );
    }

    @GetMapping("/email-unique")
    public ResponseEntity<EmailUniqueResponse> emailUnique (@RequestParam("email") String email) {
        boolean isUnique = userService.emailUnique(email);
        if (!isUnique) {
            return ResponseEntity.ok(
                    EmailUniqueResponse
                            .builder()
                            .message(localizationUtils.getLocalizedMessage(MessageKey.EMAIL_NOT_EXIST))
                            .state(true)
                            .build()
            );
        }
        return ResponseEntity.ok(
                EmailUniqueResponse
                        .builder()
                        .message(localizationUtils.getLocalizedMessage(MessageKey.EMAIL_EXIST))
                        .state(false)
                        .build()
        );
    }

    @GetMapping("/active-account")
    public ResponseEntity<ActiveAccountResponse> activeAccount (
            @RequestParam("email") String email,
            @RequestParam("active-code") String activeCode
    ) {
        try {
            int isActive = userService.activeAccount(email, activeCode);
            if (isActive == 1) {
                return ResponseEntity.ok(
                        ActiveAccountResponse
                                .builder()
                                .message(localizationUtils.getLocalizedMessage(MessageKey.ACTIVATED_ACCOUNT))
                                .build()
                );
            } else if (isActive == 2) {
                return ResponseEntity.ok(
                        ActiveAccountResponse
                                .builder()
                                .message(localizationUtils.getLocalizedMessage(MessageKey.ACTIVATION_SUCCESSFULLY))
                                .build()
                );
            }
            return ResponseEntity.badRequest().body(
                    ActiveAccountResponse
                            .builder()
                            .message(localizationUtils.getLocalizedMessage(MessageKey.ACTIVATION_FAILED))
                            .build()
            );
        } catch (Exception e) {
            return  ResponseEntity.badRequest().body(
                    ActiveAccountResponse
                            .builder()
                            .message(localizationUtils.getLocalizedMessage(e.getMessage()))
                            .build()
            );
        }
    }

    @GetMapping("/details")
    public ResponseEntity<UserResponse> getUserDetails (@RequestHeader("Authorization") String token) {
        try {
            String extractToken = token.substring(7);
            User user = userService.getUserDetailsFromToken(extractToken);
            return ResponseEntity.ok(modelMapper.map(user, UserResponse.class));

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<?> updateUser (@PathVariable("id") Long userId, @Valid @RequestBody UserUpdateDTO userDTO) {
        try {
            return ResponseEntity.ok(userService.updateUser(userId, userDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/reset-password/{user-id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> resetPassword (
            @PathVariable("user-id") Long userId
    ) {
        try {
            String newPassword = "" +
                    UPPER_CASE.charAt(RANDOM.nextInt(UPPER_CASE.length())) +
                    LOWER_CASE.charAt(RANDOM.nextInt(LOWER_CASE.length())) +
                    LOWER_CASE.charAt(RANDOM.nextInt(LOWER_CASE.length())) +
                    LOWER_CASE.charAt(RANDOM.nextInt(LOWER_CASE.length())) +
                    SPECIAL_CHARACTERS.charAt(RANDOM.nextInt(SPECIAL_CHARACTERS.length())) +
                    DIGITS.charAt(RANDOM.nextInt(DIGITS.length())) +
                    DIGITS.charAt(RANDOM.nextInt(DIGITS.length())) +
                    DIGITS.charAt(RANDOM.nextInt(DIGITS.length()));
            userService.resetPassword(userId, newPassword);
            return ResponseEntity.ok(newPassword);
        } catch (DataNotFoundException e) {
            return ResponseEntity.badRequest().body("User not found");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/block/{user-id}/{active}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> blockOrEnable (
            @PathVariable("user-id") Long userId,
            @PathVariable("active") int active
    ) {
        try {
            userService.blockOrEnable(userId, active > 0);
            String message = active > 0 ? "Successfully enabled the user." : "Successfully blocked the user.";
            return ResponseEntity.ok(message);
        } catch (DataNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
