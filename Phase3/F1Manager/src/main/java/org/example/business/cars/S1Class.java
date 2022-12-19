package org.example.business.cars;

public class S1Class extends CarClass {

    private static final float reliability= 0.95F;
    private static final String reliabilityFormula = "(1-(0.5*isHybrid))*classReliability*pilotReliability";
    private S1Class() {
        super();
    }

    private static S1Class instance;

    public static S1Class getInstance() {
        if(instance == null) {
            instance = new S1Class();
        }
        return instance;
    }
}
