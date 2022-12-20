package org.example.business.systems;

import org.example.business.Race;
import org.example.business.participants.Participant;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RaceSystem {
    //TODO:: Remove temporary method
    Map<Integer, Race> races;
    public RaceSystem() {
        races = new HashMap<>();
    }

    public void addRace(Race race) {
        races.put(race.getId(), race);
    }
    public void simulate(int raceId) {
        Race r = races.get(raceId);
        if(r != null) {
            new Thread(() -> {
                try {
                    r.simulate();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }
    }

    public Race getRaceResults(int raceId) {
        Race r = races.get(raceId);

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
