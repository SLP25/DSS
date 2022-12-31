package org.example;

import org.example.data.DatabaseData;
import org.example.controllers.MainController;
//import org.example.tests.UserTest;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        System.out.println(DatabaseData.getConnectionNoDatabase());
        //UserTest test = new UserTest();

        MainController controller = new MainController();
        controller.run();
    }
}