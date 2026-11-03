package com.codegnan.app.usermanagement.controller;

import com.codegnan.app.usermanagement.dto.AuthResponse;
import com.codegnan.app.usermanagement.dto.CreateUserRequest;
import com.codegnan.app.usermanagement.dto.ForgotPasswordRequest;
import com.codegnan.app.usermanagement.dto.MessageResponse;
import com.codegnan.app.usermanagement.dto.UserResponse;
import com.codegnan.app.usermanagement.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService service;

    public AuthController(UserService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody CreateUserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<MessageResponse> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        service.resetPasswordForDemo(request.username(), request.newPassword());
        return ResponseEntity.ok(new MessageResponse(
                "If the account exists, its password was reset. This demo flow does not verify identity."));
    }

    // Spring Security authenticates the HTTP Basic credentials before this method is called.
    @PostMapping("/login")
    public AuthResponse login(Authentication authentication) {
        UserResponse user = service.findByUsername(authentication.getName());
        return new AuthResponse(user.username(), user.role(),
                "Authentication successful. Send HTTP Basic credentials with each protected request.");
    }

    @GetMapping("/me")
    public UserResponse currentUser(Authentication authentication) {
        return service.findByUsername(authentication.getName());
    }
}
