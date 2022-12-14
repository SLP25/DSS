package org.example.business.cars;

import org.example.data.RaceCarDAO;

import java.util.Objects;

public class CombustionRaceCar {

    private Integer id;
    private Class<? extends CarClass> category;

    private Tyre tyres;

    private CombustionEngine combustionEngine;

    private BodyWork dfPackage;


    public Integer getId() {return id;}

    public void setId(Integer id) {this.id = id;}

    public Class<? extends CarClass> getCategory() {return category;}

    public void setCategory(Class<? extends CarClass> category) {this.category = category;}

    public Tyre getTyres() {return tyres;}

    public void setTyres(Tyre tyres) {this.tyres = tyres;}

    public CombustionEngine getCombustionEngine() {return combustionEngine;}

    public void setCombustionEngine(CombustionEngine combustionEngine) {this.combustionEngine = combustionEngine;}

    public BodyWork getDfPackage() {return dfPackage;}

    public void setDfPackage(BodyWork dfPackage) {this.dfPackage = dfPackage;}

    public void changeCarSetup(BodyWork.DownforcePackage df){
        this.dfPackage.setDfPackage(df);
        RaceCarDAO rdb = RaceCarDAO.getInstance();
        rdb.update(this.getId(),this);
    }


    public void setEngineMode(Engine.EngineMode engineMode){
        this.combustionEngine.setMode(engineMode);
    }
    public void setStrategy(Tyre.TyreType tyre,CombustionEngine.EngineMode engineMode){
        this.setEngineMode(engineMode);
        this.tyres.setType(tyre);
        RaceCarDAO rdb = RaceCarDAO.getInstance();
        rdb.update(this.getId(),this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CombustionRaceCar that = (CombustionRaceCar) o;
        return Objects.equals(getCategory(), that.getCategory()) && Objects.equals(getTyres(), that.getTyres()) && Objects.equals(getCombustionEngine(), that.getCombustionEngine()) && Objects.equals(getDfPackage(), that.getDfPackage());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCategory(), getTyres(), getCombustionEngine(), getDfPackage());
    }

    public CombustionRaceCar(Class<? extends CarClass> category, Tyre tyres, CombustionEngine combustionEngine, BodyWork dfPackage) {
        this.id = null;
        this.category = category;
        this.tyres = tyres;
        this.combustionEngine = combustionEngine;
        this.dfPackage = dfPackage;
    }

    public CombustionRaceCar(Integer id, Class<? extends CarClass> category, Tyre tyres, CombustionEngine combustionEngine, BodyWork dfPackage) {
        this.id = id;
        this.category = category;
        this.tyres = tyres;
        this.combustionEngine = combustionEngine;
        this.dfPackage = dfPackage;
    }
    public CombustionRaceCar(CombustionRaceCar c){
        this.id=c.getId();
        this.category = c.getCategory();
        this.tyres = c.getTyres().clone();
        this.combustionEngine=c.getCombustionEngine().clone();
        this.dfPackage = c.getDfPackage().clone();

    }
    @Override
    public CombustionRaceCar clone(){
        return new CombustionRaceCar(this);
    }
}
