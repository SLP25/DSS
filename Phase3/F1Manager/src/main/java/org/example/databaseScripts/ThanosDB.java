package org.example.databaseScripts;

import org.example.data.DatabaseData;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * A class that only used to destroy our database.
 * <p>
 * This class is named after the Marvel Comics character Thanos, who is known for
 * wiping out half of the universe's population with a snap of his fingers. Similarly,
 * the main method of this class allows the user to destroy the entire database with
 * a single command.
 *
 * @see <a href="https://marvel.fandom.com/wiki/Thanos">Thanos</a>
 */
public class ThanosDB {

    /**
     * This method gets each table of the database and destroys it.
     *
     * @param args Argument list of passed parameters, never used.
     */
    public static void main(String[] args) {

        try (Connection conn = DatabaseData.getConnectionNoDatabase();
             Statement stmt = conn.createStatement()) {

            String sql = "DROP DATABASE IF EXISTS " + DatabaseData.getDatabaseName() + ";";
            int result = stmt.executeUpdate(sql);

            if (result == 1) {
                System.out.println("Database dropped successfully!");
            }

        } catch (SQLException e) {
            System.out.println("Something went wrong while dropped the database");
            e.printStackTrace();
        }
    }
}


