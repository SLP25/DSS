package org.example.business.cars;

public class GTClass extends CarClass{

    private static final float reliability= 1.0F;
    private static final String gtReliabilityFormula = "(1-(0.5*isHybrid))*classReliability*(engineCapacity/7000)*(1/numberOfLaps)";//TODO função pipi para gt
    private GTClass() {
        super(gtReliabilityFormula);
    }

    private static GTClass instance;

    public static GTClass getInstance() {
        if(instance == null) {
            instance = new GTClass();
        }
        return instance;
    }
}