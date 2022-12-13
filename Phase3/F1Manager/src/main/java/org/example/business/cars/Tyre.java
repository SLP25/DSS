package org.example.business;


public class Tyre extends CarPart{

    public static enum tyreType {
        SOFT,
        MEDIUM,
        HARD,
        INTERMEDIATE,
        WET
    }
    private tyreType type;

    public tyreType getType() {return type;}

    public void setType(tyreType type) {this.type = type;}

    public Tyre (tyreType t){type=t;}

}
