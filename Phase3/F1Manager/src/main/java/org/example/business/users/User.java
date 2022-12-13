package org.example.business;

import java.util.Comparator;
import java.util.Objects;

import org.example.data.AdminDAO;
import org.example.data.PlayerDAO;
import org.springframework.security.crypto.bcrypt.BCrypt;

public class User implements Comparable<User> {
    public static Comparator<User> NumComparator = Comparator.comparing(User::getUsername);
    private String username;
    private String password;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return getUsername().equals(user.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername());
    }

    public static String encryptPassword(String password){
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }
    public static boolean checkPassword(String password,String hash){
        return BCrypt.checkpw(password, hash);
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
    public User(String username) {
        this.username = username;
        this.password = "";
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

    public static boolean doesUserExists(String username){
        PlayerDAO pdb = PlayerDAO.getInstance();
        AdminDAO adb = AdminDAO.getInstance();
        return pdb.containsKey(username) || adb.containsKey(username);
    }

}
