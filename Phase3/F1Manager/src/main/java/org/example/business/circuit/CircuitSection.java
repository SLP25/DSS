package org.example.business.circuit;

import java.util.Objects;

public class CircuitSection {
    public enum CircuitSectionType{
        CURVE,
        CHICANE,
        STRAIGHT,
    }
    private CircuitSectionType sectionType;
    private float sectionGDU;

    public CircuitSectionType getSectionType() {
        return sectionType;
    }

    public void setSectionType(CircuitSectionType sectionType) {
        this.sectionType = sectionType;
    }

    public float getSectionGDU() {
        return sectionGDU;
    }

    public void setSectionGDU(float sectionGDU) {
        this.sectionGDU = sectionGDU;
    }

    public CircuitSection(CircuitSectionType sectionType, float sectionGDU) {
        this.sectionType = sectionType;
        this.sectionGDU = sectionGDU;
    }
    public CircuitSection(CircuitSection section) {
        this.sectionType = section.getSectionType();
        this.sectionGDU = section.getSectionGDU();
    }

    @Override
    public String toString() {
        return "CircuitSection{" +
                "sectionType=" + sectionType +
                ", sectionGDU=" + sectionGDU +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CircuitSection that = (CircuitSection) o;
        return Float.compare(that.getSectionGDU(), getSectionGDU()) == 0 && getSectionType() == that.getSectionType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSectionType(), getSectionGDU());
    }

    @Override
    public CircuitSection clone(){
        return new CircuitSection(this);
    }
}
