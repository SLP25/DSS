package org.example.business;

import org.example.business.cars.CombustionRaceCar;
import org.example.business.cars.Tyre;
import org.example.business.circuit.Circuit;
import org.example.business.participants.Participant;
import org.example.business.users.Admin;
import org.example.business.users.Player;
import org.example.data.ChampionshipDAO;
import org.example.data.RaceDAO;
import org.example.exceptions.authentication.WrongPasswordException;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.sleep;

/**
 * A race in a championship
 */
public class Race {
    /**
     * The id of the championship the race is part of
     */
    private int championshipId;

    /**
     * A factor used to calculate the probability of
     * a puncture occurring
     */
    private static final double punctureFactor = 2.0;

    /**
     * A factor used to calculate how quickly the weather
     * changes
     */
    private static final double weatherChangeFactor = 1.0;

    /**
     * A factor used to calculate how quickly the track dries /
     * gets wet
     */
    private static final double trackDryingFactor = 1.0;

    /**
     * The base chance (i.e. before any further calculations)
     * that a car will overtake the car ahead of it
     */
    private static final double standardOvertakeChance = 0.1;

    /**
     * The maximum amount of time a driver can gain in a single
     * iteration of the simulation algorithm
     */
    private static final double timeLostMinimum = -10.0;

    /**
     * The maximum amount of time a driver can lose in a single
     * iteration of the simulation algorithm
     */
    private static final double timeLostMaximum = 10.0;

    /**
     * The amount of time lost needed in order to consider the
     * mistake as crash
     */
    private static final double crashThreshold = 9.7;

    /**
     * The amount of time lost needed in order to consider the
     * mistake as crash resulting in terminal damage, forcing
     * the car to retire from the race
     */
    private static final double dnfThreshold = 9.8;

    /**
     * How long (in seconds) a lap takes
     */
    private static final double lapTime = 90.0;

    /**
     * The maximum amount of damage a car can
     * withstand before retiring
     */
    private static final double maxDamage = 1;

    /**
     * The maximum gap between two cars so that an
     * overtake is still possible
     */
    private static final double maxGapForOvertake = 0.5; //TODO:: Dynamic based on class

    /**
     * Random number generator used for simulating
     * the race
     */
    private Random rng;

    /**
     * The delay between two iterations of the simulation
     * algorithm
     */
    private static final int simulationCooldown = 1000;

    /**
     * The id of the race
     */
    private Integer id;

    /**
     * A lock used to ensure mutual exclusion in the
     * simulation
     */
    private Lock lock;

    /**
     * The location (as in the index of the section) the
     * leader of the race is currently at
     */
    private int leaderLocation;

    /**
     * The weather conditions
     */
    private Weather weatherConditions;

    /**
     * The track the race is being held at
     */
    private Circuit track;

    /**
     * The participants in the race indexed by their
     * username
     */
    private Map<String,Participant> participants;

    /**
     * The results of the race as a list.
     *
     * The leader is at position 0, second place at position 1,
     * .etc
     */
    private List<Participant> result;

    /**
     * The current lap of the race
     */
    private int currentLap;

    /**
     * Whether the race has finished
     */
    private boolean finished;

    /**
     * The gaps between the cars (in seconds). Each position corresponds
     * to the gap between the car and the car ahead of it. So, for example,
     * the first element will always be 0, the second element the gap between
     * first and second, .etc
     */
    private List<Double> gaps;

    /**
     * Whether each participant is ready to race
     */
    private Map<Participant,Boolean> ready;

    /**
     * Whether all players are ready to race
     * @return whether all players are ready to race
     */
    public boolean areAllPlayersReady() {
        for(Boolean b : ready.values()) {
            if(!b)
                return false;
        }
        return true;
    }

    /**
     * Gets the results of the race
     * @return the results of the race
     */
    public List<Participant> getResults(){
        List<Participant> t=new ArrayList<>();
        for (Participant p:result)
            t.add(p.clone());
        return t;
    }

    /**
     * Marks the given player as ready
     * @param player the username of the player
     */
    public void setPlayerAsReady(String player){
        Participant p = participants.get(player);
        ready.put(p,true);
        RaceDAO dao = RaceDAO.getInstance(championshipId);
        dao.update(this);
    }

    /**
     * Gets whether the race has finished
     * @return whether the race has finished
     */
    public boolean hasFinished(){
        return finished;
    }

