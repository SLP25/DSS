package org.example.exceptions.logic;

import org.example.business.Race;
import org.example.exceptions.system.SystemException;

public class PlayerIsNotParticipatingInRaceException extends SystemException {
    public PlayerIsNotParticipatingInRaceException(String player, Race race){
        super(String.format("Player '%s' isn't a participant in race '%d'", player, race.getId()));
    }
}
