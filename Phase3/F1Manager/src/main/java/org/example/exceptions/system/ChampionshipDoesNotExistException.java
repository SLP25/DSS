package org.example.exceptions.system;

public class ChampionshipDoesNotExistException extends SystemException {

    public ChampionshipDoesNotExistException(int championshipID) {
        super(String.format("Championship '%d' doesn't exist", championshipID));
    }
}
