package org.example.business;

import org.example.business.cars.CombustionRaceCar;
import org.example.business.cars.Tyre;
import org.example.business.circuit.Circuit;
import org.example.business.participants.Participant;
import org.example.business.users.Admin;
import org.example.business.users.Player;

import java.util.*;

import static java.lang.Thread.sleep;

public class Race {
    private static final double punctureFactor = 2.0;
    private static final double weatherChangeFactor = 1.0;
    private static final double trackDryingFactor = 1.0;
    private static final double standardOvertakeChance = 0.1;
    private static final double timeLostMinimum = -10.0;
    private static final double timeLostMaximum = 10.0;
    private static final double crashThreshold = 9.0;
    private static final double dnfThreshold = 9.5;

    private static final double lapTime = 90.0;

    private static final double maxDamage = 1;
    private static final double maxGapForOvertake = 0.5; //TODO:: Dynamic based on class
    private Random rng;
    private static final int simulationCooldown = 1000;
    private int id;

    private int leaderLocation;
    private Admin adminHosting;
    private Weather weatherConditions;
    private Circuit track;
    private List<Participant> result;
    private int currentLap;

    private boolean over;

    private List<Double> gaps;

    private Set<Player> ready;

    public List<Participant>getResults(){
        List<Participant> t=new ArrayList<>();
        for (Participant p:result)
            t.add(p.clone());
        return t;
    }
    private void setPlayerAsReady(Player p){
        ready.add(p.clone());
    }
    private boolean hasFinished(){
        return result!=null;
    }
    public static int getPointsOfPosition(int n){
        int[] a=new int[]{25,18,15,12,10,8,6,4,2,1};
        if (n > 10 || n<=0)
            return 0;
        return a[n-1];
    }


    public void simulate() throws InterruptedException {
        initializeRace();
        while(!hasFinished()) {
            simulateTireWear();
            simulateReliability();
            simulateWeather();
            simulateAdvancements();
            simulateTimeLost();
            normalizeGaps();
            simulateOvertaking();
            Thread.sleep(simulationCooldown);
        }
    }

    private void initializeRace() {
        over = false;
        currentLap = 1;
        rng = new Random();
        leaderLocation = 0;
        for(int i = 0; i < result.size(); i++)
            gaps.set(i,0.0);
    }

    private void normalizeGaps() {
        //Sort gaps in ascending order
        Collections.sort(gaps);

        //Shift all gaps so first element is zero
        double toAdd = -gaps.get(0);
        for(int i = 0; i < gaps.size(); i++) {
            gaps.set(i, gaps.get(i) + toAdd);
        }
    }

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

    private void simulateReliability() {
        int n = track.getCircuitSections().size();
        for(int i = 0; i < result.size(); i++) {
            Map<String, Float> vars = new HashMap<>();
            vars.put("laps", (float)currentLap);
            vars.put("sva", result.get(i).getDriver().getDriverSVA());

            double probability = result.get(i).getCar().getCategory().calculateReliability(vars);
            double r = rng.nextDouble();

            //Retire the car
            // As we will run this method n times per lap, and each decision of retiring
            // is independent of the others, the probability of retiring in a single decision
            // equals prob ^ (1 / n)
            if(r < Math.pow(probability, 1.0 / (double)n)) {
                gaps.remove(i);
                result.remove(i);
            }
        }
    }

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
    private void simulateAdvancements() {
        leaderLocation = (leaderLocation + 1) % track.getCircuitSections().size();

        if(leaderLocation == 0) {
            currentLap++;
        }

        if(currentLap > track.getNumberOfLaps()) {
            over = true;
        }
    }

    private void simulateTimeLost() {
        for(int i = 0; i < result.size(); i++) {
            //TODO:: Change this to depend on SVA
            double timeLost = rng.nextDouble() * (timeLostMaximum - timeLostMinimum) + timeLostMinimum;

            gaps.set(i, gaps.get(i) + timeLost);


            if(timeLost > crashThreshold) {
                CombustionRaceCar c = result.get(i).getCar();
                c.setDamage(c.getDamage() + (timeLost - crashThreshold) / (dnfThreshold - crashThreshold));

                if(c.getDamage() > maxDamage) {
                    //Retire the car
                    gaps.remove(i);
                    result.remove(i);
                }
            }
        }
    }

    private void simulateOvertaking() {
        for(int i = 1; i < result.size(); i++) {
            if(canOvertake(i - 1, i)) {
                Participant temp = result.get(i);
                result.set(i, result.get(i - 1));
                result.set(i - 1, temp);
            }
        }
    }

    private double getSectionGDUOfPosition(int position) {
        int numberSections = track.getCircuitSections().size();
        double timePerSection = lapTime / numberSections;
        int numberBehind = ((int)(double)gaps.get(position) % (int)lapTime) / (int)timePerSection;
        int index = (leaderLocation - numberBehind) % numberSections;
        return track.getCircuitSections().get(index).getSectionGDU();
    }

    private boolean canOvertakeStandard(int aheadPosition, int behindPosition) {
        double r = rng.nextDouble();
        double gdu = getSectionGDUOfPosition(aheadPosition);
        return r < standardOvertakeChance * (1 - gdu);
    }

    private boolean canOvertakePremium(int aheadPosition, int behindPosition) {
        double r = rng.nextDouble();
        double gdu = getSectionGDUOfPosition(aheadPosition);
        double gap = gaps.get(behindPosition) - gaps.get(aheadPosition);
        return r < standardOvertakeChance * (1 - gdu) * (maxGapForOvertake - gap);
    }

    private boolean canOvertake(int aheadPosition, int behindPosition) {
        if(adminHosting.getPremium())
            return canOvertakePremium(aheadPosition, behindPosition);
        else
            return canOvertakeStandard(aheadPosition, behindPosition);
    }
}
