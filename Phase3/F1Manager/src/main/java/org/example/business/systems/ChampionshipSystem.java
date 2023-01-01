package org.example.business.systems;

import org.example.business.Championship;
import org.example.business.Race;
import org.example.business.cars.BodyWork;
import org.example.business.cars.CombustionRaceCar;
import org.example.business.cars.Engine;
import org.example.business.cars.Tyre;
import org.example.business.drivers.Driver;
import org.example.business.participants.Participant;
import org.example.data.ChampionshipDAO;
import org.example.exceptions.Systems.PlayerAlreadyReachedLimitOfSetupChangesExceptions;
import org.example.exceptions.authentication.UsernameDoesNotExistException;
import org.example.exceptions.logic.DriverInUseException;
import org.example.exceptions.logic.NoParticipantWithThatNameException;
import org.example.exceptions.logic.PlayerAlreadyParticipatingException;
import org.example.exceptions.Systems.ChampionshipDoesNotExistException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChampionshipSystem implements ChampionshipSystemFacade{

    private final ChampionshipDAO dao = ChampionshipDAO.getInstance();


    private Championship getChampionship(int championship) {
        return dao.get(championship);
    }

    @Override
    public Map<Participant, Integer> getStandings(int championship) throws ChampionshipDoesNotExistException {
        Championship c = getChampionship(championship);

        if (c == null)
            throw new ChampionshipDoesNotExistException(championship);

        return c.getStandings();
    }

    @Override
    public void signUp(int championship, String player, Driver pilot, CombustionRaceCar car) throws ChampionshipDoesNotExistException, UsernameDoesNotExistException, PlayerAlreadyParticipatingException, DriverInUseException {
        Championship c = getChampionship(championship);

        if (c == null)
            throw new ChampionshipDoesNotExistException(championship);

        c.signUp(player, pilot, car);
    }

    @Override
    public List<Driver> getAvailableDrivers(int championship) throws ChampionshipDoesNotExistException {
        Championship c = getChampionship(championship);

        if (c == null)
            throw new ChampionshipDoesNotExistException(championship);

        return c.getAvaiableDrivers();
    }

    @Override
    public boolean canChangeSetup(int championship, String player) throws ChampionshipDoesNotExistException, NoParticipantWithThatNameException {
        Championship c = getChampionship(championship);

        if (c == null)
            throw new ChampionshipDoesNotExistException(championship);

        return c.canChangeSetup(player);
    }

    @Override
    public void changeSetup(int championship, String player, BodyWork.DownforcePackage aero) throws ChampionshipDoesNotExistException, NoParticipantWithThatNameException, PlayerAlreadyReachedLimitOfSetupChangesExceptions {
        Championship c = getChampionship(championship);

        if (c == null)
            throw new ChampionshipDoesNotExistException(championship);

        if (!c.canChangeSetup(player))
            throw new PlayerAlreadyReachedLimitOfSetupChangesExceptions(player);

        c.ChangeSetup(player, aero);
    }

    @Override
    public void setStrategy(int championship, String player, Tyre.TyreType tireType, Engine.EngineMode engineMode) throws ChampionshipDoesNotExistException, NoParticipantWithThatNameException {
        Championship c = getChampionship(championship);

        if (c == null)
            throw new ChampionshipDoesNotExistException(championship);

        c.setStrategy(player, tireType, engineMode);
    }
}
