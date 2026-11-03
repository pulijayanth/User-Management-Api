package com.codegnan.app.usermanagement.controller;

import com.codegnan.app.usermanagement.dto.ChangePasswordRequest;
import com.codegnan.app.usermanagement.dto.UpdateUserRequest;
import com.codegnan.app.usermanagement.dto.UserResponse;
import com.codegnan.app.usermanagement.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/me")
    public UserResponse getMyProfile(Authentication authentication) {
        return service.findByUsername(authentication.getName());
    }

    @PutMapping("/me")
    public UserResponse updateMyProfile(Authentication authentication,
                                        @Valid @RequestBody UpdateUserRequest request) {
        return service.updateByUsername(authentication.getName(), request);
    }

    @PutMapping("/me/password")
    public ResponseEntity<Void> changeMyPassword(Authentication authentication,
                                                  @Valid @RequestBody ChangePasswordRequest request) {
        service.changePassword(authentication.getName(), request);
        return ResponseEntity.noContent().build();
    }
}
