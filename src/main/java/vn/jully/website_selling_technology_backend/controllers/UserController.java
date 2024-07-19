package vn.jully.website_selling_technology_backend.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import vn.jully.website_selling_technology_backend.dtos.UserDTO;
import vn.jully.website_selling_technology_backend.dtos.UserLoginDTO;
import vn.jully.website_selling_technology_backend.entities.User;
import vn.jully.website_selling_technology_backend.responses.*;
import vn.jully.website_selling_technology_backend.services.IUserService;
import vn.jully.website_selling_technology_backend.components.LocalizationUtils;
import vn.jully.website_selling_technology_backend.utils.MessageKey;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;
    private final LocalizationUtils localizationUtils;
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
       @Valid @RequestBody UserLoginDTO userLoginDTO
    ) throws Exception{
        try {
            String token = userService.login(userLoginDTO.getEmail(), userLoginDTO.getPassword());
            return ResponseEntity.ok(
                    LoginResponse
                            .builder()
                            .message(localizationUtils.getLocalizedMessage(MessageKey.LOGIN_SUCCESSFULLY))
                            .token(token)
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(LoginResponse
                    .builder()
                    .message(localizationUtils.getLocalizedMessage(MessageKey.LOGIN_FAILED, e.getMessage()))
                    .build());
        }
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
            return ResponseEntity.ok(userService.getUserDetailsFromToken(extractToken));

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
