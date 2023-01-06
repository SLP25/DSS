package org.example.controllers;

import org.example.annotations.API;
import org.example.business.Championship;
import org.example.business.cars.BodyWork;
import org.example.business.cars.CombustionRaceCar;
import org.example.business.cars.Engine;
import org.example.business.cars.Tyre;
import org.example.business.drivers.Driver;
import org.example.business.participants.Participant;
import org.example.business.systems.ChampionshipSystemFacade;
import org.example.data.RaceCarDAO;
import org.example.exceptions.logic.LogicException;
import org.example.exceptions.system.SystemException;
import org.example.exceptions.logic.DriverInUseException;
import org.example.exceptions.logic.ParticipantDoesNotExistException;
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
     * championship create <admin>
     * championship <championshipID> (standings|drivers)
     * championship <championshipID> player <username> signup <pilot> <car>
     * championship <championshipID> player <username> setup [downforce]
     * championship <championshipID> player <username> strategy <tire> <engine>
     * car list
     *
     */

    @Endpoint(regex = "championship create (\\S+)")
    public void createChampionship(String admin)
    {
        try {
            Championship c = getModel().createChampionship(admin);
            getView().createSuccess(c);
        } catch (SystemException e) {
            getView().error(e.getMessage());
        }
    }

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
    public void signUp(Integer championshipID, String username, String driver, Integer carID) {
        try {
            Driver pilot = getModel().getAvailableDrivers(championshipID).stream()
                    .filter(d -> Objects.equals(d.getDriverName(), driver))
                    .findFirst().get();

            CombustionRaceCar car = RaceCarDAO.getInstance().get(carID);

            getModel().signUp(championshipID, username, pilot, car);
            getView().signupSuccess(username);
        } catch (NoSuchElementException e) {
            getView().error(String.format("Driver %s not available", driver));
        } catch (SystemException | LogicException e) {
            getView().error(e.getMessage());
        }
    }

    @Endpoint(regex = "championship (\\d+) player (\\S+) setup")
    public void canChangeSetup(Integer championshipID, String username)
    {
        try {
            boolean canChange = getModel().canChangeSetup(championshipID, username);
            getView().checkSetup(username, canChange);
        } catch (SystemException | LogicException e) {
            getView().error(e.getMessage());
        }
    }

    @Endpoint(regex = "championship (\\d+) player (\\S+) setup (\\S+)")
    public void changeSetup(Integer championshipID, String username, String downforcePackage)
    {
        try {
            BodyWork.DownforcePackage dp = BodyWork.DownforcePackage.valueOf(downforcePackage);

            getModel().changeSetup(championshipID, username, dp);
            getView().setSetupSuccess();
        } catch (IllegalArgumentException e) {
            getView().error(String.format("Invalid downforce package: %s", downforcePackage));
        } catch (SystemException | LogicException e) {
            getView().error(e.getMessage());
        }
    }

    @Endpoint(regex = "championship (\\d+) player (\\S+) strategy (\\S+) (\\S+)")
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
        } catch (SystemException | LogicException e) {
            getView().error(e.getMessage());
        }
    }

    @Endpoint(regex = "car list")
    public void getRaceCars() {
        List<CombustionRaceCar> cars = getModel().getRaceCars();
        getView().printRaceCars(cars);
    }
}
