package org.example.business.participants;

import org.example.business.drivers.Driver;
import org.example.business.cars.CombustionRaceCar;
import org.example.business.users.Player;
import org.example.data.ParticipantDAO;

import java.util.Objects;

public class Participant {
    private int numberOfSetupChanges;
    private CombustionRaceCar car;
    private Driver driver;
    private Player manager;

    public Participant(int numberOfSetupChanges, CombustionRaceCar car, Driver driver, Player manager) {
        this.numberOfSetupChanges = numberOfSetupChanges;
        this.car = car;
        this.driver = driver;
        this.manager = manager;
    }

    public int getNumberOfSetupChanges() {return numberOfSetupChanges;}

    public void setNumberOfSetupChanges(int numberOfSetupChanges) {this.numberOfSetupChanges = numberOfSetupChanges;}

    public CombustionRaceCar getCar() {return car;}

    public void setCar(CombustionRaceCar car) {this.car = car;}

    public Driver getDriver() {return driver;}

    public void setDriver(Driver driver) {this.driver = driver;}

    public Player getManager() {return manager;}

    public void setManager(Player manager) {this.manager = manager;}

    public void increaseNumberOfSetupChanges(){
        this.numberOfSetupChanges=this.numberOfSetupChanges+1;
        ParticipantDAO.getInstance().update(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Participant that = (Participant) o;
        return getNumberOfSetupChanges() == that.getNumberOfSetupChanges() && Objects.equals(getCar(), that.getCar()) && Objects.equals(getDriver(), that.getDriver()) && Objects.equals(getManager(), that.getManager());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNumberOfSetupChanges(), getCar(), getDriver(), getManager());
    }


}
