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

    public void testRace(int id) {
        System.out.println("Race created and started with id " + id);
    }

    public void printRaceState(Race race) {
        List<Double> gaps = race.getGaps();
        List<Participant> participants = race.getResults();

        System.out.println("==============================");
        System.out.println("Lap: " + race.getCurrentLap());
        System.out.println("Weather: " + race.getWeatherConditions().toString());
        for(int i = 0; i < gaps.size(); i++) {
            System.out.print((i + 1) + ". ");
            System.out.print(participants.get(i).getDriver().getDriverName());
            System.out.print(" ");
            System.out.println(gaps.get(i));
        }
        System.out.println("==============================");
        try {
            Thread.sleep(1001);
        } catch(InterruptedException e) {}
    }

    public void printRaceResults(List<Participant> participants) {
        System.out.printf("Race results:%n");
        for (int i = 0; i < participants.size(); i++) {
            Participant p = participants.get(i);
            System.out.printf("\t%d) %s%n", i + 1, p.getDriver().getDriverName());
        }
    }
}
