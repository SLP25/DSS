package org.example.views;

import org.example.business.participants.Participant;
import org.example.business.Race;

import java.util.List;

public class RaceView extends View {

    public void createSuccess(Race race) {
        System.out.printf("Successfully created race %d%n", race.getId());
    }

    public void preparedForRace(String username) {
        System.out.printf("Player %s is now prepared for the race%n", username);
    }

    public void printRaceState(Race race) {
        System.out.printf("TODO%s");    //TODO
    }

    public void printRaceResults(List<Participant> participants) {
        System.out.printf("Race results:%n");
        for (Participant p : participants) {
            System.out.printf("\t%s: TODO%n", p.getManager().getUsername()); //TODO
        }
    }
}
