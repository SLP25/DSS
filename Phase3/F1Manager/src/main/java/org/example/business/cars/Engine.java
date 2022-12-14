package org.example.business.cars;

import java.util.Objects;

public abstract class Engine {
    public static enum EngineMode{
        LOW,
        MEDIUM,
        HIGH
    }

    private EngineMode mode;

    public EngineMode getMode() {return mode;}

    public void setMode(EngineMode mode) {this.mode = mode;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Engine engine = (Engine) o;
        return getMode() == engine.getMode();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMode());
    }

    public Engine(EngineMode mode) {this.mode = mode;}

}
