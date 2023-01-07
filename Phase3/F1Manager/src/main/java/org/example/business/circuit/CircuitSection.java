package org.example.business.circuit;

import java.util.Objects;

/**
 * A section of a circuit
 *
 * @see Circuit
 */
public class CircuitSection {
    /**
     * The type of section
     */
    private CircuitSectionType sectionType;
    /**
     * The difficulty of overtaking in this particular section from 0 to 1.
     * <p>
     * 0 means it is trivial to overtake, and 1 means it is impossible to do so
     */
    private float sectionGDU;

    /**
     * Parameterized constructor
     *
     * @param sectionType the type of section
     * @param sectionGDU  the difficulty of overtaking
     */
    public CircuitSection(CircuitSectionType sectionType, float sectionGDU) {
        this.sectionType = sectionType;
        this.sectionGDU = sectionGDU;
    }

    /**
     * Copy constructor
     *
     * @param section the object to deep copy
     */
    public CircuitSection(CircuitSection section) {
        this.sectionType = section.getSectionType();
        this.sectionGDU = section.getSectionGDU();
    }

    /**
     * Gets the type of section
     *
     * @return the type of section
     */
    public CircuitSectionType getSectionType() {
        return sectionType;
    }

    /**
     * Sets the type of section
     *
     * @param sectionType the type of section
     */
    public void setSectionType(CircuitSectionType sectionType) {
        this.sectionType = sectionType;
    }

    /**
     * Gets the difficulty of overtaking in this section
     *
     * @return the difficulty of overtaking in this section
     */
    public float getSectionGDU() {
        return sectionGDU;
    }

    /**
     * Sets the difficulty of overtaking in this section
     *
     * @param sectionGDU the difficulty of overtaking in this section
     */
    public void setSectionGDU(float sectionGDU) {
        this.sectionGDU = sectionGDU;
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
    public CircuitSection clone() {
        return new CircuitSection(this);
    }

    /**
     * The different types of circuit sections
     */
    public enum CircuitSectionType {
        /**
         * A corner
         */
        CURVE,
        /**
         * A chicane
         */
        CHICANE,
        /**
         * A straight
         */
        STRAIGHT,
    }
}
