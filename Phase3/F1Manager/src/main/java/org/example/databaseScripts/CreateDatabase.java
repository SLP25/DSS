package org.example.databaseScripts;

import org.example.DatabaseData;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

public class CreateDatabase {

    public static void main(String[] args) {
        // Open a connection
        try(Connection conn = DatabaseData.getConnectionNoDatabase();
            Statement stmt = conn.createStatement();
        ) {
            String sql = "CREATE DATABASE " + DatabaseData.getDatabaseName() + " IF NOT EXISTS;";
            stmt.executeUpdate(sql);
            System.out.println("Database created successfully...");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
