package org.example.business.systems;

import org.example.business.Championship;
import org.example.business.cars.BodyWork;
import org.example.business.cars.CombustionRaceCar;
import org.example.business.cars.Engine;
import org.example.business.cars.Tyre;
import org.example.business.drivers.Driver;
import org.example.business.participants.Participant;
import org.example.exceptions.Systems.AdminDoesNotExistException;
import org.example.exceptions.Systems.ChampionshipDoesNotExistException;
import org.example.exceptions.Systems.PlayerAlreadyReachedLimitOfSetupChangesExceptions;
import org.example.exceptions.authentication.UsernameDoesNotExistException;
import org.example.exceptions.logic.DriverInUseException;
import org.example.exceptions.logic.NoParticipantWithThatNameException;
import org.example.exceptions.logic.PlayerAlreadyParticipatingException;

import java.util.List;
import java.util.Map;

public interface ChampionshipSystemFacade extends SystemFacade {

    Championship createChampionship(String admin) throws AdminDoesNotExistException;

    Map<Participant, Integer> getStandings(int championship) throws ChampionshipDoesNotExistException;

    void signUp(int championship, String player, Driver pilot, CombustionRaceCar car) throws ChampionshipDoesNotExistException, UsernameDoesNotExistException, PlayerAlreadyParticipatingException, DriverInUseException;

    List<Driver> getAvailableDrivers(int championship) throws ChampionshipDoesNotExistException;

    boolean canChangeSetup(int championship, String player) throws ChampionshipDoesNotExistException, NoParticipantWithThatNameException;

    void changeSetup(int championship, String player, BodyWork.DownforcePackage aero) throws ChampionshipDoesNotExistException, NoParticipantWithThatNameException, PlayerAlreadyReachedLimitOfSetupChangesExceptions;

    void setStrategy(int championship, String player, Tyre.TyreType tireType, Engine.EngineMode engineMode) throws ChampionshipDoesNotExistException, NoParticipantWithThatNameException;

    List<CombustionRaceCar> getRaceCars();
}
