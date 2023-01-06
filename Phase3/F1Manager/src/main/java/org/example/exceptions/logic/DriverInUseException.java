package org.example.exceptions.logic;

public class DriverInUseException extends LogicException {

    public DriverInUseException(String driver) {
        super(String.format("Driver '%s' is already in use", driver));
    }
}
