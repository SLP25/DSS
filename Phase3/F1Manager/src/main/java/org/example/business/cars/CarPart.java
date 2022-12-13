package org.example.business.cars;

public abstract class CarPart {

    public static enum CarPartType {
        ELECTRIC_ENGINE,
        COMBUSTION_ENGINE,
        TYRE,
        BODYWORK
    }

    public CarPartType getType() {return type;}

    public void setType(CarPartType type) {this.type = type;}

    private CarPartType type;
    private float damage;

    public abstract CarPart clone();

    public float getDamage() {return damage;}

    public void setDamage(float damage) {this.damage = damage;}

    public CarPart(CarPartType type){
        damage=0;
        this.type=type;
    }
}
