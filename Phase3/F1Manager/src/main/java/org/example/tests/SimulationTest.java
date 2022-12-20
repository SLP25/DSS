package org.example.tests;

import org.example.business.Race;
import org.example.business.Weather;
import org.example.business.cars.*;
import org.example.business.circuit.Circuit;
import org.example.business.circuit.CircuitSection;
import org.example.business.drivers.Driver;
import org.example.business.participants.Participant;
import org.example.business.systems.RaceSystem;
import org.example.business.users.Admin;
import org.example.business.users.Player;
import org.example.data.CircuitDAO;
import org.example.data.DriverDAO;
import org.example.data.PlayerDAO;
import org.example.data.RaceCarDAO;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimulationTest {
    private static final PlayerDAO udb = PlayerDAO.getInstance();
    private static final DriverDAO ddb = DriverDAO.getInstance();
    private static final RaceCarDAO rdb = RaceCarDAO.getInstance();

    private Player createPlayer(int i) {
        String username = "player" + i;
        String password = "123456";
        Player u1 = new Player(username);
        u1.setPassword(password);
        udb.put(u1);
        return u1;
    }

    private Admin createAdmin() {
        String username = "admin1";
        String password = "123456";
        Admin u1 = new Admin(username,true);
        return u1;
    }

    private Driver createDriver(int i) {
        String name = "driver" + i;
        Driver u1 = new Driver(name,0.1F,0.1F);
        ddb.put(u1);
        return u1;
    }

    private CombustionRaceCar createCar() {
        CombustionRaceCar rc=new CombustionRaceCar(S1Class.getInstance(),new Tyre(Tyre.TyreType.HARD),
                new CombustionEngine(Engine.EngineMode.HIGH,6000),
                new BodyWork(BodyWork.DownforcePackage.LOW)
        );
        CombustionRaceCar c = rdb.put(rc);
        return c;
    }

    private Participant createParticipant(int i) {
        return new Participant(1, createCar(), createDriver(i), createPlayer(i));
    }

    private CircuitSection createRandomSection() {
        Random r = new Random();
        return new CircuitSection(CircuitSection.CircuitSectionType.CURVE, r.nextFloat());
    }
    private Circuit createTrack() {
        List<CircuitSection> sections = new ArrayList<>();

        for(int i = 0; i < 10; i++) {
            sections.add(createRandomSection());
        }

        return new Circuit("UML Grand Prix", 5.20f, 6, sections);
    }

    private Race createRace() {
        List<Participant> participants = new ArrayList<>();
        for(int i = 0; i < 22; i++)
            participants.add(createParticipant(i));

        return new Race(0, createAdmin(), new Weather(), createTrack(), participants);
    }

    @Test
    public void test() throws InterruptedException {
        RaceSystem rs = new RaceSystem();
        Race r = createRace();
        rs.addRace(r);
        rs.simulate(r.getId());

        while(true) {
            Race temp = rs.getRaceResults(r.getId());

            List<Double> gaps = temp.getGaps();
            List<Participant> participants = temp.getResults();

            System.out.println("==============================");
            System.out.println("Lap: " + temp.getCurrentLap());
            System.out.println("Weather: " + temp.getWeatherConditions().toString());
            for(int i = 0; i < gaps.size(); i++) {
                System.out.print((i + 1) + ". ");
                System.out.print(participants.get(i).getDriver().getDriverName());
                System.out.print(" ");
                System.out.println(gaps.get(i));
            }
            System.out.println("==============================");
            Thread.sleep(1001);
        }
    }
}
