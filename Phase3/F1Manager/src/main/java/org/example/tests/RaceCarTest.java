package org.example.tests;

import org.example.business.cars.*;
import org.example.data.RaceCarDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

public class RaceCarTest {
    private static final RaceCarDAO rdb = RaceCarDAO.getInstance();

    public static Set<Integer> createRaceCar(int n) {
        Set<Integer> s = new HashSet<>();
        for (int i=1;i<=n;i++){
            CombustionRaceCar u = new CombustionRaceCar(new S1Class(),new Tyre(Tyre.TyreType.HARD),
                    new CombustionEngine(Engine.EngineMode.HIGH,6000),
                    new BodyWork(BodyWork.DownforcePackage.LOW)
            );
            rdb.put(u);
            s.add(u.getId());
        }
        return s;
    }

    @BeforeEach
    public void init() {
        rdb.clear();
    }

    @Test
    public void createRaceCarTest() {
        CombustionRaceCar rc=new CombustionRaceCar(new S1Class(),new Tyre(Tyre.TyreType.HARD),
                new CombustionEngine(Engine.EngineMode.HIGH,6000),
                new BodyWork(BodyWork.DownforcePackage.LOW)
        );
        CombustionRaceCar c = rdb.put(rc);
        Assertions.assertEquals(rc,rdb.get(c.getId()));
        Assertions.assertNull(rdb.get(rc.getId()+10000));
    }
    @Test
    public void isEmptyTest() {
        Assertions.assertTrue(rdb.isEmpty());
        Set<Integer> ids=createRaceCar(10);
        Assertions.assertFalse(rdb.isEmpty());
    }
    @Test
    public void sizeTest() {
        Assertions.assertEquals(0,rdb.size());
        Set<Integer> ids=createRaceCar(10);
        Assertions.assertEquals(10,rdb.size());
        ids=createRaceCar(20);
        Assertions.assertEquals(30,rdb.size());
    }
    @Test
    public void containsKeyTest() {
        Set<Integer> ids=createRaceCar(5);
        Assertions.assertFalse(rdb.containsKey(ids.stream().max(Integer::compareTo).get()+5));
        Assertions.assertTrue(rdb.containsKey(ids.stream().max(Integer::compareTo).get()));
    }

    @Test
    public void containsValueTest(){
        CombustionRaceCar rc=new CombustionRaceCar(new S1Class(), new Tyre(Tyre.TyreType.HARD),
                new CombustionEngine(Engine.EngineMode.HIGH,6000),
                new BodyWork(BodyWork.DownforcePackage.LOW)
              );
        rc.setId(1000);
        Set<Integer>ids =createRaceCar(5);
        Integer id= ids.stream().min(Integer::compareTo).get();
        CombustionRaceCar v = rdb.get(id);

        Assertions.assertFalse(rdb.containsValue(rc));
        Assertions.assertTrue(rdb.containsValue(v));
    }
    @Test
    public void removeTest() {
        Set<Integer> ids = createRaceCar(10);
        Integer id= ids.stream().min(Integer::compareTo).get();
        CombustionRaceCar v = rdb.get(id);
        Assertions.assertEquals(rdb.remove(v.getId()),v);
        Assertions.assertFalse(rdb.containsValue(v));
        Assertions.assertEquals(9,rdb.size());
    }

    @Test
    public void setStrategy(){
        Set<Integer> ids = createRaceCar(10);
        Integer id= ids.stream().min(Integer::compareTo).get();
        CombustionRaceCar v = rdb.get(id);
        v.setStrategy(Tyre.TyreType.SOFT, Engine.EngineMode.HIGH);
        Assertions.assertEquals(Tyre.TyreType.SOFT,v.getTyres().getType());
        Assertions.assertEquals(Engine.EngineMode.HIGH,v.getCombustionEngine().getMode());
        v = rdb.get(id);

        Assertions.assertEquals(Tyre.TyreType.SOFT,v.getTyres().getType());
        Assertions.assertEquals(Engine.EngineMode.HIGH,v.getCombustionEngine().getMode());

        HybridRaceCar h = new HybridRaceCar(new S1Class(),
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

        HybridRaceCar h = new HybridRaceCar(new S1Class(),
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
    }

}