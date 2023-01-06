package org.example.exceptions.Systems;

public class AdminDoesNotExistException extends SystemException {

    public AdminDoesNotExistException(String admin) {
        super(String.format("The inputted admin (%s) doesn't exist", admin));
    }
}