    /**
     * Gets the number of points for the championship awarded
     * to the given position
     * @param n the given position (1-index)
     * @return the number of championship points awarded to the position
     */
    public static int getPointsOfPosition(int n){
        int[] a=new int[]{25,18,15,12,10,8,6,4,2,1};
        if (n > 10 || n<=0)
            return 0;
        return a[n-1];
    }

    /**
     * Simulates the race.
     *
     * The race is simulated in a separate thread. This method
     * does not block waiting for that thread to finish running.
     *
     * After the race is simulated, it is updated in the database
     */
    public void simulate() {
        new Thread(() -> {
            initializeRace();
            RaceDAO.getInstance(this.championshipId).addRunningRace(this);
            while(!finished) {
                lock.lock();
                try {
                    simulateTireWear();
                    simulateReliability();
                    simulateWeather();
                    simulateAdvancements();
                    simulateTimeLost();
                    normalizeGaps();
                    simulateOvertaking();
                } finally {
                    lock.unlock();
                }

                try {
                    Thread.sleep(simulationCooldown);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            RaceDAO.getInstance(this.championshipId).removeRunningRace(this);
            RaceDAO dao = RaceDAO.getInstance(championshipId);
            dao.update(this);
        }).start();
    }

    /**
     * Locks the race
     */
    public void lock() {
        lock.lock();
    }

    /**
     * Unlocks the race
     */
    public void unlock() {
        lock.unlock();
    }

    /**
     * Initializes the data needed to simulate the race
     */
    private void initializeRace() {
        finished = false;
        currentLap = 1;
        rng = new Random();
        leaderLocation = 0;
        for(int i = 0; i < result.size(); i++)
            gaps.set(i,0.0);
    }

    /**
     * Normalizes the gaps.
     *
     * By normalizing the gaps we mean sorting list of gaps
     * in ascending order and offsetting every gap by a certain
     * value such that the first element of the list is equal to zero
     */
    private void normalizeGaps() {
        //Sort gaps in ascending order
        Collections.sort(gaps);

        if(!gaps.isEmpty()) {
            //Shift all gaps so first element is zero
            double toAdd = -gaps.get(0);
            for(int i = 0; i < gaps.size(); i++) {
                gaps.set(i, gaps.get(i) + toAdd);
            }
        }
    }

    /**
     * Simulates the increase in tire wear
     */
    private void simulateTireWear() {
        for(Participant p : result) {
            CombustionRaceCar c = p.getCar();

            double wearStep = c.getTyres().isWet() ? (2 - weatherConditions.getTrackWetness())
                    * c.getTyres().getTyreWearStep()
                    :  c.getTyres().getTyreWearStep();
            double wear = c.getTireWear() + wearStep;

            c.setTireWear(wear);

            //Puncture
            if(Math.pow(wear, punctureFactor) > rng.nextDouble()) {
                Tyre t = c.getTyres();
                t.setPuncture(true);
                c.setTyres(t);
            }
        }
    }

    /**
     * Simulates the reliability of the cars
     */
    private void simulateReliability() {
        int n = track.getCircuitSections().size();
        for(int i = 0; i < result.size(); i++) {
            Map<String, Float> vars = new HashMap<>();
            vars.put("numberOfLaps", (float)currentLap);
            vars.put("sva", result.get(i).getDriver().getDriverSVA());
            vars.put("isHybrid", 0.0f);
            vars.put("pilotReliability", 1.0f);
            vars.put("classReliability", 0.95f);
            vars.put("engineCapacity", (float)result.get(i).getCar().getCombustionEngine().getCapacity());
            double probability = result.get(i).getCar().getCategory().calculateReliability(vars);
            double r = rng.nextDouble();

            //Retire the car
            // As we will run this method n times per lap, and each decision of retiring
            // is independent of the others, the probability of retiring in a single decision
            // equals prob ^ (1 / n)
            if(r > Math.pow(probability, 1.0 / (double)n)) {
                //System.out.println("Mechanical failure for driver in P" + (i + 1));
                gaps.remove(i);
                result.remove(i);
            }
        }
    }

    /**
     * Simulates the weather variation
     */
    private void simulateWeather() {
        //Random number between -1 and 1
        double r = rng.nextDouble() * 2 - 1;

        //Get current values
        double newRainLevel = weatherConditions.getRainLevel();
        double newTrackWetness = weatherConditions.getTrackWetness();

        //Update values
        newRainLevel += r * weatherConditions.getVariability() * weatherChangeFactor;
        newTrackWetness += r * weatherConditions.getVariability() * trackDryingFactor;

        //Make sure they stay in the [0,1] range
        newRainLevel = Math.min(newRainLevel, 1.0);
        newRainLevel = Math.max(newRainLevel, 0.0);
        newTrackWetness = Math.min(newTrackWetness, 1.0);
        newTrackWetness = Math.max(newTrackWetness, 0.0);

        weatherConditions.setRainLevel(newRainLevel);
        weatherConditions.setTrackWetness(newTrackWetness);
    }

    //TODO:: Make class matter

    /**
     * Simulate the progression of the field in the racetrack.
     *
     * @implNote the leader advances one section at a time
     */
    private void simulateAdvancements() {
        leaderLocation = (leaderLocation + 1) % track.getCircuitSections().size();

        if(leaderLocation == 0) {
            currentLap++;
        }

        if(currentLap > track.getNumberOfLaps()) {
            finished = true;
        }
    }

    /**
     * Simulates the variability in the time lost/gained by each driver in
     * the current part of the track
     */
    private void simulateTimeLost() {
        for(int i = 0; i < result.size(); i++) {
            //TODO:: Change this to depend on SVA
            double timeLost = rng.nextDouble() * (timeLostMaximum - timeLostMinimum) + timeLostMinimum;

            gaps.set(i, gaps.get(i) + timeLost);


            if(timeLost > crashThreshold) {
                CombustionRaceCar c = result.get(i).getCar();
                c.setDamage(c.getDamage() + (timeLost - crashThreshold) / (dnfThreshold - crashThreshold));

                if(c.getDamage() > maxDamage) {
                    //System.out.println("Crash for driver in P" + (i + 1));
                    //Retire the car
                    gaps.remove(i);
                    result.remove(i);
                }
            }
        }
    }

    /**
     * Simulates all overtaking
     */
    private void simulateOvertaking() {
        for(int i = 1; i < result.size(); i++) {
            if(canOvertake(i - 1, i)) {
                Participant temp = result.get(i);
                result.set(i, result.get(i - 1));
                result.set(i - 1, temp);
            }
        }
    }

    /**
     * Gets the difficulty of overtaking in the section of track the driver
     * in the given position is in
     *
     * @param position the given position
     * @return the difficulty of overtaking in the section of track the driver in
     * the given position is in
     */
    private double getSectionGDUOfPosition(int position) {
        int numberSections = track.getCircuitSections().size();
        double timePerSection = lapTime / numberSections;
        int numberBehind = ((int)(double)gaps.get(position) % (int)lapTime) / (int)timePerSection;
        int index = (leaderLocation - numberBehind + numberSections) % numberSections;
        return track.getCircuitSections().get(index).getSectionGDU();
    }

    /**
     * Whether the overtake will happen if the race is being held by a standard admin
     * @param aheadPosition the position of the car ahead
     * @param behindPosition the position of the car behind
     * @return the overtake will happen if the race is being held by a standard admin
     */
    private boolean canOvertakeStandard(int aheadPosition, int behindPosition) {
        double r = rng.nextDouble();
        double gdu = getSectionGDUOfPosition(aheadPosition);
        return r < standardOvertakeChance * (1 - gdu);
    }

    /**
     * Whether the overtake will happen if the race is being held by a premium admin
     * @param aheadPosition the position of the car ahead
     * @param behindPosition the position of the car behind
     * @return the overtake will happen if the race is being held by a premium admin
     */
    private boolean canOvertakePremium(int aheadPosition, int behindPosition) {
        double r = rng.nextDouble();
        double gdu = getSectionGDUOfPosition(aheadPosition);
        double gap = gaps.get(behindPosition) - gaps.get(aheadPosition);
        return r < standardOvertakeChance * (1 - gdu) * (maxGapForOvertake - gap);
    }

    /**
     * Whether the overtake will happen
     * @param aheadPosition the position of the car ahead
     * @param behindPosition the position of the car behind
     * @return the overtake will happen
     */
    private boolean canOvertake(int aheadPosition, int behindPosition) {
        if(ChampionshipDAO.getInstance().get(championshipId).getAdmin().getPremium())
            return canOvertakePremium(aheadPosition, behindPosition);
        else
            return canOvertakeStandard(aheadPosition, behindPosition);
    }

    /**
     * Parameterized constructor
     * @param id the id of the race
     * @param championshipId the id of the championship the race is part of
     * @param finished whether the race has finished
     * @param weather the weather for the race
     * @param track the track the race has been/will be held at
     * @param participants the participants of the race
     * @param ready whether each participant is ready to race
     */
    public Race(int id,int championshipId,boolean finished, Weather weather, Circuit track,
                List<Participant> participants,Map<Participant,Boolean>ready) {
        this.id = id;
        this.championshipId=championshipId;
        lock = new ReentrantLock();
        this.leaderLocation = 0;
        this.weatherConditions = new Weather(weather);
        this.track = track;
        this.finished = finished;
        this.currentLap = 0;
        this.result = participants;
        this.gaps = new ArrayList<>();
        this.ready = new HashMap<>();
        this.ready.putAll(ready);
        this.participants = new HashMap<>();
        for(Participant p : this.ready.keySet()) {
            this.participants.put(p.getManager().getUsername(), p);
        }

        if (!finished){
            for(int i = 0; i < participants.size(); i++)
                this.gaps.add(0.0);
        }
    }

    /**
     * Parameterized constructor
     * @param championshipId the id of the championship the race is part of
     * @param finished whether the race has finished
     * @param weather the weather for the race
     * @param track the track the race has been/will be held at
     * @param participants the participants of the race
     * @param ready whether each participant is ready to race
     */
    public Race(int championshipId,boolean finished, Weather weather, Circuit track, List<Participant> participants,
                Map<Participant,Boolean>ready) {
        this.id = null;
        this.championshipId=championshipId;
        lock = new ReentrantLock();
        this.leaderLocation = 0;
        this.weatherConditions = new Weather(weather);
        this.track = track;
        this.finished = finished;
        this.currentLap = 0;
        this.result = participants;
        this.gaps = new ArrayList<>();
        this.ready = new HashMap<>();
        this.ready.putAll(ready);
        this.participants = new HashMap<>();
        for(Participant p : this.ready.keySet()) {
            this.participants.put(p.getManager().getUsername(), p);
        }

        if (!finished){
            for(int i = 0; i < participants.size(); i++)
                this.gaps.add(0.0);
        }
    }

    /**
     * Copy constructor
     * @param r the race to deep copy
     */
    public Race(Race r) {
        this.id = r.getId();
        this.championshipId=r.getChampionshipId();
        this.lock = new ReentrantLock();
        this.leaderLocation = r.getLeaderLocation();
        this.weatherConditions = r.getWeatherConditions();
        this.track = r.getTrack();
        this.result = r.getResults();
        this.currentLap = r.getCurrentLap();

        this.finished = r.hasFinished();

        this.gaps = r.getGaps();

        this.ready = r.getReady();
        this.participants = r.participants;
    }

    /**
     * Gets the id of the race
     * @return the id of the race
     */
    public Integer getId() {
        return id;
    }

    /**
     * Gets the id of the championship the race is part of
     * @return the id of the championship the race is part of
     */
    public int getChampionshipId() {
        return championshipId;
    }

    /**
     * Sets the id of the championship the race is part of
     * @param championshipId the id of the championship the race is part of
     */
    public void setChampionshipId(int championshipId) {
        this.championshipId = championshipId;
    }

    /**
     * Gets the location of the leader
     *
     * @return the location of the leader
     */
    public int getLeaderLocation() {
        return leaderLocation;
    }

    /**
     * Gets the weather of the race
     *
     * @implNote the returned object is a deep copy of
     * the one stored by the instance
     *
     * @return the weather of the race
     */
    public Weather getWeatherConditions() {
        return new Weather(weatherConditions);
    }

    /**
     * Gets the track the race is being/will be/has been held at
     * @return the track the race is being/will be/has been held at
     */
    public Circuit getTrack() {
        return track;
    }

    /**
     * Gets the current lap of the race
     * @return the current lap of the race
     */
    public int getCurrentLap() {
        return currentLap;
    }

    /**
     * Gets the gaps between drivers (in seconds)
     * @return the gaps between drivers (in seconds)
     */
    public List<Double> getGaps() {
        List<Double> res = new ArrayList<>();

        for(Double d : gaps)
            res.add(d);

        return res;
    }

    /**
     * Gets whether each participant is ready to race
     * @return whether each participant is ready to race
     */
    public Map<Participant,Boolean> getReady() {
        return ready;
    }

    @Override
    public Race clone() {
        return new Race(this);
    }

    public void setId(int id) {
        this.id=id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Race race = (Race) o;
        return  finished == race.finished && Objects.equals(getId(), race.getId()) &&
                getChampionshipId()==race.getChampionshipId() && Objects.equals(getWeatherConditions().getVariability(),
                race.getWeatherConditions().getVariability()) && Objects.equals(getTrack().getName(),
                race.getTrack().getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
