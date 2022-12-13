package org.example.data;

import org.example.business.cars.*;
import org.example.exceptions.cars.EngineCannotBeEletricAndCombustionSimultaneousException;
import org.example.exceptions.cars.EngineCannotHaveCapacityAndNotBeCombustionException;
import org.example.exceptions.cars.EngineCannotHavePowerAndNotBeElectricException;

import java.sql.*;
import java.util.*;

public class RaceCarDAO implements Map<Integer, RaceCar> {
    private static RaceCarDAO singleton = null;

    private RaceCarDAO() {
        try (Connection conn = DatabaseData.getConnection();
             Statement stm = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS cars (" +
                    "Id INT AUTO_INCREMENT PRIMARY KEY,"+
                    "Class CHAR(33) NOT NULL," +
                    "Tyre VARCHAR(12) NOT NULL,"+
                    "BodyWork VARCHAR(6) NOT NULL,"+
                    "EngineMode VARCHAR(6) NOT NULL," +
                    "EngineCapacity INT NOT NULL," +
                    "EnginePower INT);";
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
    public static RaceCarDAO getInstance() {
        if (RaceCarDAO.singleton == null) {
            RaceCarDAO.singleton = new RaceCarDAO();
        }
        return RaceCarDAO.singleton;
    }

    /**
     * @return number of cars in the system
     */
    @Override
    public int size() {
        int i = 0;
        try (Connection conn = DatabaseData.getConnection();
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT count(*) FROM cars")) {
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
     * Method that verifies the existence of cars
     *
     * @return true if the number of cars is 0
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
        boolean r=false;
        try {
            Connection conn = DatabaseData.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT Id FROM cars WHERE Id= ?;");
            ps.setInt(1,(int) key);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                r=true;
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
        RaceCar p = (RaceCar) value;
        boolean r = false;
        try {
            Connection conn = DatabaseData.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT Id,Class,Tyre,BodyWork,EngineMode,EngineCapacity,EnginePower FROM cars WHERE Id= ?;");
            ps.setInt(1, p.getId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("Id");
                Class<? extends CarClass> c = (Class<? extends CarClass>) Class.forName(rs.getString("Class"));
                Tyre tyre = new Tyre(Tyre.TyreType.valueOf(rs.getString("Tyre")));
                BodyWork bodyWork = new BodyWork(BodyWork.DownforcePackage.valueOf(rs.getString("BodyWork")));
                Engine.EngineMode eM = Engine.EngineMode.valueOf(rs.getString("EngineMode"));
                Engine engineC = new Engine(rs.getInt("EngineCapacity"), 0, eM, CarPart.CarPartType.COMBUSTION_ENGINE);
                Engine engineE = null;
                Integer ePow = rs.getInt("EnginePower");
                if (!rs.wasNull()) {
                    engineE = new Engine(0, ePow, eM, CarPart.CarPartType.ELECTRIC_ENGINE);
                }
                r = p.equals(new RaceCar(id, c, tyre, bodyWork, engineC, engineE));
            }
        } catch (SQLException | EngineCannotBeEletricAndCombustionSimultaneousException |
                 EngineCannotHaveCapacityAndNotBeCombustionException |
                 EngineCannotHavePowerAndNotBeElectricException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
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
    public RaceCar get(Object key) {
        RaceCar r=null;
        try {
            Connection conn = DatabaseData.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT Id,Class,Tyre,BodyWork,EngineMode,EngineCapacity,EnginePower FROM cars WHERE Id= ?;");
            ps.setInt(1,(Integer) key);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("Id");
                Class<? extends CarClass> c = (Class<? extends CarClass>) Class.forName(rs.getString("Class"));
                Tyre tyre = new Tyre(Tyre.TyreType.valueOf(rs.getString("Tyre")));
                BodyWork bodyWork = new BodyWork(BodyWork.DownforcePackage.valueOf(rs.getString("BodyWork")));
                Engine.EngineMode eM = Engine.EngineMode.valueOf(rs.getString("EngineMode"));
                Engine engineC = new Engine(rs.getInt("EngineCapacity"), 0, eM, CarPart.CarPartType.COMBUSTION_ENGINE);

                Engine engineE = null;
                Integer ePow = rs.getInt("EnginePower");
                if (!rs.wasNull()) {
                    engineE = new Engine(0, ePow, eM, CarPart.CarPartType.ELECTRIC_ENGINE);
                }
                r = new RaceCar(id, c, tyre, bodyWork, engineC, engineE);
            }

        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        } catch (EngineCannotHaveCapacityAndNotBeCombustionException | ClassNotFoundException |
                 EngineCannotBeEletricAndCombustionSimultaneousException |
                 EngineCannotHavePowerAndNotBeElectricException e) {
            throw new RuntimeException(e);
        }
        return r;
    }

    /**
     * adds a RaceCar to the database or updates its password if it already exists
     *
     * @param key key with which the specified value is to be associated
     * @param car value to be associated with the specified key
     * @return the RaceCar if added successfully (null otherwise)
     */
    @Override
    public RaceCar put(Integer key, RaceCar car) {
        try {
            Connection conn = DatabaseData.getConnection();
            String sql="";
            if (key==null){
                sql="INSERT INTO cars (Class,Tyre,BodyWork,EngineMode,EngineCapacity,EnginePower) VALUES (?,?,?,?,?,?);";
            }else{
                sql="INSERT INTO cars (Id,Class,Tyre,BodyWork,EngineMode,EngineCapacity,EnginePower) VALUES (?,?,?,?,?,?,?);";
            }

            PreparedStatement ps = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            Map<CarPart.CarPartType,CarPart> parts = car.getParts();
            int n=1;
            if (key!=null){
                ps.setInt(n,car.getId());
                n++;
            }
            ps.setString(n,car.getCategory().getName());
            n++;

            ps.setString(n,((Tyre)parts.get(CarPart.CarPartType.TYRE)).getTyreType().name());
            n++;
            ps.setString(n,((BodyWork)parts.get(CarPart.CarPartType.BODYWORK)).getDfPackage().name());
            n++;
            ps.setString(n,((Engine)parts.get(CarPart.CarPartType.COMBUSTION_ENGINE)).getMode().name());
            n++;
            ps.setInt(n,((Engine)parts.get(CarPart.CarPartType.COMBUSTION_ENGINE)).getCapacity());
            n++;
            if (parts.containsKey(CarPart.CarPartType.ELECTRIC_ENGINE)){
                ps.setInt(n,((Engine)parts.get(CarPart.CarPartType.ELECTRIC_ENGINE)).getPower());
                }
            else{
                ps.setNull(n, Types.INTEGER);
                }
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()){
                car.setId(rs.getInt(1));
            }
            return car;
        } catch (SQLException e) {
            return null;
        }
    }

    /**
     * adds a RaceCar to the database or updates its password if it already exists
     *
     * @param key key with which the specified value is to be associated
     * @param car value to be associated with the specified key
     * @return the RaceCar if added successfully (null otherwise)
     */

    public RaceCar update(Integer key, RaceCar car) {
        try {
            Connection conn = DatabaseData.getConnection();
            String sql="UPDATE cars SET " +
                    "Class=?," +
                    "Tyre=?," +
                    "BodyWork=?," +
                    "EngineMode=?," +
                    "EngineCapacity=?," +
                    "EnginePower=? " +
                    "WHERE Id=?;";

            PreparedStatement ps = conn.prepareStatement(sql);
            Map<CarPart.CarPartType,CarPart> parts = car.getParts();
            ps.setString(1,car.getCategory().getName());

            ps.setString(2,((Tyre)parts.get(CarPart.CarPartType.TYRE)).getTyreType().name());

            ps.setString(3,((BodyWork)parts.get(CarPart.CarPartType.BODYWORK)).getDfPackage().name());

            ps.setString(4,((Engine)parts.get(CarPart.CarPartType.COMBUSTION_ENGINE)).getMode().name());

            ps.setInt(5,((Engine)parts.get(CarPart.CarPartType.COMBUSTION_ENGINE)).getCapacity());

            if (parts.containsKey(CarPart.CarPartType.ELECTRIC_ENGINE)){
                ps.setInt(6,((Engine)parts.get(CarPart.CarPartType.ELECTRIC_ENGINE)).getPower());
            }
            else{
                ps.setNull(6, Types.INTEGER);
            }
            ps.setInt(7,key);
            ps.executeUpdate();
            return car;
        } catch (SQLException e) {
            return null;
        }
    }


    public RaceCar put(RaceCar u){
        return put(u.getId(),u);
    }

    /**
     *
     * Removes a user with a given username from the database if it exists
     *
     * @param key key whose mapping is to be removed from the map
     * @return the user removed if it was possible to remove it (null otherwise)
     */
    @Override
    public RaceCar remove(Object key) {
        try {
            RaceCar car = this.get(key);
            if (car==null){
                return null;
            }
            Connection conn = DatabaseData.getConnection();
            PreparedStatement ps = conn.prepareStatement("DELETE FROM cars WHERE Id = ?;");
            ps.setInt(1,(int) key);
            ps.executeUpdate();
            return car;
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends RaceCar> m) {
        return;
    }


    @Override
    public void clear() {
        try {
            Connection conn = DatabaseData.getConnection();
            Statement stm = conn.createStatement();
            stm.executeUpdate("DELETE FROM cars;");
        } catch (SQLException e) {
            throw new RuntimeException(e);//TODO MUDAR ISTO
        }
    }
    @Override
    public Set<Integer> keySet() {
        Set<Integer> r=new HashSet<>();
        try {
            Connection conn = DatabaseData.getConnection();
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT Id FROM cars;");
            while(rs.next()){
                r.add(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return r;
    }

    @Override
    public Collection<RaceCar> values() {
        Collection<RaceCar> r = new HashSet<RaceCar>();
        try {
            Connection conn = DatabaseData.getConnection();
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT Id,Class,Tyre,BodyWork,EngineMode,EngineCapacity,EnginePower FROM cars;");
            while(rs.next()){
                int id = rs.getInt("Id");
                Class<? extends CarClass> c = (Class<? extends CarClass>) Class.forName(rs.getString("Class"));
                Tyre tyre = new Tyre(Tyre.TyreType.valueOf(rs.getString("Tyre")));
                BodyWork bodyWork = new BodyWork(BodyWork.DownforcePackage.valueOf(rs.getString("BodyWork")));
                Engine.EngineMode eM = Engine.EngineMode.valueOf(rs.getString("EngineMode"));
                Engine engineC = new Engine(rs.getInt("EngineCapacity"), 0, eM, CarPart.CarPartType.COMBUSTION_ENGINE);
                Engine engineE = null;
                Integer ePow = rs.getInt("EnginePower");
                if (!rs.wasNull()) {
                    engineE = new Engine(0, ePow, eM, CarPart.CarPartType.ELECTRIC_ENGINE);
                }
                r.add(new RaceCar(id, c, tyre, bodyWork, engineC, engineE));
            }
        } catch (SQLException | EngineCannotHaveCapacityAndNotBeCombustionException |
                 EngineCannotBeEletricAndCombustionSimultaneousException |
                 EngineCannotHavePowerAndNotBeElectricException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return r;
    }

    @Override
    public Set<Entry<Integer, RaceCar>> entrySet() {
        Map<Integer,RaceCar> r = new HashMap<>();
        try {
            Connection conn = DatabaseData.getConnection();
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT Id,Class,Tyre,BodyWork,EngineMode,EngineCapacity,EnginePower FROM cars;");
            while(rs.next()){
                int id = rs.getInt("Id");
                Class<? extends CarClass> c = (Class<? extends CarClass>) Class.forName(rs.getString("Class"));
                Tyre tyre = new Tyre(Tyre.TyreType.valueOf(rs.getString("Tyre")));
                BodyWork bodyWork = new BodyWork(BodyWork.DownforcePackage.valueOf(rs.getString("BodyWork")));
                Engine.EngineMode eM = Engine.EngineMode.valueOf(rs.getString("EngineMode"));
                Engine engineC = new Engine(rs.getInt("EngineCapacity"), 0, eM, CarPart.CarPartType.COMBUSTION_ENGINE);
                Engine engineE = null;
                Integer ePow = rs.getInt("EnginePower");
                if (!rs.wasNull()) {
                    engineE = new Engine(0, ePow, eM, CarPart.CarPartType.ELECTRIC_ENGINE);
                }
                r.put(id,new RaceCar(id, c, tyre, bodyWork, engineC, engineE));
            }
        } catch (SQLException | ClassNotFoundException | EngineCannotHavePowerAndNotBeElectricException |
                 EngineCannotBeEletricAndCombustionSimultaneousException |
                 EngineCannotHaveCapacityAndNotBeCombustionException e) {
            throw new RuntimeException(e);
        }
        return r.entrySet();
    }

    @Override
    public boolean equals(Object o) {
        return this==o;
    }

    @Override
    public int hashCode(){
        return this.size();
    }


}
