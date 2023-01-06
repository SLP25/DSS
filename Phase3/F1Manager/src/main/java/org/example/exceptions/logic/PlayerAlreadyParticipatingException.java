package org.example.exceptions.logic;

public class PlayerAlreadyParticipatingException extends LogicException {

    public PlayerAlreadyParticipatingException(String player) {
        super(String.format("Player '%s' is already a participant", player));
    }
}
