package org.example.tests;

import org.example.business.Championship;
import org.example.business.cars.CombustionRaceCar;
import org.example.business.drivers.Driver;
import org.example.business.participants.Participant;
import org.example.business.users.Admin;
import org.example.data.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.example.tests.AdminTest.createAdmin;

public class ParticipantTest {
    private Championship championship;

    public static Set<String> createParticipant(int championship, int n) {
        Set<String> participants = new HashSet<>();
        List<Driver> drivers = DriverDAO.getInstance().values().stream().limit(n).toList();
        List<CombustionRaceCar> cars = RaceCarDAO.getInstance().values().stream().limit(n).toList();
        List<String> players = new ArrayList<>(PlayerTest.createPlayer(n));
        for (int i = 0; i < n; i++) {
            Participant u = new Participant(championship, 0,
                    cars.get(i),
                    drivers.get(i),
                    PlayerDAO.getInstance().get(players.get(i))
            );
            ParticipantDAO.getInstance(championship).put(u);
            participants.add(u.getManager().getUsername());
        }
        return participants;
    }

    @BeforeEach
    public void init() {
        ChampionshipDAO.getInstance().clear();
        Admin a = createAdmin(1).stream().map(x -> AdminDAO.getInstance().get(x)).reduce(null, (o, n) -> n);
        Championship c = ChampionshipDAO.getInstance().put(new Championship(a));
        ParticipantDAO.getInstance(c.getId()).clear();
        this.championship = c;
    }

    @Test
    public void createParticipantTest() {
        Set<String> s = createParticipant(this.championship.getId(), 1);
        String username = s.stream().min(String::compareTo).get();
        Participant p1 = ParticipantDAO.getInstance(this.championship.getId()).get(username);

        Assertions.assertEquals(p1, ParticipantDAO.getInstance(this.championship.getId()).get(username));
        Assertions.assertNull(ParticipantDAO.getInstance(this.championship.getId()).get("player2"));
    }

    @Test
    public void isEmptyTest() {
        Assertions.assertTrue(ParticipantDAO.getInstance(this.championship.getId()).isEmpty());
        createParticipant(this.championship.getId(), 20);
        Assertions.assertFalse(ParticipantDAO.getInstance(this.championship.getId()).isEmpty());
    }

    @Test
    public void sizeTest() {
        Assertions.assertEquals(ParticipantDAO.getInstance(this.championship.getId()).size(), 0);
        createParticipant(this.championship.getId(), 10);
        Assertions.assertEquals(ParticipantDAO.getInstance(this.championship.getId()).size(), 10);
    }

    @Test
    public void containsKeyTest() {
        Set<String> ids = createParticipant(this.championship.getId(), 5);
        Assertions.assertFalse(ParticipantDAO.getInstance(this.championship.getId()).containsKey(ids.stream().max(String::compareTo).get() + "123456"));
        Assertions.assertTrue(ParticipantDAO.getInstance(this.championship.getId()).containsKey(ids.stream().max(String::compareTo).get()));
    }

    @Test
    public void containsValueTest() {
        Set<String> s = createParticipant(this.championship.getId(), 1);
        String username = s.stream().min(String::compareTo).get();
        Participant p1 = ParticipantDAO.getInstance(this.championship.getId()).get(username);
        Assertions.assertTrue(ParticipantDAO.getInstance(this.championship.getId()).containsValue(p1));
        p1.setChampionship(this.championship.getId() + 20);
        Assertions.assertFalse(ParticipantDAO.getInstance(this.championship.getId()).containsValue(p1));
    }

    @Test
    public void removeTest() {
        Set<String> s = createParticipant(this.championship.getId(), 10);
        Participant v = ParticipantDAO.getInstance(this.championship.getId()).get(s.stream().min(String::compareTo).get());
        Assertions.assertEquals(ParticipantDAO.getInstance(this.championship.getId()).remove(v.getManager().getUsername()), v);
        Assertions.assertFalse(ParticipantDAO.getInstance(this.championship.getId()).containsValue(v));
        Assertions.assertEquals(ParticipantDAO.getInstance(this.championship.getId()).size(), 9);
    }

    @Test
    public void putAllTest() {
        Map<String, Participant> m = new HashMap<>();
        Set<String> participants = new HashSet<>();
        List<Driver> drivers = DriverDAO.getInstance().values().stream().limit(4).toList();
        List<CombustionRaceCar> cars = RaceCarDAO.getInstance().values().stream().limit(4).toList();
        List<String> players = new ArrayList<>(PlayerTest.createPlayer(4));
        for (int i = 0; i < 4; i++) {
            Participant u = new Participant(this.championship.getId(), 0,
                    cars.get(i),
                    drivers.get(i),
                    PlayerDAO.getInstance().get(players.get(i))
            );
            m.put(u.getManager().getUsername(), u);
        }
        ParticipantDAO.getInstance(this.championship.getId()).putAll(m);
        for (String s : m.keySet()) {
            Assertions.assertEquals(m.get(s), ParticipantDAO.getInstance(this.championship.getId()).get(s));
        }
    }

    @Test
    public void increaseNumberOfSetupChangesTest() {
        Set<String> s = createParticipant(this.championship.getId(), 1);
        String username = s.stream().min(String::compareTo).get();
        Participant p1 = ParticipantDAO.getInstance(this.championship.getId()).get(username);
        Assertions.assertEquals(0, p1.getNumberOfSetupChanges());
        p1.increaseNumberOfSetupChanges();
        Assertions.assertEquals(1, p1.getNumberOfSetupChanges());
        Assertions.assertEquals(1, ParticipantDAO.getInstance(this.championship.getId()).get(p1.getManager().getUsername()).getNumberOfSetupChanges());
    }

}
