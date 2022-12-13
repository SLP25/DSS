package org.example.business.cars;

public class BodyWork extends CarPart{
    @Override
    public CarPart clone() {
        return new BodyWork(this.getDfPackage());
    }

    public static enum DownforcePackage{
        LOW,
        MEDIUM,
        HIGH
    }
    private DownforcePackage dfPackage;

    public BodyWork(DownforcePackage df){
        super(CarPartType.BODYWORK);
        dfPackage=df;
    }

    public DownforcePackage getDfPackage() {return dfPackage;}

    public void setDfPackage(DownforcePackage dfPackage) {this.dfPackage = dfPackage;}
}
