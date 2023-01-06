package org.example.exceptions.system;

public class AdminDoesNotExistException extends SystemException {

    public AdminDoesNotExistException(String admin) {
        super(String.format("Admin '%s' doesn't exist", admin));
    }
}
