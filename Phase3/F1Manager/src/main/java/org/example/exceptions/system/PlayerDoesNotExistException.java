package org.example.exceptions.system;

public class PlayerDoesNotExistException extends SystemException {
    public PlayerDoesNotExistException(String player){
        super(String.format("Player '%s' doesn't exist", player));
    }
}
