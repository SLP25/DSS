package org.example.business;

public class BodyWork extends CarPart{
    public static enum DownforcePackage{
        LOW,
        MEDIUM,
        HIGH
    }
    private DownforcePackage dfPackage;

    public BodyWork(DownforcePackage df){dfPackage=df;}

    public DownforcePackage getDfPackage() {return dfPackage;}

    public void setDfPackage(DownforcePackage dfPackage) {this.dfPackage = dfPackage;}
}
