package org.example.controllers;

import org.example.annotations.API;
import org.example.business.Race;
import org.example.business.participants.Participant;
import org.example.business.systems.RaceSystemFacade;
import org.example.exceptions.system.SystemException;
import org.example.views.RaceView;
import org.example.annotations.Endpoint;

import java.util.List;

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
            Race r = getModel().getRaceState(championshipID, raceID);
            getView().printRaceState(r);
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
