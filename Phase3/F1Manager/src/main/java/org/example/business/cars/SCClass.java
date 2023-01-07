package org.example.business.cars;

public class SCClass extends CarClass {
    private static final float reliability = 1.0F;
    private static final String scReliabilityFormula = "classReliability*(0.75*pilotReliability+0.25*(engineCapacity / 7000))";//TODO função pipi para sc
    private static SCClass instance;

    private SCClass() {
        super(scReliabilityFormula);
    }

    public static SCClass getInstance() {
        if (instance == null) {
            instance = new SCClass();
        }
        return instance;
    }
}
