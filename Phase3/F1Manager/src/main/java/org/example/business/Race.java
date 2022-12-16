package org.example.business;

import org.example.business.circuit.Circuit;
import org.example.business.participants.Participant;
import org.example.business.users.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Race {
    public enum Weather{
        RAINING,
        SUNSHINE,
        DROPSOFWATER
    }
    private int id;

    private Weather weatherConditions;
    private Circuit track;
    private List<Participant> result;

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



}
