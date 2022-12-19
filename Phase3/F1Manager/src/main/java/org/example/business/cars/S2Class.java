package org.example.business.cars;

public class S2Class extends CarClass{

    private static final float reliability= 0.80F;
    private static final String reliabilityFormula = "(1-(0.5*isHybrid))*classReliability*pilotReliability*tyresReliability*bodyReliability*engineReliability*FunçãoPipi(engineCapacity)";//TODO ADICIONAR FUNÇÂO PIPI Q LIGA CILINDRADA A reliability
    private S2Class() {
        super();
    }

    private static S2Class instance;

    public static S2Class getInstance() {
        if(instance == null) {
            instance = new S2Class();
        }
        return instance;
    }
}