package org.example.exceptions.logic;

import org.example.exceptions.system.SystemException;

public class PlayerAlreadyReachedLimitOfSetupChangesExceptions extends LogicException {

    public PlayerAlreadyReachedLimitOfSetupChangesExceptions(String player) {
        super(String.format("Player %s has reached his limit of setup changes", player));
    }
}
