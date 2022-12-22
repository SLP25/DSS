package org.example.tests;

import org.example.business.Race;
import org.example.business.Weather;
import org.example.business.cars.*;
import org.example.business.circuit.Circuit;
import org.example.business.circuit.CircuitSection;
import org.example.business.drivers.Driver;
import org.example.business.participants.Participant;
import org.example.business.users.Admin;
import org.example.business.users.Player;
import org.example.data.RaceCarDAO;
import org.example.data.RaceDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

public class RaceTest {
    private static final RaceDAO rdb = RaceDAO.getInstance();

    private static Player createPlayer(int i) {
        String username = "player" + i;
        String password = "123456";
        Player u1 = new Player(username);
        u1.setPassword(password);
        return u1;
    }

    private static Admin createAdmin() {
        String username = "admin1";
        String password = "123456";
        Admin u1 = new Admin(username,true);
        return u1;
    }

    private static Driver createDriver(int i) {
        String name = "driver" + i;
        Driver u1 = new Driver(name,0.1F,0.1F);
        return u1;
    }

    private static CombustionRaceCar createCar() {
        CombustionRaceCar rc=new CombustionRaceCar(S1Class.getInstance(),new Tyre(Tyre.TyreType.HARD),
                new CombustionEngine(Engine.EngineMode.HIGH,6000),
                new BodyWork(BodyWork.DownforcePackage.LOW)
        );
        return rc;
    }

    private static Participant createParticipant(int i) {
        return new Participant(1, createCar(), createDriver(i), createPlayer(i));
    }

    private static CircuitSection createRandomSection() {
        Random r = new Random();
        return new CircuitSection(CircuitSection.CircuitSectionType.CURVE, r.nextFloat());
    }
    private static Circuit createTrack() {
        List<CircuitSection> sections = new ArrayList<>();

        for(int i = 0; i < 10; i++) {
            sections.add(createRandomSection());
        }

        return new Circuit("UML Grand Prix", 5.20f, 6, sections);
    }

    private static Race createRace() {
        List<Participant> participants = new ArrayList<>();
        Map<Participant,Boolean> p = new HashMap<>();
        for(int i = 0; i < 22; i++) {
            Participant par=createParticipant(i);
            participants.add(par);
            p.put(par,true);
        }


        return new Race(0, createAdmin(),false, new Weather(), createTrack(), participants,p);
    }

    public static Set<Integer> createRace(int n) {
        Set<Integer> s = new HashSet<>();
        for (int i=1;i<=n;i++){
            Race r = createRace();
            rdb.put(r);
            s.add(r.getId());
        }
        return s;
    }

    @BeforeEach
    public void init() {
        rdb.clear();
    }

    @Test
    public void createRaceTest() {
        Race r1 = createRace();
        Race r2 = rdb.put(r1);
        Assertions.assertEquals(r1,rdb.get(r1.getId()));
        Assertions.assertNull(rdb.get(r1.getId()+10000));
    }
    @Test
    public void isEmptyTest() {
        Assertions.assertTrue(rdb.isEmpty());
        Set<Integer> ids=createRace(10);
        Assertions.assertFalse(rdb.isEmpty());
    }
    @Test
    public void sizeTest() {
        Assertions.assertEquals(0,rdb.size());
        Set<Integer> ids=createRace(10);
        Assertions.assertEquals(10,rdb.size());
        ids=createRace(20);
        Assertions.assertEquals(30,rdb.size());
    }
    @Test
    public void containsKeyTest() {
        Set<Integer> ids=createRace(5);
        Assertions.assertFalse(rdb.containsKey(ids.stream().max(Integer::compareTo).get()+5));
        Assertions.assertTrue(rdb.containsKey(ids.stream().max(Integer::compareTo).get()));
    }

