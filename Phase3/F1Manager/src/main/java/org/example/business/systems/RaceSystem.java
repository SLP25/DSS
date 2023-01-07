package org.example.business.systems;

import org.example.business.Championship;
import org.example.business.Race;
import org.example.business.Weather;
import org.example.business.participants.Participant;
import org.example.data.ChampionshipDAO;
import org.example.data.CircuitDAO;
import org.example.data.RaceDAO;
import org.example.exceptions.system.RaceHasAlreadyFinishedException;
import org.example.exceptions.system.ChampionshipDoesNotExistException;
import org.example.exceptions.system.CircuitDoesNotExistException;
import org.example.exceptions.system.RaceDoesNotExistException;
import org.jetbrains.annotations.NotNull;
import org.example.business.circuit.Circuit;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RaceSystem implements RaceSystemFacade {

    @NotNull
    private Race getRace(int championship, int race) throws ChampionshipDoesNotExistException, RaceDoesNotExistException
    {
        if (!ChampionshipDAO.getInstance().containsKey(championship))
            throw new ChampionshipDoesNotExistException(championship);

        Race r = RaceDAO.getInstance(championship).get(race);
        if (r == null)
            throw new RaceDoesNotExistException(race);

        return r;
    }

    @Override
    public Race createRace(int championship, float weather, String track) throws ChampionshipDoesNotExistException, CircuitDoesNotExistException
    {
        Championship c = ChampionshipDAO.getInstance().get(championship);
        if (c == null)
            throw new ChampionshipDoesNotExistException(championship);

        Circuit t = CircuitDAO.getInstance().get(track);
        if (t == null)
            throw new CircuitDoesNotExistException(track);

        List<Participant> participants = c.getParticipants().values().stream().toList();
        Map<Participant, Boolean> ready = participants.stream().collect(Collectors.toMap(p -> p, p -> false));

        Race r = new Race(championship, false, new Weather(weather), t, participants, ready);
        RaceDAO.getInstance(championship).put(r);
        return r;
    }

    @Override
    public void prepareForRace(int championship, int race, String player) throws ChampionshipDoesNotExistException, RaceDoesNotExistException,RaceHasAlreadyFinishedException
    {
        Race r = getRace(championship, race);

        try {
            r.lock();
            if (r.hasFinished()) throw new RaceHasAlreadyFinishedException(r);
            r.setPlayerAsReady(player);

            if(r.areAllPlayersReady())
                r.simulate();
        } finally {
            r.unlock();
        }
    }

    @Override
    public List<Participant> getRaceResults(int championship, int race) throws ChampionshipDoesNotExistException, RaceDoesNotExistException
    {
        Race r = getRace(championship, race);

        try {
            r.lock();
            return r.hasFinished() ? r.getResults() : null;
        } finally {
            r.unlock();
        }
    }

    @Override
    public Race getRaceState(int championship, int race) throws ChampionshipDoesNotExistException, RaceDoesNotExistException
    {
        Race r = getRace(championship, race);

        try {
            r.lock();
            return new Race(r);
        } finally {
            r.unlock();
        }
    }
}
