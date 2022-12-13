package org.example.tests;

import org.example.business.cars.*;
import org.example.business.users.Admin;
import org.example.business.users.Player;
import org.example.data.PlayerDAO;
import org.example.data.RaceCarDAO;
import org.example.exceptions.cars.EngineCannotBeEletricAndCombustionSimultaneousException;
import org.example.exceptions.cars.EngineCannotHaveCapacityAndNotBeCombustionException;
import org.example.exceptions.cars.EngineCannotHavePowerAndNotBeElectricException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RaceCarTest {
    private final RaceCarDAO rdb = RaceCarDAO.getInstance();

    private Set<Integer> createRaceCar(int n) throws EngineCannotHaveCapacityAndNotBeCombustionException, EngineCannotBeEletricAndCombustionSimultaneousException, EngineCannotHavePowerAndNotBeElectricException {
        Set<Integer> s = new HashSet<>();
        for (int i=1;i<=n;i++){
            RaceCar u = new RaceCar(S1Class.class,new Tyre(Tyre.TyreType.HARD),
                    new BodyWork(BodyWork.DownforcePackage.LOW),
                    new Engine(6000,0, Engine.EngineMode.HIGH, CarPart.CarPartType.COMBUSTION_ENGINE),
                    null
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
    public void createRaceCarTest() throws EngineCannotHaveCapacityAndNotBeCombustionException, EngineCannotBeEletricAndCombustionSimultaneousException, EngineCannotHavePowerAndNotBeElectricException {
        RaceCar rc=new RaceCar(S1Class.class,new Tyre(Tyre.TyreType.HARD),
                new BodyWork(BodyWork.DownforcePackage.LOW),
                new Engine(6000,0, Engine.EngineMode.HIGH, CarPart.CarPartType.COMBUSTION_ENGINE),
                null
        );
        RaceCar c = rdb.put(rc);
        Assertions.assertEquals(rc,rdb.get(c.getId()));
        Assertions.assertNull(rdb.get(rc.getId()+10000));
    }
    @Test
    public void isEmptyTest() throws EngineCannotHaveCapacityAndNotBeCombustionException, EngineCannotBeEletricAndCombustionSimultaneousException, EngineCannotHavePowerAndNotBeElectricException {
        Assertions.assertTrue(rdb.isEmpty());
        Set<Integer> ids=createRaceCar(10);
        Assertions.assertFalse(rdb.isEmpty());
    }
    @Test
    public void sizeTest() throws EngineCannotHaveCapacityAndNotBeCombustionException, EngineCannotBeEletricAndCombustionSimultaneousException, EngineCannotHavePowerAndNotBeElectricException {
        Assertions.assertEquals(0,rdb.size());
        Set<Integer> ids=createRaceCar(10);
        Assertions.assertEquals(10,rdb.size());
        ids=createRaceCar(20);
        Assertions.assertEquals(30,rdb.size());
    }
    @Test
    public void containsKeyTest() throws EngineCannotHaveCapacityAndNotBeCombustionException, EngineCannotBeEletricAndCombustionSimultaneousException, EngineCannotHavePowerAndNotBeElectricException {
        Set<Integer> ids=createRaceCar(5);
        Assertions.assertFalse(rdb.containsKey(ids.stream().max(Integer::compareTo).get()+5));
        Assertions.assertTrue(rdb.containsKey(ids.stream().max(Integer::compareTo).get()));
    }

    @Test
    public void containsValueTest() throws EngineCannotHaveCapacityAndNotBeCombustionException, EngineCannotBeEletricAndCombustionSimultaneousException, EngineCannotHavePowerAndNotBeElectricException {
        RaceCar rc=new RaceCar(S1Class.class,new Tyre(Tyre.TyreType.HARD),
                new BodyWork(BodyWork.DownforcePackage.LOW),
                new Engine(6000,0, Engine.EngineMode.HIGH, CarPart.CarPartType.COMBUSTION_ENGINE),
                null);
        rc.setId(1000);
        Set<Integer>ids =createRaceCar(5);
        Integer id= ids.stream().min(Integer::compareTo).get();
        RaceCar v = rdb.get(id);

        Assertions.assertFalse(rdb.containsValue(rc));
        Assertions.assertTrue(rdb.containsValue(v));
    }
    @Test
    public void removeTest() throws EngineCannotHaveCapacityAndNotBeCombustionException, EngineCannotBeEletricAndCombustionSimultaneousException, EngineCannotHavePowerAndNotBeElectricException {
        Set<Integer> ids = createRaceCar(10);
        Integer id= ids.stream().min(Integer::compareTo).get();
        RaceCar v = rdb.get(id);
        Assertions.assertEquals(rdb.remove(v.getId()),v);
        Assertions.assertFalse(rdb.containsValue(v));
        Assertions.assertEquals(9,rdb.size());
    }

    @Test
    public void setStrategy() throws EngineCannotHaveCapacityAndNotBeCombustionException, EngineCannotBeEletricAndCombustionSimultaneousException, EngineCannotHavePowerAndNotBeElectricException {
        Set<Integer> ids = createRaceCar(10);
        Integer id= ids.stream().min(Integer::compareTo).get();
        RaceCar v = rdb.get(id);
        v.setStrategy(Tyre.TyreType.SOFT, Engine.EngineMode.HIGH);
        Map<CarPart.CarPartType,CarPart>parts=v.getParts();
        Assertions.assertEquals(Tyre.TyreType.SOFT,((Tyre)parts.get(CarPart.CarPartType.TYRE)).getTyreType());
        Assertions.assertEquals(Engine.EngineMode.HIGH,((Engine)parts.get(CarPart.CarPartType.COMBUSTION_ENGINE)).getMode());
        v = rdb.get(id);
        parts=v.getParts();

        Assertions.assertEquals(Tyre.TyreType.SOFT,((Tyre)parts.get(CarPart.CarPartType.TYRE)).getTyreType());
        Assertions.assertEquals(Engine.EngineMode.HIGH,((Engine)parts.get(CarPart.CarPartType.COMBUSTION_ENGINE)).getMode());
    }

}