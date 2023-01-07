package org.example.exceptions.authentication;

public class WrongPasswordException extends AuthException {
    public WrongPasswordException() {
        super("Wrong password");
    }
}
