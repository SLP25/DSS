package org.example.business.systems;

import org.example.business.cars.BodyWork;
import org.example.business.cars.CombustionRaceCar;
import org.example.business.cars.Engine;
import org.example.business.cars.Tyre;
import org.example.business.drivers.Driver;
import org.example.business.participants.Participant;
import org.example.exceptions.authentication.UsernameDoesNotExistException;
import org.example.exceptions.logic.NoParticipantWithThatNameException;

import java.util.List;
import java.util.Map;

public interface ChampionshipSystemFacade {

    Map<Participant, Integer> getStandings(int championship);

    void signUp(int championship, String player, Driver pilot, CombustionRaceCar car) throws UsernameDoesNotExistException;

    List<Driver> getAvailableDrivers(int championship);

    boolean canChangeSetup(int championship, String player) throws NoParticipantWithThatNameException;

    void changeSetup(int championship, String player, BodyWork.DownforcePackage aero) throws NoParticipantWithThatNameException;

    void setStrategy(int championship, String player, Tyre.TyreType tireType, Engine.EngineMode engineMode) throws NoParticipantWithThatNameException;
}
