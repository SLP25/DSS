package org.example.exceptions.system;

import org.example.business.Race;

public class PlayerIsNotParticipatingInRaceException extends SystemException {
    public PlayerIsNotParticipatingInRaceException(String player, Race race) {
        super(String.format("Player '%s' isn't a participant in race '%d'", player, race.getId()));
    }
}
