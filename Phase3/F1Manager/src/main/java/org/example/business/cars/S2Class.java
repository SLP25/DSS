package org.example.business.cars;

public class S2Class extends CarClass {

    private static final float reliability = 0.80F;
    private static final String s2ReliabilityFormula = "(1-(0.5*isHybrid))*classReliability*pilotReliability*(engineCapacity / 7000)";//TODO ADICIONAR FUNÇÂO PIPI Q LIGA CILINDRADA A reliability
    private static S2Class instance;

    private S2Class() {
        super(s2ReliabilityFormula);
    }

    public static S2Class getInstance() {
        if (instance == null) {
            instance = new S2Class();
        }
        return instance;
    }
}