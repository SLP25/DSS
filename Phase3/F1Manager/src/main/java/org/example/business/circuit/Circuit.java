package org.example.business.circuit;

import java.util.ArrayList;
import java.util.List;

public class Circuit {
    private String name;
    private float circuitLength;
    private int numberOfLaps;
    ArrayList<CircuitSection> circuitSections;

    public Circuit(String name, float circuitLength, int numberOfLaps, List<CircuitSection> circuitSections) {
        this.name = name;
        this.circuitLength = circuitLength;
        this.numberOfLaps = numberOfLaps;
        this.circuitSections = new ArrayList<>();
        for (CircuitSection c:circuitSections)
            this.circuitSections.add(c.clone());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getCircuitLength() {
        return circuitLength;
    }

    public void setCircuitLength(float circuitLength) {
        this.circuitLength = circuitLength;
    }

    public int getNumberOfLaps() {
        return numberOfLaps;
    }

    public void setNumberOfLaps(int numberOfLaps) {
        this.numberOfLaps = numberOfLaps;
    }

    public ArrayList<CircuitSection> getCircuitSections() {
        ArrayList<CircuitSection> t=new ArrayList<>();
        for (CircuitSection c:circuitSections)
            t.add(c.clone());
        return t;
    }

    public void setCircuitSections(ArrayList<CircuitSection> circuitSections) {
        this.circuitSections = new ArrayList<>();
        for (CircuitSection c:circuitSections)
            this.circuitSections.add(c.clone());
    }
}
