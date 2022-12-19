package org.example.business.cars;

import java.util.Objects;

public class HybridRaceCar extends CombustionRaceCar{
    private EletricEngine eletricEngine;

    public EletricEngine getEletricEngine() {return eletricEngine.clone();}

    public void setEletricEngine(EletricEngine eletricEngine) {this.eletricEngine = eletricEngine.clone();}

    public HybridRaceCar(CarClass category, Tyre tyres, CombustionEngine combustionEngine, BodyWork dfPackage, EletricEngine eletricEngine) {
        super(category, tyres, combustionEngine, dfPackage);
        this.eletricEngine = eletricEngine.clone();
    }
    public HybridRaceCar(Integer id, CarClass category, Tyre tyres, CombustionEngine combustionEngine, BodyWork dfPackage,EletricEngine eletricEngine) {
        super(id, category, tyres, combustionEngine, dfPackage);
        this.eletricEngine = eletricEngine.clone();
    }

    @Override
    public void setEngineMode(Engine.EngineMode engineMode){
        super.setEngineMode(engineMode);
        this.eletricEngine.setMode(engineMode);
    }

    public HybridRaceCar(CombustionRaceCar c,EletricEngine eletricEngine){
        super(c);
        this.eletricEngine=eletricEngine.clone();
    }
}
