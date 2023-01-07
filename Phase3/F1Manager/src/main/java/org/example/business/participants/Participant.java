package org.example.business.participants;

import org.example.business.cars.BodyWork;
import org.example.business.cars.CombustionEngine;
import org.example.business.cars.CombustionRaceCar;
import org.example.business.cars.Tyre;
import org.example.business.drivers.Driver;
import org.example.business.users.Player;
import org.example.data.ParticipantDAO;

import java.util.Objects;

public class Participant {
    private int championship;
    private int numberOfSetupChanges;
    private CombustionRaceCar car;
    private Driver driver;
    private Player manager;

    public Participant(int championship, int numberOfSetupChanges, CombustionRaceCar car, Driver driver, Player manager) {
        this.championship = championship;
        this.numberOfSetupChanges = numberOfSetupChanges;
        this.car = car;
        this.driver = driver;
        this.manager = manager;
    }

    public int getChampionship() {
        return championship;
    }

    public void setChampionship(int championship) {
        this.championship = championship;
    }

    public int getNumberOfSetupChanges() {
        return numberOfSetupChanges;
    }

    public void setNumberOfSetupChanges(int numberOfSetupChanges) {
        this.numberOfSetupChanges = numberOfSetupChanges;
    }

    public CombustionRaceCar getCar() {
        return car;
    }

    public void setCar(CombustionRaceCar car) {
        this.car = car;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Player getManager() {
        return manager;
    }

    public void setManager(Player manager) {
        this.manager = manager;
    }

    public void increaseNumberOfSetupChanges() {
        this.numberOfSetupChanges = this.numberOfSetupChanges + 1;
        ParticipantDAO.getInstance(this.getChampionship()).update(this);
    }

    public void setStrategy(Tyre.TyreType tyre, CombustionEngine.EngineMode engineMode) {
        this.car.setStrategy(tyre, engineMode);
        ParticipantDAO.getInstance(this.getChampionship()).update(this);
    }

    public void changeCarSetup(BodyWork.DownforcePackage df) {
        this.car.changeCarSetup(df);
        this.numberOfSetupChanges = this.numberOfSetupChanges + 1;
        ParticipantDAO.getInstance(this.getChampionship()).update(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Participant that = (Participant) o;
        return getChampionship() == that.getChampionship() && getNumberOfSetupChanges() == that.getNumberOfSetupChanges() && Objects.equals(getCar(), that.getCar()) && Objects.equals(getDriver(), that.getDriver()) && Objects.equals(getManager(), that.getManager());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getManager().getUsername(), getChampionship());
    }

    @Override
    public Participant clone() {
        return new Participant(this.championship, this.numberOfSetupChanges, this.getCar().clone(), this.getDriver().clone(), this.getManager().clone());
    }
}
