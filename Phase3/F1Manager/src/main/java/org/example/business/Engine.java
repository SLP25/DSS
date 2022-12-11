package org.example.business;

public class Engine extends CarPart{

    public static enum engineMode{
        LOW,
        MEDIUM,
        HIGH
    }

    private int capacity;
    private int power;

    private engineMode mode;

    public int getCapacity() {return capacity;}

    public void setCapacity(int capacity) {this.capacity = capacity;}

    public int getPower() {return power;}

    public void setPower(int power) {this.power = power;}

    public engineMode getMode() {return mode;}

    public void setMode(engineMode mode) {this.mode = mode;}

    public Engine(int cap,int pow,engineMode mod){
        capacity=cap;
        power=pow;
        mode=mod;
    }
}
