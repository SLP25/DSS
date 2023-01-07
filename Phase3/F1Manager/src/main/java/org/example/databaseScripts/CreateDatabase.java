package org.example.databaseScripts;

import org.example.data.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Utility 'static' class that creates the database used on this project.
 */
public class CreateDatabase {

    /**
     * Run the script to create the database, its information
     * (name, type of database) are specified @ org.example.databaseScript.DatabaseData.
     *
     * @param args Argument list of passed parameters, never used.
     */
    public static void main(String[] args) {

        /* Open a connection with the database, we're using MySQL */
        try (Connection conn = DatabaseData.getConnectionNoDatabase();
             Statement stmt = conn.createStatement()) {

            String sql = "CREATE DATABASE IF NOT EXISTS " + DatabaseData.getDatabaseName() + ";";
            int result = stmt.executeUpdate(sql);

            if (result == 1) {
                System.out.println("Database created successfully!");
                try {
                    AdminDAO.getInstance();
                    ChampionshipDAO.getInstance();
                    CircuitDAO.getInstance();
                    DriverDAO.getInstance();
                    ParticipantDAO.getInstance(0);
                    PlayerDAO.getInstance();
                    RaceCarDAO.getInstance();
                    RaceDAO.getInstance(0);
                } catch (NullPointerException e) {
                    System.out.println(e.getMessage());
                    System.out.println("An error occured creating tables");
                }
                System.out.println("Tables created successfully!");
            }

        } catch (SQLException e) {
            System.out.println("Something went wrong while creating the database");
            e.printStackTrace();
        }
    }
}
