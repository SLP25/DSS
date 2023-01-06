package org.example.exceptions.logic;

public abstract class LogicException extends RuntimeException {
    public LogicException(String msg) {
        super(msg);
    }
}
