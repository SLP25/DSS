package org.example.exceptions.authentication;

public abstract class AuthException extends Exception {
    public AuthException(String msg) {
        super(msg);
    }
}
