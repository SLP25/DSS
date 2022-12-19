package org.example.business;

import org.example.exceptions.logic.InvalidWeatherException;

import java.security.InvalidAlgorithmParameterException;
import java.util.Random;

public class Weather {
    private double trackWetness;
    private double rainLevel;

    private double variability;

    public Weather(double trackWetness, double rainLevel, double variability) {
        if(trackWetness < 0 || trackWetness > 1)
            throw new InvalidWeatherException("Track wetness must be between 0 and 1, inclusive");

        if(rainLevel < 0 || rainLevel > 1)
            throw new InvalidWeatherException("Rain level must be between 0 and 1, inclusive");

        if(variability < 0 || variability > 1)
            throw new InvalidWeatherException("Variability must be between 0 and 1, inclusive");

        this.trackWetness = trackWetness;
        this.rainLevel = rainLevel;
        this.variability = variability;
    }

    public Weather(Weather weather) {
        this(weather.getTrackWetness(), weather.getRainLevel(), weather.getVariability());
    }

    public Weather() {
        Random r = new Random();
        this.trackWetness = r.nextDouble();
        this.rainLevel = r.nextDouble();
        this.variability = r.nextDouble();
    }

    public double getTrackWetness() {
        return trackWetness;
    }

    public double getRainLevel() {
        return rainLevel;
    }

    public double getVariability() {
        return variability;
    }

    public void setTrackWetness(double trackWetness) {
        if(trackWetness < 0 || trackWetness > 1)
            throw new InvalidWeatherException("Track wetness must be between 0 and 1, inclusive");
        this.trackWetness = trackWetness;
    }

    public void setRainLevel(double rainLevel) {
        if(rainLevel < 0 || rainLevel > 1)
            throw new InvalidWeatherException("Rain level must be between 0 and 1, inclusive");

        this.rainLevel = rainLevel;
    }

    public void setVariability(double variability) {
        if(variability < 0 || variability > 1)
            throw new InvalidWeatherException("Variability must be between 0 and 1, inclusive");

        this.variability = variability;
    }

    @Override
    public String toString() {
        return String.format("Rain: %f, Wetness: %f, Variability: %f",
                this.rainLevel, this.trackWetness, this.variability);
    }

    @Override
    public Object clone() {
        return new Weather(this);
    }
}
