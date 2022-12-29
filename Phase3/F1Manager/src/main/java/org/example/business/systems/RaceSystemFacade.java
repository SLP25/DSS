package org.example.business.systems;

import org.example.business.Race;
import org.example.business.participants.Participant;

import java.util.List;

public interface RaceSystemFacade {

    void prepareForRace(int race, String player);

    Race getRaceState(int race);

    List<Participant> getRaceResults(int race);
}
