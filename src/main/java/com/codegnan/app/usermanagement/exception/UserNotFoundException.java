package com.codegnan.app.usermanagement.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super("User with id " + id + " was not found");
    }

    public UserNotFoundException(String username) {
        super("User was not found: " + username);
    }
}
