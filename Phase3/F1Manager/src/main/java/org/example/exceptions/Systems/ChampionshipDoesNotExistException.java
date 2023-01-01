package org.example.exceptions.Systems;

public class ChampionshipDoesNotExistException extends SystemException {

    public ChampionshipDoesNotExistException(int championshipID){
        super(String.format("The inputted championship (%d) doesn't exist", championshipID));
    }
}
