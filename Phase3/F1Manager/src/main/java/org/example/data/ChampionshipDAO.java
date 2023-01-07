package org.example.data;

import org.example.business.Championship;
import org.example.business.users.Admin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ChampionshipDAO implements Map<Integer, Championship> {
    private static ChampionshipDAO singleton = null;
    private static AdminDAO adb = AdminDAO.getInstance();

    private ChampionshipDAO() {
        try (
                Connection conn = DatabaseData.getConnection();
                Statement stm = conn.createStatement();
        ) {
            String sql = "CREATE TABLE IF NOT EXISTS championships (" +
                    "Id INT AUTO_INCREMENT PRIMARY KEY," +
                    "Admin VARCHAR(255) NOT NULL,"+
                    "FOREIGN KEY (Admin) REFERENCES users(Username) ON DELETE CASCADE ON UPDATE CASCADE" +
                    ");";
            stm.executeUpdate(sql);
        } catch (SQLException e) {
            // Erro a criar tabela...
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    public static ChampionshipDAO getInstance() {
        if (singleton==null)
            singleton=new ChampionshipDAO();
        return singleton;
    }

    @Override
    public int size() {
        int i = 0;
        try (Connection conn = DatabaseData.getConnection();
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT COUNT(*) FROM championships;");){
            if (rs.next())
                i = rs.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return i;
    }

    @Override
    public boolean isEmpty() {
        return size()==0;
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) return false;
        boolean r=false;
        try (Connection conn = DatabaseData.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT Id FROM championships WHERE Id= ?;");){
            ps.setInt(1,(Integer) key);
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
    public boolean containsValue(Object value) {
        if (!(value instanceof Championship)) return false;
        Championship p = (Championship) value;
        return p.equals(get(p.getId()));
    }

    @Override
    public Championship get(Object key) {
        if (!(key instanceof Integer)) return null;
        try (Connection conn = DatabaseData.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT Id,Admin FROM championships WHERE Id= ?;");
        ) {
            ps.setInt(1,(Integer)key);
            try (ResultSet rs = ps.executeQuery();){
                if (rs.next())
                    return new Championship(
                            rs.getInt("Id"),
                            adb.get(rs.getString("Admin"))
                    );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Nullable
    @Override
    public Championship put(Integer key, Championship value) {
        String sql = "";
        if (key == null) {
            sql = "INSERT INTO championships (Admin) VALUES (?);";
        } else {
            sql = "INSERT INTO championships (Id,Admin) VALUES (?,?);";
        }
        try (Connection conn = DatabaseData.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ) {
            int n = 1;
            if (key != null) {
                ps.setInt(n, value.getId());
                n++;
            }
            ps.setString(n, value.getAdmin().getUsername());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys();) {
                if (rs.next())
                    value.setId(rs.getInt(1));
            }
            return value;
        } catch (SQLException e) {
            return null;
        }
    }

    public Championship put(Championship value) {
        return put(value.getId(),value);
    }


    @Override
    public Championship remove(Object key) {
        Championship cha = get(key);
        if (cha == null)
            return null;
        try (Connection conn = DatabaseData.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM championships WHERE Id = ?;");
        ) {
            ps.setInt(1, (int) key);
            ps.executeUpdate();
            return cha;
        } catch (SQLException e) {
            return null;
        }
    }


    @Override
    public void putAll(@NotNull Map<? extends Integer, ? extends Championship> m) {

        try (Connection conn = DatabaseData.getConnection();) {
            conn.setAutoCommit(false);
            String sql="";
            for (Entry e : m.entrySet()) {
                if (e.getKey()!=null) sql="INSERT INTO championships (Id,Admin) VALUES (?,?);";
                else sql="INSERT INTO championships (Admin) VALUES (?);";
            try (PreparedStatement ps = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)) {
                int n = 1;
                if (e.getKey() != null) {
                    ps.setInt(n, (Integer) e.getKey());
                    n++;
                }
                ps.setString(n, ((Championship)e.getValue()).getAdmin().getUsername());
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys();) {
                    if (rs.next())
                        ((Championship)e.getValue()).setId(rs.getInt(1));
                }
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
             Statement stm = conn.createStatement();
        ) {
            stm.executeUpdate("DELETE FROM championships;");
        } catch (SQLException e) {
            throw new RuntimeException(e);//TODO MUDAR ISTO
        }
    }

    @NotNull
    @Override
    public Set<Integer> keySet() {
        Set<Integer> r = new HashSet<>();
        try (Connection conn = DatabaseData.getConnection();
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT Id FROM championships;");
        ) {
            while (rs.next())
                r.add(rs.getInt(1));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return r;
    }

    @NotNull
    @Override
    public Collection<Championship> values() {
        Collection<Championship> r = new HashSet<Championship>();
        try (Connection conn = DatabaseData.getConnection();
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT Id,Admin FROM championships;");
        ) {
            while (rs.next())
                r.add(new Championship(rs.getInt("Id"),
                        adb.get(rs.getString("Admin"))));
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
        return r;
    }

    @NotNull
    @Override
    public Set<Entry<Integer, Championship>> entrySet() {
        return values().stream().collect(
                Collectors.toMap(Championship::getId, x -> x)).entrySet();
    }
}
