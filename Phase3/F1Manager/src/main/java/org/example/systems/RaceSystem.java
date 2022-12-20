package org.example.systems;

import org.example.business.Race;
import org.example.business.participants.Participant;
import org.example.data.ParticipantDAO;
import org.example.data.RaceDAO;
import org.example.exceptions.Systems.PlayerIsNotParticipatingInRaceException;
import org.example.exceptions.Systems.RaceDoesNotExistException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RaceSystem {
    private final RaceDAO races = RaceDAO.getInstance();

    public void prepareForRace(int race,String player) throws RaceDoesNotExistException, PlayerIsNotParticipatingInRaceException {
        Participant p = ParticipantDAO.getInstance().get(player);
        if (p==null)
            throw new PlayerIsNotParticipatingInRaceException();
        Race r=races.get(race);
        if (r==null)
            throw new RaceDoesNotExistException();
        r.setParticipantAsReady(p);
        if (!r.getReady().containsValue(Boolean.FALSE)){
            try {
                r.simulate();
            }catch (InterruptedException e){
                return;//TODO::MUDAR ISTO
            }finally {
                races.update(r);
            }
        }
    }
    public Race getRaceState(int race) throws RaceDoesNotExistException {
        Race r=races.get(race);
        if (r==null)
            throw new RaceDoesNotExistException();
        return r;//TODO:::MUDARISTO??????
    }
    public List<Participant> getRaceResults(int race) throws RaceDoesNotExistException {
        Race r=races.get(race);
        if (r==null)
            throw new RaceDoesNotExistException();
        List<Participant> p = new ArrayList<>();
        if (r.getFinished())
            for (Participant part:r.getResults())
                p.add(part.clone());
        return p;
    }

}
