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
import org.example.data.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

public class RaceTest {
    private static final RaceDAO rdb = RaceDAO.getInstance();


    public static Set<Integer> createRace(int n) {
        Set<Integer> s = new HashSet<>();

        List<String> admins = new ArrayList<>(AdminTest.createAdmin(n));

        for (int i=0;i<n;i++){
            List<Participant> parts=ParticipantTest.createParticipant(10).stream().map(str->ParticipantDAO.getInstance().get(str)).toList();
            Map<Participant,Boolean> p = new HashMap<>();
            for (Participant par:parts)
                p.put(par,false);
            Race r = new Race(
                    AdminDAO.getInstance().get(admins.get(i)),
                    false,
                    new Weather(),
                    CircuitDAO.getInstance().get("Monza"),
                    parts,
                    p
                    );
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
        Set<Integer> ids = createRace(1);
        Race r1 = rdb.get(ids.stream().min(Integer::compareTo).get());
        Assertions.assertEquals(r1,rdb.get(r1.getId()));
        Assertions.assertNull(rdb.get(r1.getId()+10000));
    }
    @Test
    public void isEmptyTest() {
        Assertions.assertTrue(rdb.isEmpty());
        Set<Integer> ids=createRace(1);
        Assertions.assertFalse(rdb.isEmpty());
    }
    @Test
    public void sizeTest() {
        int k = rdb.size();
        Assertions.assertEquals(k,rdb.size());
        Set<Integer> ids=createRace(10);
        Assertions.assertEquals(k+10,rdb.size());
    }
    @Test
    public void containsKeyTest() {
        Set<Integer> ids=createRace(1);
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
