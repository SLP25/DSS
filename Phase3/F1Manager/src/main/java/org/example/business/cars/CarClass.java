package org.example.business.cars;

import com.udojava.evalex.Expression;

import java.math.BigDecimal;
import java.util.Map;

public abstract class CarClass {
    //static float reliability;
    private String reliabilityFormula;

    public CarClass(String reliabilityFormula) {
        this.reliabilityFormula = reliabilityFormula;
    }

    //public float getReliability() {return reliability;}

    public double calculateReliability(Map<String, Float> variables) {
        Expression formula = new Expression(this.reliabilityFormula);

        for (Map.Entry<String, Float> entry : variables.entrySet()) {
            formula = formula.with(entry.getKey(), new BigDecimal(entry.getValue()));
        }

        try {
            return formula.eval().doubleValue();
        } catch (Expression.ExpressionException e) {
            throw new RuntimeException(); //TODO:: MUDAR ISTO
        }
    }


}
