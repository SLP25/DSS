package org.example.data;

import org.example.business.drivers.Driver;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;


/**
 * Class that represents and handles drivers.
 * It's technically a DAO, since data is hardcoded.
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
     * Hardcoded data for drivers.
     */
    private Map<String, Driver> drivers = Map.ofEntries(
            Map.entry("Super Max",new Driver("Super Max",0.99F,0.99F)),
            Map.entry("louis Hamilton",new Driver("louis Hamilton",0.99F,0.99F)),
            Map.entry("Battery Voltas",new Driver("Battery Voltas",0.9F,0.9F)),
            Map.entry("Minister of Defense",new Driver("Minister of Defense",0.9F,0.9F)),
            Map.entry("KMAG",new Driver("KMAG",0.5F,0.5F)),
            Map.entry("Smooth Operator",new Driver("Smooth Operator",0.5F,0.5F)),
            Map.entry("DU BIST WELTMEISTER",new Driver("DU BIST WELTMEISTER",0.5F,0.5F)),
            Map.entry("Shumi",new Driver("Shumi",0.75F,0.75F)),
            Map.entry("Mazaspin",new Driver("Mazaspin",0.1F,0.1F)),
            Map.entry("Goatifi",new Driver("Goatifi",0.1F,0.1F)),
            Map.entry("Super Max2",new Driver("Super Max2",0.99F,0.99F)),
            Map.entry("louis Hamilton2",new Driver("louis Hamilton2",0.99F,0.99F)),
            Map.entry("Battery Voltas2",new Driver("Battery Voltas2",0.9F,0.9F)),
            Map.entry("Minister of Defense2",new Driver("Minister of Defense2",0.9F,0.9F)),
            Map.entry("KMAG2",new Driver("KMAG2",0.5F,0.5F)),
            Map.entry("Smooth Operator2",new Driver("Smooth Operator2",0.5F,0.5F)),
            Map.entry("DU BIST WELTMEISTER2",new Driver("DU BIST WELTMEISTER2",0.5F,0.5F)),
            Map.entry("Shumi2",new Driver("Shumi2",0.75F,0.75F)),
            Map.entry("Mazaspin2",new Driver("Mazaspin2",0.1F,0.1F)),
            Map.entry("Goatifi2",new Driver("Goatifi2",0.1F,0.1F))
    );

    /**
     * Returns the singleton instance of the {@code DriverDAO} class.
     * <p>
     * If the instance has not been created yet, this method will create it before returning.
     *
     * @return The singleton instance of {@code DriverDAO}.
     */
    public static DriverDAO getInstance() {

        if (DriverDAO.singleton == null) DriverDAO.singleton = new DriverDAO();
        return DriverDAO.singleton;

    }

    /**
     * Returns the number of drivers in the collection.
     * <p>
     * This method overrides the {@link Collection#size()} method in order to
     * provide the size of the driver collection.
     *
     * @return The number of drivers in the collection.
     */
    @Override
    public int size() {
        return this.drivers.size();
    }

    /**
     * Returns {@code true} if the collection of drivers is empty, {@code false} otherwise.
     * <p>
     * This method overrides the {@link Collection#isEmpty()} method in order to
     * provide the correct implementation for the collection.
     *
     * @return {@code true} if the collection is empty, {@code false} otherwise.
     */
    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Returns {@code true} if the collection of drivers contains an element with the specified key, {@code false} otherwise.
     * <p>
     * This method overrides the {@link Map#containsKey(Object)} method in order to
     * provide the correct implementation for the collection.
     *
     * @param key The key to be checked for membership in the collection.
     * @return {@code true} if the collection contains an element with the specified key, {@code false} otherwise.
     */
    @Override
    public boolean containsKey(Object key) {
        return drivers.containsKey(key);
    }

    /**
     * Returns the driver with the specified key from the collection of drivers, or {@code null} if no such driver exists.
     * <p>
     * This method overrides the {@link Map#get(Object)} method in order to
     * provide the correct implementation for the collection. The returned driver is a
     * deep copy of the original driver to prevent modification of the internal data.
     *
     * @param key The key of the driver to be retrieved.
     * @return The driver with the specified key, or {@code null} if no such driver exists.
     */
    @Override
    public Driver get(Object key) {
        return this.drivers.get(key).clone();
    }

    /**
     * Returns {@code true} if the collection of drivers contains the specified value, {@code false} otherwise.
     * <p>
     * This method overrides the {@link Map#containsValue(Object)} method in order to
     * provide the correct implementation for the collection.
     *
     * @param value The value to be checked for membership in the collection.
     * @return {@code true} if the collection contains the specified value, {@code false} otherwise.
     */
    @Override
    public boolean containsValue(Object value) {
        return this.drivers.containsValue(value);
    }

    /**
     * Associates the specified driver with the specified key in the collection.
     * <p>
     * This method overrides the {@link Map#put(Object, Object)} method in order to
     * provide the correct implementation for the collection. The provided driver is
     * stored as a deep copy to prevent modification of the internal data.
     *
     * @param key The key with which the specified driver is to be associated.
     * @param driver The driver to be associated with the specified key.
     * @return The driver previously associated with the key, or {@code null} if there was no such driver.
     */
    @Override
    public Driver put(String key, @NotNull Driver driver) {
        Driver d = this.drivers.put(key, driver.clone());
        if (d==null) return null;
        return d.clone();
    }


    /**
     * Associates the specified driver with its name as the key in the collection.
     * <p>
     * This method stores the provided driver as a deep copy to prevent modification of the internal data.
     *
     * @param driver The driver to be associated with its name as the key.
     * @return The driver previously associated with the key, or {@code null} if there was no such driver.
     */
    public Driver put(@NotNull Driver driver) {
        return this.put(driver.getDriverName(),driver);
    }

    /**
     * Removes the driver with the specified key from the collection, if it is present.
     * <p>
     * This method overrides the {@link Map#remove(Object)} method in order to
     * provide the correct implementation for the collection. The removed driver is
     * returned as a deep copy to prevent modification of the internal data.
     *
     * @param key The key of the driver to be removed.
     * @return The driver previously associated with the key, or {@code null} if there was no such driver.
     */
    @Override
    public Driver remove(Object key) {
        Driver d = this.drivers.remove(key);
        if (d==null) return null;
        return d.clone();
    }

    /**
     * Copies all the drivers from the specified map to the collection.
     * <p>
     * This method overrides the {@link Map#putAll(Map)} method in order to
     * provide the correct implementation for the collection. The provided drivers
     * are stored as deep copies to prevent modification of the internal data.
     *
     * @param m The map containing the drivers to be added to the collection.
     */
    @Override
    public void putAll(Map<? extends String, ? extends Driver> m) {

        Map<String,Driver> m2 = new HashMap<>();

        for (Entry<? extends String, ? extends Driver> e:m.entrySet()){
            m2.put(e.getKey(),e.getValue().clone());
        }

        this.drivers.putAll(m2);
    }


    /**
     * Removes all drivers from the collection.
     * <p>
     * This method overrides the {@link Map#clear()} method in order to
     * provide the correct implementation for the collection.
     */
    @Override
    public void clear() {
       this.drivers.clear();
    }


    /**
     * Returns a set view of the keys in the collection.
     * <p>
     * This method overrides the {@link Map#keySet()} method in order to
     * provide the correct implementation for the collection.
     *
     * @return A set view of the keys in the collection.
     */
    @Override
    public Set<String> keySet() {
        return this.drivers.keySet();
    }

    /**
     * Returns a collection view of the drivers in the collection.
     * <p>
     * This method overrides the {@link Map#values()} method in order to
     * provide the correct implementation for the collection. The returned drivers
     * are deep copies to prevent modification of the internal data.
     *
     * @return A collection view of the drivers in the collection.
     */
    @Override
    public Collection<Driver> values() {
        return this.drivers.values().stream().map(Driver::clone).collect(Collectors.toList());
    }


    /**
     * Returns a set view of the mappings in the collection.
     * <p>
     * This method overrides the {@link Map#entrySet()} method in order to
     * provide the correct implementation for the collection. The returned drivers
     * are deep copies to prevent modification of the internal data.
     *
     * @return A set view of the mappings in the collection.
     */
    @Override
    public Set<Entry<String, Driver>> entrySet() {
        return values().stream().collect(
               Collectors.toMap(Driver::getDriverName, Driver::clone)).entrySet();
    }
}
