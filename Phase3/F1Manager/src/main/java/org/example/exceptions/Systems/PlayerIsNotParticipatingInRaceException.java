package org.example.exceptions.Systems;

public class PlayerIsNotParticipatingInRaceException extends SystemException {
    public PlayerIsNotParticipatingInRaceException(String player, int raceID){
        super(String.format("Player %s isn't participating in the race (%d)", player, raceID));
    }
}
