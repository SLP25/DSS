package org.example.data;

import org.example.business.drivers.Driver;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Class that represents and handles drivers in the database.
 * Implements Map<> methods for the ease of use.
 * @author Group 20
 * @version 1.0
 * @since 1.0
 */
public class DriverDAO implements Map<String, Driver> {

    /**
     * The one and only DriverDAO instance.
     */
    private static DriverDAO singleton = null;

    /**
     * Private constructor that creates the Driver table using the DatabaseData to connect to the database.
     * @throws RuntimeException If there is a problem running the SQL command.
     */
    private DriverDAO() {

        try (Connection conn = DatabaseData.getConnection();
             Statement stm = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS drivers(" +
                                 "DriverName VARCHAR(50) NOT NULL PRIMARY KEY," +
                                 "DriverCTS DECIMAL(10,2) NOT NULL," +
                                 "DriverSVA DECIMAL(10,2) NOT NULL)";

            stm.executeUpdate(sql);
        } catch (SQLException error) {

            error.printStackTrace();
            throw new RuntimeException(error.getMessage());

        }

    }

    /**
     * Method that makes sure there is only one instance of DriverDAO at a time.
     * <p>
     * If it's the first run, creates a new DriverDAO, else returns the existing one.
     * @return DriverDAO instance.
     */
    public static DriverDAO getInstance() {

        if (DriverDAO.singleton == null) DriverDAO.singleton = new DriverDAO();
        return DriverDAO.singleton;

    }

    /**
     * Computes the size of the 'driver' table.
     * @return The number of entries in the 'driver' table.
     */
    @Override
    public int size() {
        int i = 0;
        try (Connection conn = DatabaseData.getConnection();
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT COUNT(*) FROM drivers;");){
                if (rs.next())
                    i = rs.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return i;
    }

    /**
     * Checks if there are no existing drivers.
     * @return True if the table is empty, false otherwise.
     */
    @Override
    public boolean isEmpty() {
        return size() == 0;
    }



    @Override
    public boolean containsKey(Object key) {
        boolean r=false;
        try (Connection conn = DatabaseData.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT DriverName FROM drivers WHERE DriverName= ?;");){
            ps.setString(1,key.toString());
            try (ResultSet rs = ps.executeQuery();){
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




    @Override
    public Driver get(Object key) {
        try (Connection conn = DatabaseData.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT DriverCTS,DriverSVA FROM drivers WHERE DriverName=?;");){
            ps.setString(1,key.toString());
            try(ResultSet rs = ps.executeQuery();){
                if (rs.next())
                    return new Driver(
                            key.toString(),
                            rs.getFloat("DriverCTS"),
                            rs.getFloat("DriverSVA")
                    );
            }
        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return null;
    }

    @Override
    public boolean containsValue(Object value) {
        if (!(value instanceof Driver)) return false;
        Driver p = (Driver) value;
        return p.equals(get(p.getDriverName()));
    }


    @Override
    public Driver put(String key, @NotNull Driver driver) {
        try (
            Connection conn = DatabaseData.getConnection();
            PreparedStatement ps = conn.prepareStatement("INSERT INTO drivers (DriverName,DriverCTS,DriverSVA) VALUES (?,?,?);");){
            ps.setString(1,driver.getDriverName());
            ps.setFloat(2,driver.getDriverCTS());
            ps.setFloat(3,driver.getDriverSVA());
            ps.executeUpdate();
            return driver;
        } catch (SQLException e) {
            return null;
        }
    }


    /**
     * Insert a drive onto the database, by providing the Driver object.
     * @param driver Driver object.
     * @return The driver if the insertion was successful, null otherwise.
     */
    public Driver put(@NotNull Driver driver) {
        return this.put(driver.getDriverName(),driver);
    }

    @Override
    public Driver remove(Object key) {
        Driver driver = this.get(key);
        if (driver==null){
            return null;
        }
        try(Connection conn = DatabaseData.getConnection();
            PreparedStatement ps = conn.prepareStatement("DELETE FROM drivers WHERE DriverName = ?;");){
            ps.setString(1,key.toString());
            ps.executeUpdate();
            return driver;
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public void putAll(Map<? extends String, ? extends Driver> m) {
        try (Connection conn = DatabaseData.getConnection();){
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO drivers (DriverName,DriverCTS,DriverSVA) VALUES (?,?,?);");) {
                for (Entry e : m.entrySet()) {
                    ps.setString(1, (String) e.getKey());
                    ps.setFloat(2, ((Driver) e.getValue()).getDriverCTS());
                    ps.setFloat(3, ((Driver) e.getValue()).getDriverSVA());
                    ps.executeUpdate();
                }
            }
            conn.commit();
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Clear the table Driver FROM the database.
     */
    @Override
    public void clear() {
        try (Connection conn = DatabaseData.getConnection();
            Statement stm = conn.createStatement();){
            stm.executeUpdate("DELETE FROM drivers;");
        } catch (SQLException e) {
            throw new RuntimeException(e);//TODO MUDAR ISTO
        }
    }


    @Override
    public Set<String> keySet() {
        Set<String> r=new HashSet<String>();
        try (Connection conn = DatabaseData.getConnection();
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT DriverName FROM drivers;");){
                while(rs.next())
                    r.add(rs.getString("DriverName"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return r;
    }

    @Override
    public Collection<Driver> values() {
        Collection<Driver> r = new HashSet<Driver>();
        try (
            Connection conn = DatabaseData.getConnection();
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT DriverName,DriverCTS,DriverSVA FROM drivers;");
            ){
                while(rs.next())
                    r.add(new Driver(
                        rs.getString("DriverName"),
                        rs.getFloat("DriverCTS"),
                        rs.getFloat("DriverSVA")
                    ));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return r;
    }


    @Override
    public Set<Entry<String, Driver>> entrySet() {
        return values().stream().collect(
               Collectors.toMap(Driver::getDriverName, x -> x)).entrySet();
    }
}
