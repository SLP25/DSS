package org.example.buisness;

import java.util.Comparator;
import org.springframework.security.crypto.bcrypt.BCrypt;

public class User implements Comparable<User> {
    public static Comparator<User> NumComparator = Comparator.comparing(User::getUsername);
    private String username;
    private String password;
    public static String encryptPassword(String password){
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }
    public static boolean checkPassword(String password,String hash){
        return BCrypt.checkpw(password, hash);
    }
    public User(String username, String password) {
        this.username = username;
        this.password = User.encryptPassword(password);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHashedPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = User.encryptPassword(password);
    }


    /**
     * @return String representing user.
     */
    @Override
    public String toString() {
        return "User("+this.username+")";
    }

    @Override
    public int compareTo(User o) {
        return this.username.compareTo(o.getUsername());
    }

}
