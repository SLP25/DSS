package org.example.exceptions.authentication;

public class UsernameAlreadyExistsException extends AuthException {

    public UsernameAlreadyExistsException(String username) {
        super(String.format("Username '%s' already exists", username));
    }
}
