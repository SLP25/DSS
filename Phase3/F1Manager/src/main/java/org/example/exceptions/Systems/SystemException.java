package org.example.exceptions.Systems;

public class SystemException extends Exception{
    String msg;

    public SystemException(String msg) {
        this.msg = msg;
    }

    @Override
    public String getMessage() {
        return msg;
    }
}
