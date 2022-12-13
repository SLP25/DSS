package org.example.business.cars;


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
        type=t;}

}
