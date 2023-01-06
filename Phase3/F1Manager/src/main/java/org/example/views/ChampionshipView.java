package org.example.views;

import org.example.business.Championship;
import org.example.business.cars.CombustionRaceCar;
import org.example.business.circuit.Circuit;
import org.example.business.drivers.Driver;
import org.example.business.participants.Participant;

import java.util.List;
import java.util.Map;

public class ChampionshipView extends View {

    public void createSuccess(Championship c) {
        System.out.printf("Successfully created championship %d%n", c.getId());
    }

    public void printStandings(Map<Participant, Integer> standings) {
        System.out.printf("Current standings:%n");

        standings.entrySet().stream().sorted(Map.Entry.comparingByValue()).forEach(e ->
                System.out.printf("\t%d: %s%n", e.getValue(), e.getKey().getManager().getUsername()));
    }

    public void printDrivers(List<Driver> drivers) {
        System.out.printf("Available drivers:%n");

        for (Driver d : drivers)
            System.out.printf("\t%s (SVA: %f, CTS:%f)%n", d.getDriverName(), d.getDriverSVA(), d.getDriverCTS());
    }

    public void signupSuccess(String username) {
        System.out.printf("Player %s has benn successfully signed-up in the championship%n", username);
    }

    public void checkSetup(String username, boolean canChange) {
        System.out.printf("Player %s %s change their setup%n", username, canChange ? "can" : "cannot");
    }

    public void setSetupSuccess() {
        System.out.printf("Setup successfully set%n");
    }

    public void setStrategySuccess() {
        System.out.printf("Strategy successfully set%n");
    }

    public void printRaceCars(List<CombustionRaceCar> cars) {
        System.out.printf("Available cars:%n");

        for (CombustionRaceCar c : cars)
            System.out.printf("\t%d: %s (%s)%n", c.getId(), c.getCategory().getClass().getName(), c.getCombustionEngine().toString());
    }

    public void printCircuits(List<Circuit> circuits) {
        System.out.printf("Available circuits:%n");

        for (Circuit c : circuits)
            System.out.printf("\t%s (%.1fkm, %d laps)%n", c.getName(), c.getCircuitLength(), c.getNumberOfLaps());
    }
}
