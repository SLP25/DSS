package org.example.exceptions.logic;

public class PlayerAlreadyReachedLimitOfSetupChangesExceptions extends LogicException {

    public PlayerAlreadyReachedLimitOfSetupChangesExceptions(String player) {
        super(String.format("Player %s has reached his limit of setup changes", player));
    }
}
