package org.example.exceptions.logic;

public class ParticipantDoesNotExistException extends LogicException {
    public ParticipantDoesNotExistException(String participant) {
        super(String.format("Participant '%s' doesn't exist", participant));
    }
}
