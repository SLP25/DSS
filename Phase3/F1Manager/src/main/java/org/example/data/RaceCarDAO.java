package org.example.data;

import org.example.business.cars.*;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class RaceCarDAO implements Map<Integer, CombustionRaceCar> {
    private static RaceCarDAO singleton = null;
    private Map<Integer, CombustionRaceCar> cars = Map.ofEntries(
            Map.entry(1, new HybridRaceCar(1, S1Class.getInstance(), new Tyre(Tyre.TyreType.SOFT), new CombustionEngine(Engine.EngineMode.HIGH, 6000), new BodyWork(BodyWork.DownforcePackage.LOW), new EletricEngine(Engine.EngineMode.HIGH, 500))),
            Map.entry(2, new HybridRaceCar(2, S1Class.getInstance(), new Tyre(Tyre.TyreType.HARD), new CombustionEngine(Engine.EngineMode.LOW, 6000), new BodyWork(BodyWork.DownforcePackage.HIGH), new EletricEngine(Engine.EngineMode.LOW, 600))),
            Map.entry(3, new CombustionRaceCar(3, S1Class.getInstance(), new Tyre(Tyre.TyreType.SOFT), new CombustionEngine(Engine.EngineMode.HIGH, 6000), new BodyWork(BodyWork.DownforcePackage.HIGH))),
            Map.entry(4, new HybridRaceCar(4, S2Class.getInstance(), new Tyre(Tyre.TyreType.SOFT), new CombustionEngine(Engine.EngineMode.HIGH, 4000), new BodyWork(BodyWork.DownforcePackage.LOW), new EletricEngine(Engine.EngineMode.HIGH, 500))),
            Map.entry(5, new HybridRaceCar(5, S2Class.getInstance(), new Tyre(Tyre.TyreType.HARD), new CombustionEngine(Engine.EngineMode.LOW, 3000), new BodyWork(BodyWork.DownforcePackage.HIGH), new EletricEngine(Engine.EngineMode.LOW, 600))),
            Map.entry(6, new CombustionRaceCar(6, S2Class.getInstance(), new Tyre(Tyre.TyreType.SOFT), new CombustionEngine(Engine.EngineMode.HIGH, 5000), new BodyWork(BodyWork.DownforcePackage.HIGH))),
            Map.entry(7, new HybridRaceCar(7, GTClass.getInstance(), new Tyre(Tyre.TyreType.SOFT), new CombustionEngine(Engine.EngineMode.HIGH, 2000), new BodyWork(BodyWork.DownforcePackage.LOW), new EletricEngine(Engine.EngineMode.HIGH, 500))),
            Map.entry(8, new HybridRaceCar(8, GTClass.getInstance(), new Tyre(Tyre.TyreType.HARD), new CombustionEngine(Engine.EngineMode.LOW, 3000), new BodyWork(BodyWork.DownforcePackage.HIGH), new EletricEngine(Engine.EngineMode.LOW, 600))),
            Map.entry(9, new CombustionRaceCar(9, GTClass.getInstance(), new Tyre(Tyre.TyreType.SOFT), new CombustionEngine(Engine.EngineMode.HIGH, 4000), new BodyWork(BodyWork.DownforcePackage.HIGH))),
            Map.entry(10, new CombustionRaceCar(10, SCClass.getInstance(), new Tyre(Tyre.TyreType.SOFT), new CombustionEngine(Engine.EngineMode.HIGH, 2500), new BodyWork(BodyWork.DownforcePackage.LOW))),
            Map.entry(11, new CombustionRaceCar(11, SCClass.getInstance(), new Tyre(Tyre.TyreType.HARD), new CombustionEngine(Engine.EngineMode.LOW, 2500), new BodyWork(BodyWork.DownforcePackage.HIGH))),
            Map.entry(12, new CombustionRaceCar(12, SCClass.getInstance(), new Tyre(Tyre.TyreType.SOFT), new CombustionEngine(Engine.EngineMode.HIGH, 2500), new BodyWork(BodyWork.DownforcePackage.HIGH))),
            Map.entry(13, new HybridRaceCar(13, S1Class.getInstance(), new Tyre(Tyre.TyreType.SOFT), new CombustionEngine(Engine.EngineMode.HIGH, 6000), new BodyWork(BodyWork.DownforcePackage.LOW), new EletricEngine(Engine.EngineMode.HIGH, 500))),
            Map.entry(14, new HybridRaceCar(14, S1Class.getInstance(), new Tyre(Tyre.TyreType.SOFT), new CombustionEngine(Engine.EngineMode.HIGH, 6000), new BodyWork(BodyWork.DownforcePackage.LOW), new EletricEngine(Engine.EngineMode.HIGH, 500))),
            Map.entry(15, new HybridRaceCar(15, S1Class.getInstance(), new Tyre(Tyre.TyreType.SOFT), new CombustionEngine(Engine.EngineMode.HIGH, 6000), new BodyWork(BodyWork.DownforcePackage.LOW), new EletricEngine(Engine.EngineMode.HIGH, 500))),
            Map.entry(16, new HybridRaceCar(16, S1Class.getInstance(), new Tyre(Tyre.TyreType.SOFT), new CombustionEngine(Engine.EngineMode.HIGH, 6000), new BodyWork(BodyWork.DownforcePackage.LOW), new EletricEngine(Engine.EngineMode.HIGH, 500))),
            Map.entry(17, new HybridRaceCar(17, S1Class.getInstance(), new Tyre(Tyre.TyreType.SOFT), new CombustionEngine(Engine.EngineMode.HIGH, 6000), new BodyWork(BodyWork.DownforcePackage.LOW), new EletricEngine(Engine.EngineMode.HIGH, 500))),
            Map.entry(18, new HybridRaceCar(18, S1Class.getInstance(), new Tyre(Tyre.TyreType.SOFT), new CombustionEngine(Engine.EngineMode.HIGH, 6000), new BodyWork(BodyWork.DownforcePackage.LOW), new EletricEngine(Engine.EngineMode.HIGH, 500))),
            Map.entry(19, new HybridRaceCar(19, S1Class.getInstance(), new Tyre(Tyre.TyreType.SOFT), new CombustionEngine(Engine.EngineMode.HIGH, 6000), new BodyWork(BodyWork.DownforcePackage.LOW), new EletricEngine(Engine.EngineMode.HIGH, 500))),
            Map.entry(20, new HybridRaceCar(20, S1Class.getInstance(), new Tyre(Tyre.TyreType.SOFT), new CombustionEngine(Engine.EngineMode.HIGH, 6000), new BodyWork(BodyWork.DownforcePackage.LOW), new EletricEngine(Engine.EngineMode.HIGH, 500))),
            Map.entry(21, new HybridRaceCar(21, S1Class.getInstance(), new Tyre(Tyre.TyreType.SOFT), new CombustionEngine(Engine.EngineMode.HIGH, 6000), new BodyWork(BodyWork.DownforcePackage.LOW), new EletricEngine(Engine.EngineMode.HIGH, 500))),
            Map.entry(22, new HybridRaceCar(22, S1Class.getInstance(), new Tyre(Tyre.TyreType.SOFT), new CombustionEngine(Engine.EngineMode.HIGH, 6000), new BodyWork(BodyWork.DownforcePackage.LOW), new EletricEngine(Engine.EngineMode.HIGH, 500))),
            Map.entry(23, new HybridRaceCar(23, S1Class.getInstance(), new Tyre(Tyre.TyreType.SOFT), new CombustionEngine(Engine.EngineMode.HIGH, 6000), new BodyWork(BodyWork.DownforcePackage.LOW), new EletricEngine(Engine.EngineMode.HIGH, 500))),
            Map.entry(24, new HybridRaceCar(24, S1Class.getInstance(), new Tyre(Tyre.TyreType.SOFT), new CombustionEngine(Engine.EngineMode.HIGH, 6000), new BodyWork(BodyWork.DownforcePackage.LOW), new EletricEngine(Engine.EngineMode.HIGH, 500))),
            Map.entry(25, new HybridRaceCar(25, S1Class.getInstance(), new Tyre(Tyre.TyreType.SOFT), new CombustionEngine(Engine.EngineMode.HIGH, 6000), new BodyWork(BodyWork.DownforcePackage.LOW), new EletricEngine(Engine.EngineMode.HIGH, 500)))
    );

    /**
     * implementation of the singleton pattern
     *
     * @return returns the only instance of the class
     */
    public static RaceCarDAO getInstance() {
        if (RaceCarDAO.singleton == null) {
            RaceCarDAO.singleton = new RaceCarDAO();
        }
        return RaceCarDAO.singleton;
    }

    /**
     * @return number of cars in the system
     */
    @Override
    public int size() {
        return cars.size();
    }

    /**
     * Method that verifies the existence of cars
     *
     * @return true if the number of cars is 0
     */
    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }


    @Override
    public boolean containsKey(Object key) {
        return cars.containsKey(key);
    }


    @Override
    public boolean containsValue(Object value) {
        return cars.containsValue(value);
    }

    /**
     * Gets a user given a username
     *
     * @param key username of the user
     * @return the user if exists (null otherwise)
     * @throws NullPointerException //TODO MUDAR ISTO
     */
    @Override
    public CombustionRaceCar get(Object key) {
        return cars.get(key).clone();
    }


    @Override
    public CombustionRaceCar put(Integer key, CombustionRaceCar car) {
        CombustionRaceCar c = cars.put(key, car.clone());
        if (c == null) return null;
        return c.clone();
    }

    public CombustionRaceCar put(CombustionRaceCar u) {
        return put(u.getId(), u);
    }

    @Override
    public CombustionRaceCar remove(Object key) {
        CombustionRaceCar c = cars.remove(key);
        if (c == null) return null;
        return c.clone();
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends CombustionRaceCar> m) {
        cars.putAll(m.entrySet().stream().collect(Collectors.toMap(Entry::getKey, e -> e.getValue().clone())));
    }


    @Override
    public void clear() {
        cars.clear();
    }

    @Override
    public Set<Integer> keySet() {
        return cars.keySet();
    }

    @Override
    public Collection<CombustionRaceCar> values() {
        return cars.values().stream().map(CombustionRaceCar::clone).collect(Collectors.toList());
    }

    @Override
    public Set<Entry<Integer, CombustionRaceCar>> entrySet() {
        return values().stream().collect(
                Collectors.toMap(CombustionRaceCar::getId, x -> x)).entrySet();
    }
}
