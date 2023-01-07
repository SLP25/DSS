package org.example.business.cars;

import java.util.Objects;

public class CombustionEngine extends Engine {
    private int capacity;

    public CombustionEngine(EngineMode mode, int capacity) {
        super(mode);
        this.capacity = capacity;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CombustionEngine that = (CombustionEngine) o;
        return super.equals(o) && getCapacity() == that.getCapacity();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCapacity());
    }

    @Override
    public String toString() {
        return "CombustionEngine{" +
                "capacity=" + capacity +
                "mode=" + getMode().name() +
                '}';
    }

    @Override
    public CombustionEngine clone() {
        return new CombustionEngine(this.getMode(), this.getCapacity());
    }
}
