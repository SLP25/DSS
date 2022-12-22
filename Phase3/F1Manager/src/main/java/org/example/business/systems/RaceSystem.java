package org.example.business.systems;

import org.example.business.Race;
import org.example.business.participants.Participant;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RaceSystem {
    //4
    Map<Integer, Race> races;
    public RaceSystem() {
        races = new HashMap<>();
    }

    public void addRace(Race race) {
        races.put(race.getId(), race);
    }

    public void prepareForRace(int race, String player) {
        Race r = races.get(race);

        if(r != null) {
            r.lock();
            try {
                r.setPlayerAsReady(player);

                if(r.areAllPlayersReady()) {
                    r.simulate();
                }
            } finally {
                r.unlock();
            }

        }
    }

    public List<Participant> getRaceResults(int race) {
        Race r = races.get(race);

        if(r != null) {
            r.lock();
            try {
                if(r.hasFinished()) {
                    return r.getResults();
                } else {
                    return null;
                }
            } finally {
                r.unlock();
            }
        }
        return null;
    }

    public Race getRaceState(int race) {
        Race r = races.get(race);

        if(r != null) {
            r.lock();
            try {
                return new Race(r);
            } finally {
                r.unlock();
            }
        } else {
            return null;
        }
    }
}
