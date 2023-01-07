package org.example.business.circuit;

import java.util.ArrayList;
import java.util.List;

/**
 * A circuit where races can be held at
 */
public class Circuit {
    List<CircuitSection> circuitSections;
    /**
     * The name of the circuit
     */
    private String name;
    /**
     * The length of the circuit
     */
    private float circuitLength;
    /**
     * The number of laps a race at this circuit must have
     */
    private int numberOfLaps;

    /**
     * Parameterized constructor
     *
     * @param name            the name of the circuit
     * @param circuitLength   the length of the circuit
     * @param numberOfLaps    the number of laps a race at this circuit must have
     * @param circuitSections the list of all sections that make up the track
     * @see CircuitSection
     */
    public Circuit(String name, float circuitLength, int numberOfLaps, List<CircuitSection> circuitSections) {
        this.name = name;
        this.circuitLength = circuitLength;
        this.numberOfLaps = numberOfLaps;
        this.circuitSections = new ArrayList<>();
        for (CircuitSection c : circuitSections)
            this.circuitSections.add(c.clone());
    }

    /**
     * Gets the name of the circuit
     *
     * @return the name of the circuit
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the circuit
     *
     * @param name the name of the circuit
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the length of the circuit
     *
     * @return the length of the circuit
     */
    public float getCircuitLength() {
        return circuitLength;
    }

    /**
     * Sets the length of the circuit
     *
     * @param circuitLength the length of the circuit
     */
    public void setCircuitLength(float circuitLength) {
        this.circuitLength = circuitLength;
    }

    /**
     * Gets the number of laps of a race at this circuit
     *
     * @return the number of laps of a race at this circuit
     */
    public int getNumberOfLaps() {
        return numberOfLaps;
    }

    /**
     * Sets the number of laps of a race at this circuit
     *
     * @param numberOfLaps the number of laps of a race at this circuit
     */
    public void setNumberOfLaps(int numberOfLaps) {
        this.numberOfLaps = numberOfLaps;
    }

    /**
     * Gets the sections that make up the track
     *
     * @return the sections that make up the track
     * @implNote the sections returned are deep copies of those
     * stored inside the instance
     * @see CircuitSection
     */
    public List<CircuitSection> getCircuitSections() {
        List<CircuitSection> t = new ArrayList<>();
        for (CircuitSection c : circuitSections)
            t.add(c.clone());
        return t;
    }

    /**
     * Sets the sections that make up the track
     *
     * @return the sections that make up the track
     * @implNote the sections stored are deep copies of those
     * stored inside the instance
     * @see CircuitSection
     */
    public void setCircuitSections(ArrayList<CircuitSection> circuitSections) {
        this.circuitSections = new ArrayList<>();
        for (CircuitSection c : circuitSections)
            this.circuitSections.add(c.clone());
    }
}
