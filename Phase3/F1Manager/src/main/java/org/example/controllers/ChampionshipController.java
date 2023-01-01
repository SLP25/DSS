package org.example.controllers;

import org.example.annotations.API;
import org.example.business.cars.BodyWork;
import org.example.business.cars.CombustionRaceCar;
import org.example.business.cars.Engine;
import org.example.business.cars.Tyre;
import org.example.business.drivers.Driver;
import org.example.business.participants.Participant;
import org.example.business.systems.ChampionshipSystem;
import org.example.business.systems.ChampionshipSystemFacade;
import org.example.data.RaceCarDAO;
import org.example.exceptions.Systems.ChampionshipDoesNotExistException;
import org.example.exceptions.Systems.SystemException;
import org.example.exceptions.authentication.UsernameDoesNotExistException;
import org.example.exceptions.logic.DriverInUseException;
import org.example.exceptions.logic.NoParticipantWithThatNameException;
import org.example.exceptions.logic.PlayerAlreadyParticipatingException;
import org.example.views.ChampionshipView;
import org.example.annotations.Endpoint;

import java.util.*;

@API(model = "org.example.business.systems.ChampionshipSystem")
public class ChampionshipController extends Controller {

    public ChampionshipController(ChampionshipSystemFacade system) {
        super(system, new ChampionshipView());
    }

    @Override
    protected ChampionshipSystemFacade getModel() {
        return (ChampionshipSystemFacade)super.getModel();
    }

    @Override
    protected ChampionshipView getView() {
        return (ChampionshipView)super.getView();
    }

    /*
     * COMMAND SUMMARY:
     *
     * championship <championshipID> (standings|drivers)
     * championship <championshipID> player <username> signup <pilot> <car>
     * championship <championshipID> player <username> setup (check|set <downforce>)
     * championship <championshipID> player <username> strategy set <tire> <engine>
     *
     */

    @Endpoint(regex = "championship (\\d+) standings")
    public void getStandings(Integer championshipID)
    {
        try {
            Map<Participant, Integer> standings = getModel().getStandings(championshipID);
            getView().printStandings(standings);
        } catch (SystemException e) {
            getView().error(e.getMessage());
        }

    }

    @Endpoint(regex = "championship (\\d+) drivers")
    public void getAvailableDrivers(Integer championshipID)
    {
        try {
            List<Driver> drivers = getModel().getAvailableDrivers(championshipID);
            getView().printDrivers(drivers);
        } catch (SystemException e) {
            getView().error(e.getMessage());
        }

    }

    @Endpoint(regex = "championship (\\d+) player (\\S+) signup (\\S+) (\\d+)")
    public void signUp(Integer championshipID, String username, String pilotName, Integer carID) {
        try {
            Driver pilot = getModel().getAvailableDrivers(championshipID).stream()
                    .filter(d -> Objects.equals(d.getDriverName(), pilotName))
                    .findFirst().get();

            CombustionRaceCar car = RaceCarDAO.getInstance().get(carID);

            getModel().signUp(championshipID, username, pilot, car);
            getView().signupSuccess(username);
        } catch (NoSuchElementException e) {
            getView().error(String.format("Pilot %s not available", pilotName));
        } catch (UsernameDoesNotExistException | PlayerAlreadyParticipatingException | DriverInUseException | SystemException e) {
            getView().error(e.getMessage());
        }
    }

    @Endpoint(regex = "championship (\\d+) player (\\S+) setup check")
    public void canChangeSetup(Integer championshipID, String username)
    {
        try {
            boolean canChange = getModel().canChangeSetup(championshipID, username);
            getView().checkSetup(username, canChange);
        } catch (NoParticipantWithThatNameException | SystemException e) {
            getView().error(e.getMessage());
        }
    }

    @Endpoint(regex = "championship (\\d+) player (\\S+) setup set (\\S+)")
    public void changeSetup(Integer championshipID, String username, String downforcePackage)
    {
        try {
            BodyWork.DownforcePackage dp = BodyWork.DownforcePackage.valueOf(downforcePackage);

            getModel().changeSetup(championshipID, username, dp);
            getView().setSetupSuccess();
        } catch (IllegalArgumentException e) {
            getView().error(String.format("Invalid downforce package: %s", downforcePackage));
        } catch (NoParticipantWithThatNameException | SystemException e) {
            getView().error(e.getMessage());
        }
    }

    @Endpoint(regex = "championship (\\d+) player (\\S+) strategy set (\\S+) (\\S+)")
    public void setStrategy(Integer championshipID, String username, String tireType, String engineMode)
    {
        Tyre.TyreType tt;
        Engine.EngineMode em;

        try {
            tt = Tyre.TyreType.valueOf(tireType);
        } catch (IllegalArgumentException e) {
            getView().error(String.format("Invalid tire type: %s", tireType));
            return;
        }

        try {
            em = Engine.EngineMode.valueOf(engineMode);
        } catch (IllegalArgumentException e) {
            getView().error(String.format("Invalid engine mode: %s", engineMode));
            return;
        }

        try {
            getModel().setStrategy(championshipID, username, tt, em);
            getView().setStrategySuccess();
        } catch (NoParticipantWithThatNameException | SystemException e) {
            getView().error(e.getMessage());
        }
    }
}
