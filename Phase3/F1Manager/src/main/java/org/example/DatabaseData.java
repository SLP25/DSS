package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseData {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_DATABASE = "F1Manager_dev";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "mysql";

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
