package org.example.business;

import org.example.business.cars.BodyWork;
import org.example.business.cars.CombustionRaceCar;
import org.example.business.cars.Engine;
import org.example.business.cars.Tyre;
import org.example.business.drivers.Driver;
import org.example.business.participants.Participant;
import org.example.business.users.Admin;
import org.example.business.users.Player;
import org.example.data.DriverDAO;
import org.example.data.ParticipantDAO;
import org.example.data.PlayerDAO;
import org.example.data.RaceDAO;
import org.example.exceptions.authentication.UsernameDoesNotExistException;
import org.example.exceptions.logic.DriverInUseException;
import org.example.exceptions.logic.NoParticipantWithThatNameException;
import org.example.exceptions.logic.PlayerAlreadyParticipatingException;

import java.util.*;
import java.util.stream.Collectors;

public class Championship {
    private Integer id;
    private Admin admin;
    private Map<String, Participant> participants=null;
    private Set<Race> races=null;
    public Championship(Admin admin) {
        this.id = null;
        this.admin = admin;
    }
    public Championship(int id,Admin admin) {
        this.id = id;
        this.admin = admin;
    }

    public Integer getId() {
        return id;
    }
    public void setId(int id) {
        this.id=id;
    }
    public Admin getAdmin() {
        return admin;
    }
    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public Map<String, Participant> getParticipants() {
        if (id==null) return null;
        if (participants==null)
            participants=ParticipantDAO.getInstance(id).entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e->e.getValue().clone()));
        return participants.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e->e.getValue().clone()));
    }
    public Set<Race> getRaces() {
        if (id==null) return null;
        if (races==null)
            races = RaceDAO.getInstance(id).values().stream().map(Race::clone).collect(Collectors.toSet());
        return races.stream().map(Race::clone).collect(Collectors.toSet());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Championship that = (Championship) o;
        return Objects.equals(getId(), that.getId()) ;
    }

    @Override
    public int hashCode() {
        return getId();
    }

    public Map<Participant,Integer> getStandings(){
        Map<Participant,Integer> r = new HashMap<>();
        for (Participant p :getParticipants().values())
            r.put(p,0);
        for (Race c:getRaces()){
            if(c.hasFinished()){
                List<Participant> ps = c.getResults();
                for (int i=0;i< ps.size();i++){
                    Participant p = ps.get(i);
                    r.put(p,r.get(p)+Race.getPointsOfPosition(i+1));
                }
            }
        }
        return r;
    }

    public List<Driver> getAvaiableDrivers(){
        List<Driver> r = (List<Driver>) DriverDAO.getInstance().values();
        for (Driver d:getParticipants().values().stream().map(Participant::getDriver).collect(Collectors.toSet()))
            r.remove(d);
        return r;
    }
    public Boolean canChangeSetup(String player) throws NoParticipantWithThatNameException {
        Participant p = getParticipants().get(player);
        if (p==null) throw new NoParticipantWithThatNameException();
        return p.getNumberOfSetupChanges() < ( (double) 2/3) * getRaces().size();
    }
    public void ChangeSetup(String player, BodyWork.DownforcePackage df) throws NoParticipantWithThatNameException {
        Participant p = getParticipants().get(player);
        if (p==null) throw new NoParticipantWithThatNameException();
        p.changeCarSetup(df);
    }
    public void signUp(String player, Driver pilot, CombustionRaceCar car) throws UsernameDoesNotExistException, DriverInUseException, PlayerAlreadyParticipatingException {
        Player p = PlayerDAO.getInstance().get(player);
        if (p==null) throw new UsernameDoesNotExistException();
        if (this.isDriverInUse(pilot)) throw new DriverInUseException();
        if (ParticipantDAO.getInstance(this.id).get(player)!=null) throw new PlayerAlreadyParticipatingException();
        ParticipantDAO.getInstance(id).put(player,new Participant(id,0,car,pilot,p));
        this.participants=null;
    }
    public void setStrategy(String player, Tyre.TyreType tyreType, Engine.EngineMode engineMode) throws NoParticipantWithThatNameException {
        Participant p = getParticipants().get(player);
        if (p==null) throw new NoParticipantWithThatNameException();
        p.setStrategy(tyreType,engineMode);
    }
    public Boolean isDriverInUse(Driver pilot){
        return this.participants.values().stream().map(Participant::getDriver).toList().contains(pilot);
    }
}
