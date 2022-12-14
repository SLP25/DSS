package org.example.business.cars;


import java.util.Objects;

public class Tyre{

    public static enum TyreType {
        SOFT,
        MEDIUM,
        HARD,
        INTERMEDIATE,
        WET
    }
    private TyreType type;

    public TyreType getType() {return type;}

    @Override
    public Tyre clone() {
        return new Tyre(this.getType());
    }

    public void setType(TyreType type) {this.type = type;}

    public Tyre (TyreType t){
        type=t;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tyre tyre = (Tyre) o;
        return this.getType() == tyre.getType();
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
