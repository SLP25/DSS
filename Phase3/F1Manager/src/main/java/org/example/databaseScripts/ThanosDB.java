package org.example.databaseScripts;

import org.example.business.Race;
import org.example.data.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class ThanosDB {
    public static void main(String[] args) {
        RaceDAO.getInstance().clear();
        ParticipantDAO.getInstance().clear();
        AdminDAO.getInstance().clear();
        CircuitDAO.getInstance().clear();
        RaceCarDAO.getInstance().clear();
        DriverDAO.getInstance().clear();
        PlayerDAO.getInstance().clear();
    }
}


