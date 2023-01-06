package org.example.exceptions.Systems;

public class CircuitDoesNotExistException extends SystemException {
    public CircuitDoesNotExistException(String track) {
        super(String.format("The inputted championship (%s) doesn't exist", track));
    }
}
