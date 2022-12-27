package org.example.tests;

import org.example.business.Championship;
import org.example.business.Race;
import org.example.business.Weather;
import org.example.business.cars.*;
import org.example.business.circuit.Circuit;
import org.example.business.circuit.CircuitSection;
import org.example.business.drivers.Driver;
import org.example.business.participants.Participant;
import org.example.business.users.Admin;
import org.example.business.users.Player;
import org.example.data.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.example.tests.AdminTest.createAdmin;

public class RaceTest {
    private Championship championship;


    public static Set<Integer> createRace(int championship,int n) {
        Set<Integer> s = new HashSet<>();

        for (int i=0;i<n;i++){
            List<Participant> parts=ParticipantTest.createParticipant(championship,10).stream().map(str->ParticipantDAO.getInstance(championship).get(str)).toList();
            Map<Participant,Boolean> p = new HashMap<>();
            for (Participant par:parts)
                p.put(par,false);
            Race r = new Race(
                    championship,
                    false,
                    new Weather(),
                    CircuitDAO.getInstance().get("Monza"),
                    parts,
                    p
                    );
            RaceDAO.getInstance(championship).put(r);
            s.add(r.getId());
        }
        return s;
    }

    @BeforeEach
    public void init() {
        ChampionshipDAO.getInstance().clear();
        Admin a = createAdmin(1).stream().map(x->AdminDAO.getInstance().get(x)).reduce(null,(o,n)->n);
        Championship c=ChampionshipDAO.getInstance().put(new Championship(a));
        RaceDAO.getInstance(c.getId()).clear();
        this.championship=c;
    }

    @Test
    public void createRaceTest() {
        Set<Integer> ids = createRace(this.championship.getId(),1);
        Race r1 = RaceDAO.getInstance(this.championship.getId()).get(ids.stream().min(Integer::compareTo).get());
        Assertions.assertEquals(r1,RaceDAO.getInstance(this.championship.getId()).get(r1.getId()));
        Assertions.assertNull(RaceDAO.getInstance(this.championship.getId()).get(r1.getId()+10000));
    }
    @Test
    public void isEmptyTest() {
        Assertions.assertTrue(RaceDAO.getInstance(this.championship.getId()).isEmpty());
        Set<Integer> ids=createRace(this.championship.getId(),1);
        Assertions.assertFalse(RaceDAO.getInstance(this.championship.getId()).isEmpty());
    }
    @Test
    public void sizeTest() {
        int k = RaceDAO.getInstance(this.championship.getId()).size();
        Assertions.assertEquals(k,RaceDAO.getInstance(this.championship.getId()).size());
        Set<Integer> ids=createRace(this.championship.getId(),10);
        Assertions.assertEquals(k+10,RaceDAO.getInstance(this.championship.getId()).size());
    }
    @Test
    public void containsKeyTest() {
        Set<Integer> ids=createRace(this.championship.getId(),1);
        Assertions.assertFalse(RaceDAO.getInstance(this.championship.getId()).containsKey(ids.stream().max(Integer::compareTo).get()+5));
        Assertions.assertTrue(RaceDAO.getInstance(this.championship.getId()).containsKey(ids.stream().max(Integer::compareTo).get()));
    }

    @Test
    public void containsValueTest(){
        CombustionRaceCar rc=new CombustionRaceCar(S1Class.getInstance(), new Tyre(Tyre.TyreType.HARD),
                new CombustionEngine(Engine.EngineMode.HIGH,6000),
                new BodyWork(BodyWork.DownforcePackage.LOW)
        );
        rc.setId(1000);
        Set<Integer>ids =createRace(this.championship.getId(),5);
        Integer id= ids.stream().min(Integer::compareTo).get();
        Race r = RaceDAO.getInstance(this.championship.getId()).get(id);

        Assertions.assertFalse(RaceDAO.getInstance(this.championship.getId()).containsValue(rc));
        Assertions.assertTrue(RaceDAO.getInstance(this.championship.getId()).containsValue(r));
    }
    @Test
    public void removeTest() {
        int s=RaceDAO.getInstance(this.championship.getId()).size();
        Set<Integer> ids = createRace(this.championship.getId(),2);
        Integer id= ids.stream().min(Integer::compareTo).get();
        Race r = RaceDAO.getInstance(this.championship.getId()).get(id);
        Assertions.assertEquals(r,RaceDAO.getInstance(this.championship.getId()).remove(r.getId()));
        Assertions.assertFalse(RaceDAO.getInstance(this.championship.getId()).containsValue(r));
        Assertions.assertEquals(s+1,RaceDAO.getInstance(this.championship.getId()).size());
    }
}
