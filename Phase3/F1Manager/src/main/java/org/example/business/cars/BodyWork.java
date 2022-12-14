package org.example.business.cars;

import java.util.Objects;

public class BodyWork{
    @Override
    public BodyWork clone() {
        return new BodyWork(this.getDfPackage());
    }

    public static enum DownforcePackage{
        LOW,
        MEDIUM,
        HIGH
    }
    private DownforcePackage dfPackage;

    public BodyWork(DownforcePackage df){
        dfPackage=df;
    }

    public DownforcePackage getDfPackage() {return dfPackage;}

    public void setDfPackage(DownforcePackage dfPackage) {this.dfPackage = dfPackage;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BodyWork bodyWork = (BodyWork) o;
        return getDfPackage() == bodyWork.getDfPackage();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDfPackage());
    }

    @Override
    public String toString() {
        return "BodyWork{" +
                "dfPackage=" + dfPackage +
                '}';
    }
}
