package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseData {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/";
    private static final String DB_DATABASE = "F1Manager_dev";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "postgres";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL + DB_DATABASE, DB_USER, DB_PASSWORD);
    }
    public static Connection getConnectionNoDatabase() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public static String getDatabaseName() {
        return DB_DATABASE;
    }
}
