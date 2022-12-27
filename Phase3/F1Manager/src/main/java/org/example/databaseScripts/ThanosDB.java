package org.example.databaseScripts;

import org.example.business.Championship;
import org.example.business.Race;
import org.example.data.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class ThanosDB {
    public static void main(String[] args) {
        AdminDAO.getInstance().clear();
        PlayerDAO.getInstance().clear();
        for (Integer c : ChampionshipDAO.getInstance().keySet()){
            ParticipantDAO.getInstance(c).clear();
            RaceDAO.getInstance(c).clear();
        }
        ChampionshipDAO.getInstance().clear();
    }
}


