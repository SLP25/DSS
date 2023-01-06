package org.example.exceptions.system;

public class CircuitDoesNotExistException extends SystemException {
    public CircuitDoesNotExistException(String track) {
        super(String.format("Circuit '%s' doesn't exist", track));
    }
}
