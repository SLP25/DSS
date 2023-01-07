package org.example.controllers;

import org.example.annotations.API;
import org.example.business.Championship;
import org.example.business.Race;
import org.example.business.participants.Participant;
import org.example.business.systems.RaceSystem;
import org.example.business.systems.RaceSystemFacade;
import org.example.business.users.Admin;
import org.example.data.AdminDAO;
import org.example.data.ChampionshipDAO;
import org.example.data.RaceDAO;
import org.example.exceptions.system.SystemException;
import org.example.tests.RaceTest;
import org.example.views.RaceView;
import org.example.annotations.Endpoint;

import java.util.List;

import static org.example.tests.AdminTest.createAdmin;

@API(model = "org.example.business.systems.RaceSystem")
public class RaceController extends Controller {

    public RaceController(RaceSystemFacade system) {
        super(system, new RaceView());
    }

    @Override
    protected RaceSystemFacade getModel() {
        return (RaceSystemFacade)super.getModel();
    }

    @Override
    protected RaceView getView() {
        return (RaceView)super.getView();
    }

    /*
     * COMMAND SUMMARY:
     *
     * race <championshipID> create <weather> <track>
     * race <championshipID> <raceID> prepare <username>
     * race <championshipID> <raceID> (state|result)
     *
     */

    @Endpoint(regex = "race test create")
    public void createTestRace() {
        RaceSystem rs = new RaceSystem();
        Admin a = createAdmin(1).stream().map(n-> AdminDAO.getInstance().get(n)).reduce(null,(o, n)->n);
        Championship c =new Championship(a);
        c= ChampionshipDAO.getInstance().put(c);
        Championship finalC = c;
        Race r = RaceTest.createRace(c.getId(),1).stream()
                .map(x-> RaceDAO.getInstance(finalC.getId()).get(x))
                .limit(1)
                .reduce(null,(o, n)->n);

        for (Participant p: c.getParticipants().values()){
            getModel().prepareForRace(c.getId(), r.getId(),p.getManager().getUsername());
        }

        getView().testRace(r.getId());
    }

    @Endpoint(regex = "race (\\d+) create (\\d+\\.\\d+) (\\S+)")
    public void createRace(Integer championshipID, Float weather, String track)
    {
        try {
            Race race = getModel().createRace(championshipID, weather, track);
            getView().createSuccess(race);
        } catch (SystemException e) {
            getView().error(e.getMessage());
        }
    }

    @Endpoint(regex = "race (\\d+) (\\d+) prepare (\\S+)")
    public void prepareForRace(Integer championshipID, Integer raceID, String username)
    {
        try {
            getModel().prepareForRace(championshipID, raceID, username);
            getView().preparedForRace(username);
        } catch (SystemException e) {
            getView().error(e.getMessage());
        }
    }

    @Endpoint(regex = "race (\\d+) (\\d+) state")
    public void getRaceState(Integer championshipID, Integer raceID) {
        try {
            boolean over = false;
            while(!over) {
                Race r = getModel().getRaceState(championshipID, raceID);
                getView().printRaceState(r);
                over = r.hasFinished();
            }
        } catch (SystemException e) {
            getView().error(e.getMessage());
        }
    }

    @Endpoint(regex = "race (\\d+) (\\d+) result")
    public void getRaceResults(Integer championshipID, Integer raceID) {
        try {
            List<Participant> r = getModel().getRaceResults(championshipID, raceID);
            getView().printRaceResults(r);
        } catch (SystemException e) {
            getView().error(e.getMessage());
        }
    }
}
