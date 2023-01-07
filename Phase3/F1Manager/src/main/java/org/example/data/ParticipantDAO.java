package org.example.data;

import org.example.business.cars.BodyWork;
import org.example.business.cars.CombustionRaceCar;
import org.example.business.cars.Engine;
import org.example.business.cars.Tyre;
import org.example.business.participants.Participant;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class ParticipantDAO implements Map<String, Participant> {
    private static Map<Integer, ParticipantDAO> singletons = new HashMap<>();
    private static RaceCarDAO rdb = RaceCarDAO.getInstance();
    private static DriverDAO ddb = DriverDAO.getInstance();
    private static PlayerDAO ppd = PlayerDAO.getInstance();
    private int championship;

    private ParticipantDAO(int championship) {
        this.championship = championship;
        try (Connection conn = DatabaseData.getConnection();
             Statement stm = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS participants (" +
                    "Player VARCHAR(255) NOT NULL," +
                    "Championship INT NOT NULL," +
                    "Car INT NOT NULL," +
                    "Driver VARCHAR(50) NOT NULL," +
                    "NumberOfSetupChanges INT NOT NULL," +
                    "Tyre VARCHAR(12) NOT NULL," +
                    "BodyWork VARCHAR(6) NOT NULL," +
                    "EngineMode VARCHAR(6) NOT NULL," +
                    "PRIMARY KEY (Player,Championship)," +
                    "FOREIGN KEY (Player) REFERENCES users(Username) ON DELETE CASCADE ON UPDATE CASCADE," +
                    "FOREIGN KEY (Championship) REFERENCES championships(Id) ON DELETE CASCADE ON UPDATE CASCADE" +
                    ");";
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
    public static ParticipantDAO getInstance(int championship) {
        if (!ParticipantDAO.singletons.containsKey(championship)) {
            ParticipantDAO.singletons.put(championship, new ParticipantDAO(championship));
        }
        return ParticipantDAO.singletons.get(championship);
    }

    /**
     * @return number of users in the system
     */
    @Override
    public int size() {
        int i = 0;
        try (Connection conn = DatabaseData.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT count(*) FROM participants WHERE Championship=?;");) {
            ps.setInt(1, championship);
            try (ResultSet rs = ps.executeQuery();) {
                if (rs.next())
                    i = rs.getInt(1);
            }
        } catch (Exception e) {
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
             PreparedStatement ps = conn.prepareStatement("SELECT Player FROM participants WHERE Championship=? AND Player= ?;");) {
            ps.setInt(1, championship);
            ps.setString(2, key.toString());
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
     * Gets a user given a username
     *
     * @param key username of the user
     * @return the user if exists (null otherwise)
     * @throws NullPointerException //TODO MUDAR ISTO
     */
    @Override
    public Participant get(Object key) {
        if (!(key instanceof String)) return null;
        try (Connection conn = DatabaseData.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT Player,Championship,Car,Driver,NumberOfSetupChanges,Tyre,BodyWork,EngineMode FROM participants WHERE Championship=? AND Player= ?;");) {
            ps.setInt(1, championship);
            ps.setString(2, (String) key);
            try (ResultSet rs = ps.executeQuery();) {
                if (rs.next()) {
                    CombustionRaceCar car = rdb.get(rs.getInt("Car"));
                    car.setStrategy(
                            Tyre.TyreType.valueOf(rs.getString("Tyre")),
                            Engine.EngineMode.valueOf(rs.getString("EngineMode"))
                    );
                    car.changeCarSetup(BodyWork.DownforcePackage.valueOf(rs.getString("BodyWork")));
                    return new Participant(
                            rs.getInt("Championship"),
                            rs.getInt("NumberOfSetupChanges"),
                            car,
                            ddb.get(rs.getString("Driver")),
                            ppd.get(rs.getString("Player"))
                    );
                }
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
        if (!(value instanceof Participant)) return false;
        Participant p = (Participant) value;
        return p.equals(get(p.getManager().getUsername()));
    }

    /**
     * adds a Participant
     *
     * @param key  key with which the specified value is to be associated
     * @param part value to be associated with the specified key
     * @return the Player if added successfully (null otherwise)
     */
    @Override
    public Participant put(String key, Participant part) {
        if (championship != part.getChampionship()) return null;
        try (
                Connection conn = DatabaseData.getConnection();
                PreparedStatement ps = conn.prepareStatement("INSERT INTO participants (Player,Championship,Car,Driver,NumberOfSetupChanges,Tyre,BodyWork,EngineMode) VALUES (?,?,?,?,?,?,?,?);");
        ) {
            ps.setString(1, key);
            ps.setInt(2, championship);
            ps.setInt(3, part.getCar().getId());
            ps.setString(4, part.getDriver().getDriverName());
            ps.setInt(5, part.getNumberOfSetupChanges());
            ps.setString(6, part.getCar().getTyres().getType().name());
            ps.setString(7, part.getCar().getDfPackage().getDfPackage().name());
            ps.setString(8, part.getCar().getCombustionEngine().getMode().name());
            ps.executeUpdate();
            return part;
        } catch (SQLException e) {
            return null;
        }
    }

    public Participant put(Participant part) {
        return this.put(part.getManager().getUsername(), part);
    }


    public Participant update(Participant part) {
        if (part.getChampionship() != championship) return null;
        try (
                Connection conn = DatabaseData.getConnection();
                PreparedStatement ps = conn.prepareStatement("UPDATE participants SET Car=?,Driver=?,NumberOfSetupChanges=?,Tyre=?,BodyWork=?,EngineMode=? WHERE Championship=? AND Player=?;");
        ) {
            CombustionRaceCar car = part.getCar();
            ps.setInt(1, car.getId());
            ps.setString(2, part.getDriver().getDriverName());
            ps.setInt(3, part.getNumberOfSetupChanges());
            ps.setString(4, car.getTyres().getType().name());
            ps.setString(5, car.getDfPackage().getDfPackage().name());
            ps.setString(6, car.getCombustionEngine().getMode().name());
            ps.setInt(7, championship);
            ps.setString(8, part.getManager().getUsername());
            ps.executeUpdate();
            return part;
        } catch (SQLException e) {
            return null;
        }
    }


    /**
     * Removes a user with a given username from the database if it exists
     *
     * @param key key whose mapping is to be removed from the map
     * @return the user removed if it was possible to remove it (null otherwise)
     */
    @Override
    public Participant remove(Object key) {
        Participant value = this.get(key);
        if (value == null)
            return null;
        try (
                Connection conn = DatabaseData.getConnection();
                PreparedStatement ps = conn.prepareStatement("DELETE FROM participants WHERE Championship=? AND Player = ?;");
        ) {
            ps.setInt(1, championship);
            ps.setString(2, (key.toString()));
            ps.executeUpdate();
            return value;
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public void putAll(Map<? extends String, ? extends Participant> m) {
        try (Connection conn = DatabaseData.getConnection();) {
            conn.setAutoCommit(false);
            try (
                    PreparedStatement ps = conn.prepareStatement("INSERT INTO participants (Player,Championship,Car,Driver,NumberOfSetupChanges,Tyre,BodyWork,EngineMode) VALUES (?,?,?,?,?,?,?,?);");
            ) {
                for (Map.Entry e : m.entrySet()) {
                    ps.setString(1, ((String) e.getKey()));
                    ps.setInt(2, championship);
                    ps.setInt(3, ((Participant) e.getValue()).getCar().getId());
                    ps.setString(4, ((Participant) e.getValue()).getDriver().getDriverName());
                    ps.setInt(5, ((Participant) e.getValue()).getNumberOfSetupChanges());
                    ps.setString(6, ((Participant) e.getValue()).getCar().getTyres().getType().name());
                    ps.setString(7, ((Participant) e.getValue()).getCar().getDfPackage().getDfPackage().name());
                    ps.setString(8, ((Participant) e.getValue()).getCar().getCombustionEngine().getMode().name());
                    ps.executeUpdate();
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
        try (Connection conn = DatabaseData.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM participants WHERE Championship=?;");) {
            ps.setInt(1, championship);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);//TODO MUDAR ISTO
        }
    }

    @Override
    public Set<String> keySet() {
        Set<String> r = new HashSet<String>();
        try (Connection conn = DatabaseData.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT Player FROM participants WHERE Championship=?;");) {
            ps.setInt(1, championship);
            try (ResultSet rs = ps.executeQuery();) {
                while (rs.next())
                    r.add(rs.getString("Player"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return r;
    }

    @Override
    public Collection<Participant> values() {
        Collection<Participant> r = new HashSet<Participant>();
        try (Connection conn = DatabaseData.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT Player,Championship,Car,Driver,NumberOfSetupChanges,Tyre,BodyWork,EngineMode FROM participants WHERE Championship=?;");) {
            ps.setInt(1, championship);
            try (ResultSet rs = ps.executeQuery();) {
                while (rs.next()) {
                    CombustionRaceCar car = rdb.get(rs.getInt("Car"));
                    car.setStrategy(
                            Tyre.TyreType.valueOf(rs.getString("Tyre")),
                            Engine.EngineMode.valueOf(rs.getString("EngineMode"))
                    );
                    car.changeCarSetup(BodyWork.DownforcePackage.valueOf(rs.getString("BodyWork")));
                    r.add(new Participant(
                            rs.getInt("Championship"),
                            rs.getInt("NumberOfSetupChanges"),
                            car,
                            ddb.get(rs.getString("Driver")),
                            ppd.get(rs.getString("Player"))
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return r;
    }

    @Override
    public Set<Entry<String, Participant>> entrySet() {
        return values().stream().collect(
                Collectors.toMap(x -> x.getManager().getUsername(), x -> x)).entrySet();
    }
}
