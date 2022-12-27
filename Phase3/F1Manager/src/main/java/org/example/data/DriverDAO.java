package org.example.data;

import org.example.business.circuit.Circuit;
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

    private Map<String, Driver> drivers =Map.ofEntries(
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

    public static DriverDAO getInstance() {

        if (DriverDAO.singleton == null) DriverDAO.singleton = new DriverDAO();
        return DriverDAO.singleton;

    }


    @Override
    public int size() {
        return this.drivers.size();
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }



    @Override
    public boolean containsKey(Object key) {
        return drivers.containsKey(key);
    }




    @Override
    public Driver get(Object key) {
        return this.drivers.get(key).clone();
    }

    @Override
    public boolean containsValue(Object value) {
        return this.drivers.containsValue(value);
    }


    @Override
    public Driver put(String key, @NotNull Driver driver) {
        Driver d = this.drivers.put(key, driver.clone());
        if (d==null) return null;
        return d.clone();
    }


    public Driver put(@NotNull Driver driver) {
        return this.put(driver.getDriverName(),driver);
    }

    @Override
    public Driver remove(Object key) {
        Driver d = this.drivers.remove(key);
        if (d==null) return null;
        return d.clone();
    }

    @Override
    public void putAll(Map<? extends String, ? extends Driver> m) {
        Map<String,Driver> m2 = new HashMap<>();
        for (Entry<? extends String, ? extends Driver> e:m.entrySet()){
            m2.put(e.getKey(),e.getValue().clone());
        }
        this.drivers.putAll(m2);
    }


    @Override
    public void clear() {
       this.drivers.clear();
    }


    @Override
    public Set<String> keySet() {
        return this.drivers.keySet();
    }

    @Override
    public Collection<Driver> values() {
        return this.drivers.values().stream().map(Driver::clone).collect(Collectors.toList());
    }


    @Override
    public Set<Entry<String, Driver>> entrySet() {
        return values().stream().collect(
               Collectors.toMap(Driver::getDriverName, Driver::clone)).entrySet();
    }
}
