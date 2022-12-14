package org.example.business.cars;

import java.util.Objects;

public class EletricEngine extends Engine{
    private int power;

    public int getPower() {return power;}

    public void setPower(int power) {this.power = power;}

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EletricEngine that = (EletricEngine) o;
        return super.equals(o) && getPower() == that.getPower();
    }

    @Override
    public int hashCode() {return Objects.hash(getPower());}

    public EletricEngine(EngineMode mode, int power) {
        super(mode);
        this.power=power;
    }

    @Override
    public String toString() {
        return "EletricEngine{" +
                "power=" + power +
                "mode="+  getMode().name() +
                '}';
    }
    @Override
    public EletricEngine clone(){
        return new EletricEngine(this.getMode(),this.getPower());
    }
}
