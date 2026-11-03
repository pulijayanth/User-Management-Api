package com.codegnan.app.usermanagement.dto;

import com.codegnan.app.usermanagement.entity.User;

public record UserResponse(Long id, String firstName, String lastName, String username, String role) {
    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getFirstName(), user.getLastName(),
                user.getUsername(), user.getRole().name());
    }
}