    @Test
    public void containsValueTest(){
        CombustionRaceCar rc=new CombustionRaceCar(S1Class.getInstance(), new Tyre(Tyre.TyreType.HARD),
                new CombustionEngine(Engine.EngineMode.HIGH,6000),
                new BodyWork(BodyWork.DownforcePackage.LOW)
        );
        rc.setId(1000);
        Set<Integer>ids =createRace(5);
        Integer id= ids.stream().min(Integer::compareTo).get();
        Race r = rdb.get(id);

        Assertions.assertFalse(rdb.containsValue(rc));
        Assertions.assertTrue(rdb.containsValue(r));
    }
    /*@Test
    public void removeTest() {
        Set<Integer> ids = createRace(10);
        Integer id= ids.stream().min(Integer::compareTo).get();
        Race r = rdb.get(id);
        Assertions.assertEquals(rdb.remove(r.getId()),r);
        Assertions.assertFalse(rdb.containsValue(r));
        Assertions.assertEquals(9,rdb.size());
    }

    @Test
    public void setStrategy(){
        Set<Integer> ids = createRace(10);
        Integer id= ids.stream().min(Integer::compareTo).get();
        Race r = rdb.get(id);
        v.setStrategy(Tyre.TyreType.SOFT, Engine.EngineMode.HIGH);
        Assertions.assertEquals(Tyre.TyreType.SOFT,v.getTyres().getType());
        Assertions.assertEquals(Engine.EngineMode.HIGH,v.getCombustionEngine().getMode());
        v = rdb.get(id);

        Assertions.assertEquals(Tyre.TyreType.SOFT,v.getTyres().getType());
        Assertions.assertEquals(Engine.EngineMode.HIGH,v.getCombustionEngine().getMode());

        HybridRaceCar h = new HybridRaceCar(S1Class.getInstance(),
                new Tyre(Tyre.TyreType.HARD),
                new CombustionEngine(Engine.EngineMode.HIGH,6000),
                new BodyWork(BodyWork.DownforcePackage.LOW),
                new EletricEngine(Engine.EngineMode.HIGH,100));
        rdb.put(h);
        id = h.getId();
        Assertions.assertEquals(Tyre.TyreType.HARD,h.getTyres().getType());
        Assertions.assertEquals(Engine.EngineMode.HIGH,h.getCombustionEngine().getMode());
        Assertions.assertEquals(Engine.EngineMode.HIGH,h.getEletricEngine().getMode());
        Assertions.assertEquals(BodyWork.DownforcePackage.LOW,h.getDfPackage().getDfPackage());
        h.setStrategy(Tyre.TyreType.SOFT, Engine.EngineMode.LOW);
        h= (HybridRaceCar) rdb.get(id);
        Assertions.assertEquals(Tyre.TyreType.SOFT,h.getTyres().getType());
        Assertions.assertEquals(Engine.EngineMode.LOW,h.getCombustionEngine().getMode());
        Assertions.assertEquals(Engine.EngineMode.LOW,h.getEletricEngine().getMode());
        Assertions.assertEquals(BodyWork.DownforcePackage.LOW,h.getDfPackage().getDfPackage());

    }
    @Test
    public void changeCarSetupTest(){
        Set<Integer> ids = createRaceCar(10);
        Integer id= ids.stream().min(Integer::compareTo).get();
        CombustionRaceCar v = rdb.get(id);
        v.changeCarSetup(BodyWork.DownforcePackage.HIGH);
        Assertions.assertEquals(BodyWork.DownforcePackage.HIGH,v.getDfPackage().getDfPackage());
        v = rdb.get(id);

        Assertions.assertEquals(BodyWork.DownforcePackage.HIGH,v.getDfPackage().getDfPackage());

        HybridRaceCar h = new HybridRaceCar(S1Class.getInstance(),
                new Tyre(Tyre.TyreType.HARD),
                new CombustionEngine(Engine.EngineMode.HIGH,6000),
                new BodyWork(BodyWork.DownforcePackage.LOW),
                new EletricEngine(Engine.EngineMode.HIGH,100));
        rdb.put(h);
        id = h.getId();
        Assertions.assertEquals(Tyre.TyreType.HARD,h.getTyres().getType());
        Assertions.assertEquals(Engine.EngineMode.HIGH,h.getCombustionEngine().getMode());
        Assertions.assertEquals(Engine.EngineMode.HIGH,h.getEletricEngine().getMode());
        Assertions.assertEquals(BodyWork.DownforcePackage.LOW,h.getDfPackage().getDfPackage());
        h.changeCarSetup(BodyWork.DownforcePackage.HIGH);
        h= (HybridRaceCar) rdb.get(id);
        Assertions.assertEquals(Tyre.TyreType.HARD,h.getTyres().getType());
        Assertions.assertEquals(Engine.EngineMode.HIGH,h.getCombustionEngine().getMode());
        Assertions.assertEquals(Engine.EngineMode.HIGH,h.getEletricEngine().getMode());
        Assertions.assertEquals(BodyWork.DownforcePackage.HIGH,h.getDfPackage().getDfPackage());
    }*/
}
