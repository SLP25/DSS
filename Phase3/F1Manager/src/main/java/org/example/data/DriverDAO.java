package org.example.data;

import org.example.business.Driver;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.*;


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

        try (Connection con = DatabaseData.getConnection();
             Statement statement = con.createStatement()  ) {

            String driverTable = "create table if not exists Driver (" +
                                 "DriverName varchar(50) not null primary key," +
                                 "DriverCTS decimal(10,2) not null," +
                                 "DriverSVA decimal(10,2) not null)";

            statement.execute(driverTable);


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

        int size = 0;
        String sizeQuery = "select count(*) from Driver";

        try (Connection con = DatabaseData.getConnection();
             Statement statement = con.createStatement();
             ResultSet results = statement.executeQuery(sizeQuery)) {

            if (results.next()) size = results.getInt(1);

        } catch (SQLException error) {

            error.printStackTrace();
            throw new RuntimeException(error.getMessage());
        }

        return size;
    }

    /**
     * Checks if there are no existing drivers.
     * @return True if the table is empty, false otherwise.
     */
    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Check if a drive with the name 'key' exists.
     * @param key The driver name string.
     * @return True if the driver is found, false otherwise.
     * @throws RuntimeException If there was an error executing the query.
     */
    @Override
    public boolean containsKey(Object key) {

        boolean foundKey;
        String queryString = "select DriverName from Driver where DriverName'" + key.toString() + "'";

        try (Connection con = DatabaseData.getConnection();
             Statement statement = con.createStatement();
             ResultSet results = statement.executeQuery(queryString)) {

            foundKey = results.next();

        } catch (SQLException error) {

            error.printStackTrace();
            throw new RuntimeException(error.getMessage());

        }

        return foundKey;
    }

    /**
     * Checks for a value (of type Driver) exists within the database.
     * @param value Driver object to search for.
     * @return True if the driver is found, false if 'value' is not an instance of Driver or if the driver is not
     * found.
     * @throws RuntimeException If there was an error executing the query.
     */
    @Override
    public boolean containsValue(Object value) {

        if (!(value instanceof Driver)) return false;

        Driver driver = (Driver) value;
        return containsKey(driver.getDriverName());

    }

    /**
     * Get the driver by providing its name.
     * @param key The driver name, has to be a string.
     * @return The newly built Driver object.
     */
    @Override
    public Driver get(Object key) {

        Driver driver = null;
        String queryString = "select * from Driver where DriverName='" + key.toString() + "'";

        try (Connection con = DatabaseData.getConnection();
             Statement statement = con.createStatement();
             ResultSet results = statement.executeQuery(queryString)) {

            if (results.next())
                driver = new Driver(
                        results.getString("DriverName"),
                        results.getFloat("DriverCTS"),
                        results.getFloat("DriverSVA")
                );

        } catch (SQLException error) {

            error.printStackTrace();
            throw new RuntimeException(error.getMessage());

        }

        return driver;
    }

    /**
     * Insert a drive onto the database, by providing its name (primary key) and the Driver object.
     * @param key Driver name.
     * @param driver Driver object.
     * @return The driver if the insertion was successful, null otherwise.
     */
    @Override
    public Driver put(String key, @NotNull Driver driver) {

        String updateString = "insert into Driver values (" +
                              key + ", " +
                              driver.getDriverCTS() + ", " +
                              driver.getDriverSVA() + ")";

        try (Connection con = DatabaseData.getConnection();
             Statement statement = con.createStatement()) {

            statement.executeUpdate(updateString);
            return driver;

        } catch (SQLException error) {

            return null;

        }
    }

    /**
     * Insert a drive onto the database, by providing the Driver object.
     * @param driver Driver object.
     * @return The driver if the insertion was successful, null otherwise.
     */
    public Driver put(@NotNull Driver driver) {

        String updateString = "insert into Driver values (" +
                driver.getDriverName() + ", " +
                driver.getDriverCTS() + ", " +
                driver.getDriverSVA() + ")";

        try (Connection con = DatabaseData.getConnection();
             Statement statement = con.createStatement()) {

            statement.executeUpdate(updateString);
            return driver;

        } catch (SQLException error) {

            return null;

        }
    }

    /**
     * Remove a driver by providing its name.
     * @param key Driver name.
     * @return The removed driver.
     */
    @Override
    public Driver remove(Object key) {

        String updateString = "delete from Drivers where DriverName=" + key.toString();

        try (Connection con = DatabaseData.getConnection();
             Statement statement = con.createStatement()) {

            Driver driver = get(key);
            if (driver == null) return null;

            statement.executeUpdate(updateString);
            return driver;

        } catch (SQLException error) {
            return null;
        }
    }

    /**
     * Given a Map containing the drivers and their corresponding name, insert them onto the database.
     * @param map Map containing the drivers.
     */
    @Override
    public void putAll(Map<? extends String, ? extends Driver> map) {

        String preparedQuery = "insert into Driver values (?,?,?)";

        try (Connection con = DatabaseData.getConnection();
             PreparedStatement statement = con.prepareStatement(preparedQuery)) {

            con.setAutoCommit(false);

            for (Entry<? extends String, ? extends Driver> entry : map.entrySet()) {

                statement.setString(1, entry.getKey()); // DriverName
                statement.setFloat(2, entry.getValue().getDriverCTS()); // DriverCTS
                statement.setFloat(3, entry.getValue().getDriverSVA()); // DriverSVA

                con.commit();
                con.setAutoCommit(true);

            }

        } catch (SQLException error) {

            error.printStackTrace();
            throw new RuntimeException(error.getMessage());

        }
    }

    /**
     * Clear the table Driver from the database.
     */
    @Override
    public void clear() {

        String queryString = "delete from Driver";

        try (Connection con = DatabaseData.getConnection();
             Statement statement = con.createStatement()) {

            statement.executeQuery(queryString);

        } catch (SQLException error) {

            error.printStackTrace();
            throw new RuntimeException(error.getMessage());

        }
    }

    /**
     * Get the every (primary) key on the Driver table from the database, as a set.
     * @return Set containing every driver name.
     */
    @Override
    public Set<String> keySet() {

        Set<String> set = new HashSet<>();

        String queryString = "select DriverName from Driver";

        try (Connection con = DatabaseData.getConnection();
             Statement statement = con.createStatement();
             ResultSet results = statement.executeQuery(queryString)) {

            while (results.next()) set.add(results.getString("DriverName"));

        } catch (SQLException error) {

            error.printStackTrace();
            throw new RuntimeException(error.getMessage());

        }

        return set;
    }

    /**
     * Get every value from the Driver table of the database.
     * @return HashSet containing every value by its primary key.
     */
    @Override
    public Collection<Driver> values() {

        Collection<Driver> collection = new HashSet<>();

        String queryString = "select * from Driver";

        try (Connection con = DatabaseData.getConnection();
             Statement statement = con.createStatement();
             ResultSet results = statement.executeQuery(queryString)) {

            while (results.next())
                collection.add(new Driver(
                        results.getString(1),
                        results.getFloat(2),
                        results.getFloat(2))
                );

        } catch (SQLException error) {

            error.printStackTrace();
            throw new RuntimeException(error.getMessage());

        }

        return collection;
    }

    /**
     * Get a set containing every entry for the Driver table.
     * @return Set containing the entries.
     */
    @Override
    public Set<Entry<String, Driver>> entrySet() {

        Map<String, Driver> map = new HashMap<>();

        String queryString = "select * from Driver";

        try (Connection con = DatabaseData.getConnection();
             Statement statement = con.createStatement();
             ResultSet results = statement.executeQuery(queryString)) {

            while (results.next()) {

                Driver driver = new Driver(
                        results.getString(1),
                        results.getFloat(2),
                        results.getFloat(2)
                );

                map.put(driver.getDriverName(), driver);

            }

        } catch (SQLException error) {

            error.printStackTrace();
            throw new RuntimeException(error.getMessage());

        }

        return map.entrySet();
    }
}
