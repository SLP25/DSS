package org.example.business.cars;


import java.util.Objects;

public class Tyre extends CarPart{

    public static enum TyreType {
        SOFT,
        MEDIUM,
        HARD,
        INTERMEDIATE,
        WET
    }
    private TyreType type;

    public TyreType getTyreType() {return type;}

    @Override
    public CarPart clone() {
        return new Tyre(this.getTyreType());
    }

    public void setType(TyreType type) {this.type = type;}

    public Tyre (TyreType t){
        super(CarPartType.TYRE);
        type=t;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tyre tyre = (Tyre) o;
        return getType() == tyre.getType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getType());
    }

    @Override
    public String toString() {
        return "Tyre{" +
                "type=" + type +
                '}';
    }
}
