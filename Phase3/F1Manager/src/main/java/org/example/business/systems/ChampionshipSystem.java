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
import org.example.exceptions.authentication.UsernameDoesNotExistException;
import org.example.exceptions.logic.NoParticipantWithThatNameException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChampionshipSystem implements ChampionshipSystemFacade{

    private final ChampionshipDAO dao = ChampionshipDAO.getInstance();

    public ChampionshipSystem() { }


    private Championship getChampionship(int championship) {
        return dao.get(championship);
    }

    @Override
    public Map<Participant, Integer> getStandings(int championship) {
        Championship c = getChampionship(championship);
        return c.getStandings();
    }

    @Override
    public void signUp(int championship, String player, Driver pilot, CombustionRaceCar car) throws UsernameDoesNotExistException {
        Championship c = getChampionship(championship);
        c.signUp(player, pilot, car);
    }

    @Override
    public List<Driver> getAvailableDrivers(int championship) {
        Championship c = getChampionship(championship);
        return c.getAvaiableDrivers();
    }

    @Override
    public boolean canChangeSetup(int championship, String player) throws NoParticipantWithThatNameException {
        Championship c = getChampionship(championship);
        return c.canChangeSetup(player);
    }

    @Override
    public void changeSetup(int championship, String player, BodyWork.DownforcePackage aero) throws NoParticipantWithThatNameException {
        Championship c = getChampionship(championship);
        c.ChangeSetup(player, aero);
    }

    @Override
    public void setStrategy(int championship, String player, Tyre.TyreType tireType, Engine.EngineMode engineMode) throws NoParticipantWithThatNameException {
        Championship c = getChampionship(championship);
        c.setStrategy(player, tireType, engineMode);
    }
}
