package org.example.exceptions.Systems;

public class RaceDoesNotExistException extends SystemException {
    public RaceDoesNotExistException(int raceID){
        super(String.format("The inputted race (%d) doesn't exist", raceID));
    }
}
