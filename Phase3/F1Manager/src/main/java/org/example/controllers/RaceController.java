package org.example.controllers;

import org.example.annotations.API;
import org.example.business.Race;
import org.example.business.participants.Participant;
import org.example.business.systems.ChampionshipSystem;
import org.example.business.systems.RaceSystem;
import org.example.business.systems.RaceSystemFacade;
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
     * race prepare <raceID> <username>
     * race get (state|result) <raceID>
     *
     */

    @Endpoint(regex = "race prepare (\\d+) (.+)")
    public void prepareForRace(Integer raceID, String username)
    {
        getModel().prepareForRace(raceID, username);
        getView().preparedForRace(username);
    }

    @Endpoint(regex = "race get state (\\d+)")
    public void getRaceState(Integer raceID) {
        Race r = getModel().getRaceState(raceID);
        getView().printRaceState(r);
    }

    @Endpoint(regex = "race get result (\\d+)")
    public void getRaceResults(Integer raceID) {
        List<Participant> r = getModel().getRaceResults(raceID);
        getView().printRaceResults(r);
    }
}
