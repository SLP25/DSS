package org.example.business.cars;

import org.example.exceptions.cars.EngineCannotBeEletricAndCombustionSimultaneousException;
import org.example.exceptions.cars.EngineCannotHaveCapacityAndNotBeCombustionException;
import org.example.exceptions.cars.EngineCannotHavePowerAndNotBeElectricException;

public class Engine extends CarPart{



    @Override
    public Engine clone() {
        return new Engine(this);
    }

    public static enum EngineMode{
        LOW,
        MEDIUM,
        HIGH
    }

    private int capacity;
    private int power;

    private EngineMode mode;

    public int getCapacity() {return capacity;}

    public void setCapacity(int capacity) {this.capacity = capacity;}

    public int getPower() {return power;}

    public void setPower(int power) {this.power = power;}

    public EngineMode getMode() {return mode;}

    public void setMode(EngineMode mode) {this.mode = mode;}

    public Engine(Engine e){
        super(e.getType());
        capacity=e.getCapacity();
        power=e.getPower();
        mode=e.getMode();
    }

    public Engine(int cap,int pow,EngineMode mod,CarPart.CarPartType type) throws EngineCannotBeEletricAndCombustionSimultaneousException, EngineCannotHaveCapacityAndNotBeCombustionException, EngineCannotHavePowerAndNotBeElectricException {
        super(type);
        if (cap!=0 && pow!=0){
            throw new EngineCannotBeEletricAndCombustionSimultaneousException();
        }
        if (cap!=0 && type!=CarPartType.COMBUSTION_ENGINE){
            throw new EngineCannotHaveCapacityAndNotBeCombustionException();
        }
        if (pow!=0 && type!=CarPartType.ELECTRIC_ENGINE){
            throw new EngineCannotHavePowerAndNotBeElectricException();
        }
        capacity=cap;
        power=pow;
        mode=mod;
    }
}
