package org.example;

import org.example.data.DatabaseData;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        System.out.println(DatabaseData.getConnectionNoDatabase());
    }
}