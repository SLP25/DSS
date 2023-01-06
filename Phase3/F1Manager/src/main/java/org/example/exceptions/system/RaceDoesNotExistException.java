package org.example.exceptions.system;

public class RaceDoesNotExistException extends SystemException {
    public RaceDoesNotExistException(int raceID) {
        super(String.format("Race '%d' doesn't exist", raceID));
    }
}
