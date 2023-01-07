package org.example.data;


import org.example.business.users.Admin;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class AdminDAO implements Map<String, Admin> {
    private static AdminDAO singleton = null;

    private AdminDAO() {
        try (
                Connection conn = DatabaseData.getConnection();
                Statement stm = conn.createStatement();
        ) {
            String sql = "CREATE TABLE IF NOT EXISTS users (" +
                    "Username VARCHAR(255) NOT NULL PRIMARY KEY," +
                    "Password CHAR(60) NOT NULL," +
                    "Premium BOOLEAN);";
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
    public static AdminDAO getInstance() {
        if (AdminDAO.singleton == null) {
            AdminDAO.singleton = new AdminDAO();
        }
        return AdminDAO.singleton;
    }

    /**
     * @return number of users in the system
     */
    @Override
    public int size() {
        int i = 0;
        try (Connection conn = DatabaseData.getConnection();
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT count(*) FROM users WHERE Premium IS NOT NULL")
        ) {
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
        boolean r = false;
        try (Connection conn = DatabaseData.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT Username FROM users WHERE Username= ? AND Premium IS NOT NULL;");) {
            ps.setString(1, key.toString());
            try (ResultSet rs = ps.executeQuery();) {
                if (rs.next())
                    r = true;
            }
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
    public boolean containsValue(Object value) {
        if (!(value instanceof Admin)) return false;
        Admin p = (Admin) value;
        return p.equals(get(p.getUsername()));
    }


    @Override
    public Admin get(Object key) {
        try (Connection conn = DatabaseData.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT Username,Password,Premium FROM users WHERE Username= ? AND Premium IS NOT NULL;");) {

            ps.setString(1, key.toString());
            try (ResultSet rs = ps.executeQuery();) {
                if (rs.next())
                    return new Admin(
                            rs.getString(1),
                            rs.getString(2),
                            rs.getBoolean(3)
                    );
            }
        } catch (SQLException ignored) {
        }
        return null;
    }

    /**
     * adds a Admin to the database or updates its password if it already exists
     *
     * @param key   key with which the specified value is to be associated
     * @param admin value to be associated with the specified key
     * @return the Admin if added successfully (null otherwise)
     */
    @Override
    public Admin put(String key, Admin admin) {
        try (
                Connection conn = DatabaseData.getConnection();
                PreparedStatement ps = conn.prepareStatement("INSERT INTO users (Username,Password,Premium) VALUES (?,?,?);");) {
            ps.setString(1, admin.getUsername());
            ps.setString(2, admin.getHashedPassword());
            ps.setBoolean(3, admin.getPremium());
            ps.executeUpdate();
            return admin;
        } catch (SQLException e) {
            return null;
        }
    }

    public Admin put(Admin u) {
        return put(u.getUsername(), u);
    }

    /**
     * Removes a user with a given username from the database if it exists
     *
     * @param key key whose mapping is to be removed from the map
     * @return the user removed if it was possible to remove it (null otherwise)
     */
    @Override
    public Admin remove(Object key) {
        Admin user = this.get(key);
        if (user == null) return null;
        try (Connection conn = DatabaseData.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM users WHERE Username = ? AND Premium IS NOT NULL;");) {
            ps.setString(1, key.toString());
            ps.executeUpdate();
            return user;
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public void putAll(Map<? extends String, ? extends Admin> m) {
        try (Connection conn = DatabaseData.getConnection();) {
            conn.setAutoCommit(false);
            try (PreparedStatement stm = conn.prepareStatement("INSERT INTO users (Username,Password,Premium) VALUES (?,?,?);");) {
                for (Entry e : m.entrySet()) {
                    stm.setString(1, (String) e.getKey());
                    stm.setString(2, ((Admin) e.getValue()).getHashedPassword());
                    stm.setBoolean(3, ((Admin) e.getValue()).getPremium());
                    stm.executeUpdate();
                }
            }
            conn.commit();
            conn.setAutoCommit(true);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void clear() {
        try (
                Connection conn = DatabaseData.getConnection();
                Statement stm = conn.createStatement();) {
            stm.executeUpdate("DELETE FROM users WHERE Premium IS NOT NULL;");
        } catch (SQLException e) {
            throw new RuntimeException(e);//TODO MUDAR ISTO
        }
    }

    @Override
    public Set<String> keySet() {
        Set<String> r = new HashSet<String>();
        try (
                Connection conn = DatabaseData.getConnection();
                Statement stm = conn.createStatement();
                ResultSet rs = stm.executeQuery("SELECT Username FROM users WHERE Premium IS NOT NULL;");
        ) {
            while (rs.next())
                r.add(rs.getString(1));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return r;
    }

    @Override
    public Collection<Admin> values() {
        Collection<Admin> r = new HashSet<Admin>();
        try (
                Connection conn = DatabaseData.getConnection();
                Statement stm = conn.createStatement();
                ResultSet rs = stm.executeQuery("SELECT Username,Password,Premium FROM users WHERE Premium IS NOT NULL;");
        ) {
            while (rs.next())
                r.add(new Admin(rs.getString("Username"),
                        rs.getString("Password"),
                        rs.getBoolean("Premium")
                ));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return r;
    }

    @Override
    public Set<Entry<String, Admin>> entrySet() {
        return values().stream().collect(
                Collectors.toMap(Admin::getUsername, x -> x)).entrySet();
    }
}