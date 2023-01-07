package org.example.exceptions.system;

public abstract class SystemException extends RuntimeException{

    public SystemException(String msg) {
        super(msg);
    }
}
