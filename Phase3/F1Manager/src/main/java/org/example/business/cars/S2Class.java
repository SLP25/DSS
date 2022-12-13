package org.example.business.cars;

public class S2Class extends CarClass{

    private static final float reliability= 0.80F;
    private static final String reliabilityFormula = "(1-(0.5*isHybrid))*classReliability*pilotReliability*tyresReliability*bodyReliability*engineReliability*FunçãoPipi(engineCapacity)";//TODO ADICIONAR FUNÇÂO PIPI Q LIGA CILINDRADA A reliability
    public S2Class() {
        super();
    }
}