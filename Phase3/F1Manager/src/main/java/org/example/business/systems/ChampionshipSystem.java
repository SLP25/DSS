package org.example.business.systems;

import org.example.business.Championship;
import org.example.business.cars.BodyWork;
import org.example.business.cars.CombustionRaceCar;
import org.example.business.cars.Engine;
import org.example.business.cars.Tyre;
import org.example.business.circuit.Circuit;
import org.example.business.drivers.Driver;
import org.example.business.participants.Participant;
import org.example.business.users.Admin;
import org.example.data.AdminDAO;
import org.example.data.ChampionshipDAO;
import org.example.data.CircuitDAO;
import org.example.data.RaceCarDAO;
import org.example.exceptions.system.AdminDoesNotExistException;
import org.example.exceptions.logic.PlayerAlreadyReachedLimitOfSetupChangesExceptions;
import org.example.exceptions.system.PlayerDoesNotExistException;
import org.example.exceptions.logic.DriverInUseException;
import org.example.exceptions.logic.ParticipantDoesNotExistException;
import org.example.exceptions.logic.PlayerAlreadyParticipatingException;
import org.example.exceptions.system.ChampionshipDoesNotExistException;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class ChampionshipSystem implements ChampionshipSystemFacade {

    @NotNull
    private Championship getChampionship(int championship) throws ChampionshipDoesNotExistException {
        Championship c = ChampionshipDAO.getInstance().get(championship);
        if (c == null)
            throw new ChampionshipDoesNotExistException(championship);

        return c;
    }

    @Override
    public Championship createChampionship(String admin) throws AdminDoesNotExistException {
        Admin a = AdminDAO.getInstance().get(admin);
        if (a == null)
            throw new AdminDoesNotExistException(admin);

        Championship c = new Championship(a);
        ChampionshipDAO.getInstance().put(c);
        return c;
    }

    @Override
    public Map<Participant, Integer> getStandings(int championship) throws ChampionshipDoesNotExistException {
        Championship c = getChampionship(championship);
        return getChampionship(championship).getStandings();
    }

    @Override
    public void signUp(int championship, String player, Driver pilot, CombustionRaceCar car) throws ChampionshipDoesNotExistException, PlayerDoesNotExistException, PlayerAlreadyParticipatingException, DriverInUseException {
        Championship c = getChampionship(championship);
        c.signUp(player, pilot, car);
    }

    @Override
    public List<Driver> getAvailableDrivers(int championship) throws ChampionshipDoesNotExistException {
        Championship c = getChampionship(championship);
        return c.getAvaiableDrivers();
    }

    @Override
    public boolean canChangeSetup(int championship, String player) throws ChampionshipDoesNotExistException, ParticipantDoesNotExistException {
        Championship c = getChampionship(championship);
        return c.canChangeSetup(player);
    }

    @Override
    public void changeSetup(int championship, String player, BodyWork.DownforcePackage aero) throws ChampionshipDoesNotExistException, ParticipantDoesNotExistException, PlayerAlreadyReachedLimitOfSetupChangesExceptions {
        Championship c = getChampionship(championship);

        if (!c.canChangeSetup(player))
            throw new PlayerAlreadyReachedLimitOfSetupChangesExceptions(player);

        c.ChangeSetup(player, aero);
    }

    @Override
    public void setStrategy(int championship, String player, Tyre.TyreType tireType, Engine.EngineMode engineMode) throws ChampionshipDoesNotExistException, ParticipantDoesNotExistException {
        Championship c = getChampionship(championship);
        c.setStrategy(player, tireType, engineMode);
    }

    @Override
    public List<CombustionRaceCar> getRaceCars() {
        return RaceCarDAO.getInstance().values().stream().toList();
    }

    @Override
    public List<Circuit> getCircuits() {
        return CircuitDAO.getInstance().values().stream().toList();
    }
}
