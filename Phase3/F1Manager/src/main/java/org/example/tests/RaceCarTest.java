package org.example.tests;

import org.example.business.cars.*;
import org.example.business.users.Player;
import org.example.data.PlayerDAO;
import org.example.data.RaceCarDAO;
import org.example.exceptions.cars.EngineCannotBeEletricAndCombustionSimultaneousException;
import org.example.exceptions.cars.EngineCannotHaveCapacityAndNotBeCombustionException;
import org.example.exceptions.cars.EngineCannotHavePowerAndNotBeElectricException;

import java.util.HashSet;
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

}
