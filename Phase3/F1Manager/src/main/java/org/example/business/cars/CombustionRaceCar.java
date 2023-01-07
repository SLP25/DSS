package org.example.business.cars;

import java.util.Objects;

public class CombustionRaceCar {

    private Integer id;
    private CarClass category;

    private Tyre tyres;

    private CombustionEngine combustionEngine;

    private BodyWork dfPackage;

    private double tireWear;

    private double damage;


    public CombustionRaceCar(CarClass category, Tyre tyres, CombustionEngine combustionEngine, BodyWork dfPackage) {
        this.id = null;
        this.category = category;
        this.tyres = tyres.clone();
        this.combustionEngine = combustionEngine.clone();
        this.dfPackage = dfPackage.clone();
    }

    public CombustionRaceCar(Integer id, CarClass category, Tyre tyres, CombustionEngine combustionEngine, BodyWork dfPackage) {
        this.id = id;
        this.category = category;
        this.tyres = tyres.clone();
        this.combustionEngine = combustionEngine.clone();
        this.dfPackage = dfPackage.clone();
    }

    public CombustionRaceCar(CombustionRaceCar c) {
        this.id = c.getId();
        this.category = c.getCategory();
        this.tyres = c.getTyres().clone();
        this.combustionEngine = c.getCombustionEngine().clone();
        this.dfPackage = c.getDfPackage().clone();

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CarClass getCategory() {
        return category;
    }

    public void setCategory(CarClass category) {
        this.category = category;
    }

    public Tyre getTyres() {
        return tyres.clone();
    }

    public void setTyres(Tyre tyres) {
        this.tyres = tyres.clone();
    }

    public double getDamage() {
        return damage;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

    public CombustionEngine getCombustionEngine() {
        return combustionEngine.clone();
    }

    public void setCombustionEngine(CombustionEngine combustionEngine) {
        this.combustionEngine = combustionEngine.clone();
    }

    public BodyWork getDfPackage() {
        return dfPackage.clone();
    }

    public void setDfPackage(BodyWork dfPackage) {
        this.dfPackage = dfPackage.clone();
    }

    public void changeCarSetup(BodyWork.DownforcePackage df) {
        this.dfPackage.setDfPackage(df);
    }

    public void setEngineMode(Engine.EngineMode engineMode) {
        this.combustionEngine.setMode(engineMode);
    }

    public void setStrategy(Tyre.TyreType tyre, CombustionEngine.EngineMode engineMode) {
        this.setEngineMode(engineMode);
        this.tyres.setType(tyre);
    }

    public double getTireWear() {
        return tireWear;
    }

    public void setTireWear(double wear) {
        tireWear = wear;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CombustionRaceCar that = (CombustionRaceCar) o;
        return Objects.equals(getCategory().getClass(), that.getCategory().getClass()) && Objects.equals(getTyres(), that.getTyres()) && Objects.equals(getCombustionEngine(), that.getCombustionEngine()) && Objects.equals(getDfPackage(), that.getDfPackage());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCategory(), getTyres(), getCombustionEngine(), getDfPackage());
    }

    @Override
    public CombustionRaceCar clone() {
        return new CombustionRaceCar(this);
    }
}
