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

    private boolean isPunctured;

    @Override
    public Tyre clone() {
        return new Tyre(this.getType());
    }

    public void setType(TyreType type) {this.type = type;}

    public Tyre (TyreType t){
        type=t;
    }

    public double getTyreWearStep() {
        switch(this.type) {
            case SOFT:
                return 1.0;
            case MEDIUM:
                return 0.5;
            case HARD:
                return 0.25;
            case INTERMEDIATE:
                return 1.3;
            case WET:
                return 1.5;
            default:
                return 0.0;
        }
    }

    public boolean isPunctured() {
        return this.isPunctured;
    }

    public void setPuncture(boolean puncture) {
        this.isPunctured = puncture;
    }
    public boolean isWet() {
        return type == TyreType.WET || type == TyreType.INTERMEDIATE;
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
