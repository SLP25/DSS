package org.example.views;

import org.example.business.users.User;

public class UserView extends View {

    public void registerSuccess(User user) {
        System.out.printf("Successfully registered new user %s%n", user.getUsername());
    }

    public void loginSuccess(User user) {
        System.out.printf("Successfully logged in as %s%n", user.getUsername());
    }

    public void checkUsername(String username, boolean exists) {
        System.out.printf("Username %s is%s registered in the database%n", username, exists ? "" : " not");
    }
}
