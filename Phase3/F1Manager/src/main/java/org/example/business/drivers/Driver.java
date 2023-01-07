package org.example.business.drivers;

import java.util.Objects;

public class Driver {

    private String driverName;
    private float driverCTS;
    private float driverSVA;

    public Driver(String driverName, float driverCTS, float driverSVA) {

        this.driverName = driverName;
        this.driverCTS = driverCTS;
        this.driverSVA = driverSVA;

    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public float getDriverCTS() {
        return driverCTS;
    }

    public void setDriverCTS(float driverCTS) {
        this.driverCTS = driverCTS;
    }

    public float getDriverSVA() {
        return driverSVA;
    }

    public void setDriverSVA(float driverSVA) {
        this.driverSVA = driverSVA;
    }

    @Override
    public String toString() {
        return "Driver{" +
                "driverName='" + driverName + '\'' +
                ", driverCTS=" + driverCTS +
                ", driverSVA=" + driverSVA +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Driver driver = (Driver) o;
        return Float.compare(driver.driverCTS, driverCTS) == 0 && Float.compare(driver.driverSVA, driverSVA) == 0 && driverName.equals(driver.driverName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(driverName, driverCTS, driverSVA);
    }

    public Driver clone() {
        return new Driver(this.getDriverName(), this.getDriverCTS(), this.driverSVA);
    }
}
