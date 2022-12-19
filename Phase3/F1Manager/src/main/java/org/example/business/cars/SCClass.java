package org.example.business.cars;

public class SCClass extends CarClass{
    private static final float reliability= 1.0F;
    private static final String reliabilityFormula = "classReliability*(0.75*pilotReliability+0.25*funçãopipi(engineCapacity)";//TODO função pipi para sc

    private SCClass() {
        super();
    }

    private static SCClass instance;

    public static SCClass getInstance() {
        if(instance == null) {
            instance = new SCClass();
        }
        return instance;
    }
}
