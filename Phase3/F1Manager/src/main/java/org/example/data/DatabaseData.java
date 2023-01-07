package org.example.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseData {
    private static final String DB_DRIVER = "jdbc:mysql";

    private static final String DB_HOST = "localhost";
    private static final String DB_PORT = "3306";
    private static final String DB_DATABASE = "F1Manager_dev";

    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "mysql";

    private static final String DB_URL = DB_DRIVER + "://" + DB_HOST + ":" + DB_PORT + "/";

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
