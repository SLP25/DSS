package org.example.databaseScripts;

import org.example.data.DatabaseData;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

/**
 * Utility 'static' class that creates the database used on this project.
 */
public class CreateDatabase {

    /**
     * Run the script to create the database, its information
     * (name, type of database) are specified @ org.example.databaseScript.DatabaseData.
     * @param args Argument list of passed parameters, never used.
     */
    public static void main(String[] args) {

        /* Open a connection with the database, we're using MySQL */
        try(Connection conn = DatabaseData.getConnectionNoDatabase();
            Statement stmt = conn.createStatement()) {

            String sql = "CREATE DATABASE IF NOT EXISTS " + DatabaseData.getDatabaseName() + ";";
            int result = stmt.executeUpdate(sql);

            if (result == 1) {
                System.out.println("Database created successfully!");
            }

        } catch (SQLException e) {
            System.out.println("Something went wrong while creating the database");
            e.printStackTrace();
        }
    }
}
