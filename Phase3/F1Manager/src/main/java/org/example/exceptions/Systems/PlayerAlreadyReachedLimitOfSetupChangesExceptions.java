package org.example.exceptions.Systems;

public class PlayerAlreadyReachedLimitOfSetupChangesExceptions extends SystemException {

    public PlayerAlreadyReachedLimitOfSetupChangesExceptions(String player) {
        super(String.format("Player %s has reached his limit of setup changes", player));
    }
}
