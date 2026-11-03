package com.codegnan.app.usermanagement.exception;

public class DuplicateUsernameException extends RuntimeException {
    public DuplicateUsernameException(String username) {
        super("Username is already in use: " + username);
    }
}
