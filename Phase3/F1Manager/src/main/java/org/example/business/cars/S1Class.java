package org.example.business.cars;

public class S1Class extends CarClass{

    private static final float reliability= 0.95F;
    private static final String reliabilityFormula = "(1-(0.5*isHybrid))*classReliability*pilotReliability";
    public S1Class() {
        super();
    }
}
