package org.example.data;

import org.example.buisness.User;

import java.sql.*;
import java.util.*;

public class UserDAO implements Map<String, User> {
    private static UserDAO singleton = null;


    private UserDAO() {
        try (Connection conn = DatabaseData.getConnection();
             Statement stm = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS users (" +
                    "Username varchar(255) NOT NULL PRIMARY KEY," +
                    "Password char(60) NOT NULL);";
            stm.executeUpdate(sql);
        } catch (SQLException e) {
            // Erro a criar tabela...
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    /**
     * implementation of the singleton pattern
     *
     * @return returns the only instance of the class
     */
    public static UserDAO getInstance() {
        if (UserDAO.singleton == null) {
            UserDAO.singleton = new UserDAO();
        }
        return UserDAO.singleton;
    }

    /**
     * @return number of users in the system
     */
    @Override
    public int size() {
        int i = 0;
        try (Connection conn = DatabaseData.getConnection();
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT count(*) FROM users")) {
            if (rs.next()) {
                i = rs.getInt(1);
            }
        } catch (Exception e) {
            // Erro a criar tabela...
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return i;
    }

    /**
     * Method that verifies the existence of users
     *
     * @return true if the number of users is 0
     */
    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }

    /**
     * method which checks if a user with a given username exists in the database
     *
     * @param key username
     * @return true if the user exists
     * @throws NullPointerException //TODO MUDAR ISTO
     */
    @Override
    public boolean containsKey(Object key) {
        boolean r;
        try (Connection conn = DatabaseData.getConnection();
             Statement stm = conn.createStatement();
             ResultSet rs =
                     stm.executeQuery("SELECT Username FROM users WHERE Username='" + key.toString() + "'")) {
            r = rs.next();
        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return r;
    }

    /**
     * Verifies if a user exists in the database
     *
     * @param value ...
     * @return ...
     * @throws NullPointerException //TODO MUDAR ISTO
     */
    @Override
    public boolean containsValue(Object value) {//MUDAR ESTA DEFENIÇÃO
        User t = (User) value;
        return this.containsKey(t.getUsername());
    }

    /**
     * Gets a user given a username
     *
     * @param key username of the user
     * @return the user if exists (null otherwise)
     * @throws NullPointerException //TODO MUDAR ISTO
     */
    @Override
    public User get(Object key) {
        try {
            Connection conn = DatabaseData.getConnection();
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT * FROM users WHERE username='" + key + "'");
                if (rs.isBeforeFirst()) {
                    return new User(rs.getString(0), rs.getString(1));
                }
        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return null;
    }

    /**
     * adds a User to the database or updates its password if it already exists
     *
     * @param key key with which the specified value is to be associated
     * @param user value to be associated with the specified key
     * @return the User if added successfully (null otherwise)
     */
    @Override
    public User put(String key, User user) {
        try {
            Connection conn = DatabaseData.getConnection();
            Statement stm = conn.createStatement();
            stm.executeUpdate(
                    "INSERT INTO users VALUES ("+
                            user.getUsername()+", "+
                            user.getHashedPassword()+") " +
                            "ON DUPLICATE KEY UPDATE Password = "+user.getHashedPassword()+";"
            );
            return user;
        } catch (SQLException e) {
            return null;
        }
    }

    /**
     *
     * Removes a user with a given username from the database if it exists
     *
     * @param key key whose mapping is to be removed from the map
     * @return the user removed if it was possible to remove it (null otherwise)
     */
    @Override
    public User remove(Object key) {
        try {
            User user = this.get(key);
            if (user==null){
                return null;
            }
            Connection conn = DatabaseData.getConnection();
            Statement stm = conn.createStatement();
            stm.executeUpdate(
                    "DELETE FROM users WHERE Username = "+key.toString()+";");
            return user;
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public void putAll(Map<? extends String, ? extends User> m) {
        try {
            Connection conn = DatabaseData.getConnection();
            conn.setAutoCommit(false);
            PreparedStatement stm = conn.prepareStatement("INSERT INTO users VALUES (?,?) ON DUPLICATE KEY UPDATE Password = ?;");
            for (Entry e : m.entrySet()) {
                stm.setString(1, (String) e.getKey());
                stm.setString(2, ((User) e.getValue()).getHashedPassword());
                stm.setString(3, ((User) e.getValue()).getHashedPassword());
            }
            conn.commit();
            conn.setAutoCommit(true);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


        @Override
    public void clear() {
            try {
                Connection conn = DatabaseData.getConnection();
                Statement stm = conn.createStatement();
                stm.executeQuery("DELETE FROM users;");
            } catch (SQLException e) {
                throw new RuntimeException(e);//TODO MUDAR ISTO
            }
        }
            @Override
    public Set<String> keySet() {
        Set<String> r=new HashSet<String>();
        try {
            Connection conn = DatabaseData.getConnection();
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT Username FROM users;");
            while(rs.next()){
                r.add(rs.getString(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return r;
    }

    @Override
    public Collection<User> values() {
        Collection<User> r = new HashSet<User>();
        try {
            Connection conn = DatabaseData.getConnection();
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM users;");
            while(rs.next()){
                r.add(new User(rs.getString(1),rs.getString(2)));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return r;
    }

    @Override
    public Set<Entry<String, User>> entrySet() {
        Map<String,User> r = new HashMap<>();
        try {
            Connection conn = DatabaseData.getConnection();
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM users;");
            while(rs.next()){
                User u = new User(rs.getString(1),rs.getString(2));
                r.put(u.getUsername(),u);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return r.entrySet();
    }
}
