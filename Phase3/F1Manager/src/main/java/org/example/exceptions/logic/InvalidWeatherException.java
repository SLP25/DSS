package org.example.exceptions.logic;

public class InvalidWeatherException extends RuntimeException {
    public InvalidWeatherException(String msg) {
        super(msg);
    }
}
