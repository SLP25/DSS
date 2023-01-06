package org.example.business.systems;

import org.example.business.Race;
import org.example.business.participants.Participant;
import org.example.exceptions.Systems.ChampionshipDoesNotExistException;
import org.example.exceptions.Systems.CircuitDoesNotExistException;
import org.example.exceptions.Systems.RaceDoesNotExistException;

import java.util.List;

public interface RaceSystemFacade extends SystemFacade {

    Race createRace(int championship, float weather, String track) throws ChampionshipDoesNotExistException, CircuitDoesNotExistException;

    void prepareForRace(int championship, int race, String player) throws ChampionshipDoesNotExistException, RaceDoesNotExistException;

    Race getRaceState(int championship, int race) throws ChampionshipDoesNotExistException, RaceDoesNotExistException;

    List<Participant> getRaceResults(int championship, int race) throws ChampionshipDoesNotExistException, RaceDoesNotExistException;
}
