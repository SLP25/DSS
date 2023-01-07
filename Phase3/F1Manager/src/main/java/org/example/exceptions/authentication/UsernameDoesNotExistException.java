package org.example.exceptions.authentication;

public class UsernameDoesNotExistException extends AuthException {

    public UsernameDoesNotExistException(String username) {
        super(String.format("Username '%s' doesn't exist", username));
    }
}
