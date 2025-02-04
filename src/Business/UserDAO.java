package Business;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class UserDAO implements Map<String, Utilizador> {

    public void clear() {
        Connection conn = DBConnect.connect();
        try {
            Statement stm = conn.createStatement();
            stm.executeUpdate("DELETE FROM Utilizadores");
        } catch (Exception e) {
            throw new NullPointerException(e.getMessage());
        } finally {
            DBConnect.close(conn);
        }
    }

    public boolean containsKey(Object key) throws NullPointerException {
        Connection conn = DBConnect.connect();
        try {
            Statement stm = conn.createStatement();
            String sql = "SELECT name FROM Utilizadores WHERE email='" + key +
                    "'";
            ResultSet rs = stm.executeQuery(sql);
            return rs.next();
        } catch (Exception e) {
            throw new NullPointerException(e.getMessage());
        } finally {
            DBConnect.close(conn);
        }
    }

    public boolean containsValue(Object value) {
        throw new NullPointerException("public boolean containsValue(Object value) not implemented!");
    }

    public Set<Map.Entry<String, Utilizador>> entrySet() {
        throw new NullPointerException("public Set<Map.Entry<String,Aluno>> entrySet() not implemented!");
    }

    public boolean equals(Object o) {
        throw new NullPointerException("public boolean equals(Object o) not implemented!");
    }

    public Utilizador get(Object key) {
        Connection conn = DBConnect.connect();
        try {
            Utilizador al = null;
            Statement stm = conn.createStatement();
            String sql = "SELECT * FROM Utilizadores WHERE email='" + key + "'";
            ResultSet rs = stm.executeQuery(sql);
            if (rs.next())
                if (rs.getBoolean(4))
                    al = new Administrador(rs.getString(1), rs.getString(2),
                            rs.getString(3));
                else
                    al = new Utilizador(rs.getString(1), rs.getString(2),
                            rs.getString(3));
            return al;
        } catch (Exception e) {
            throw new NullPointerException(e.getMessage());
        } finally {
            DBConnect.close(conn);
        }
    }

    public int hashCode() {
        //return this.inst.hashCode();
        return 0;
    }

    public boolean isEmpty() {
        Connection conn = DBConnect.connect();
        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT name FROM Utilizadores");
            return !rs.next();
        } catch (Exception e) {
            throw new NullPointerException(e.getMessage());
        } finally {
            DBConnect.close(conn);
        }
    }

    public Set<String> keySet() {
        throw new NullPointerException("Not implemented!");
    }

    public Utilizador put(String key, Utilizador value) {
        Connection conn = DBConnect.connect();
        try {
            Utilizador al = null;
            Statement stm = conn.createStatement();
            if (this.containsKey(key)) {
                stm.executeUpdate("UPDATE Utilizadores SET " +
                        "name = '" + value.getName() +
                        "', passwd = '" + value.getPasswd() +
                        "'WHERE email = '" + key + "'");
                return value;
            }
            String sql;
            if (value.getPasswd() != null) {
                sql =
                        "INSERT INTO Utilizadores VALUES ('" + key + "','" + value.getName() +
                                "','" + value.getPasswd() + "'," + value.isAdmin() + ")";
            } else {
                sql =
                        "INSERT INTO Utilizadores (email, name, admin) VALUES" +
                                " ('" + key + "'," +
                                "'" + value.getName() +
                                "'," + value.isAdmin() + ")";
            }
            int i = stm.executeUpdate(sql);
            return new Utilizador(value.getEmail(), value.getName(), value.getPasswd());
        } catch (Exception e) {
            throw new NullPointerException(e.getMessage());
        } finally {
            DBConnect.close(conn);
        }
    }

    public void putAll(Map<? extends String, ? extends Utilizador> t) {
        throw new NullPointerException("Not implemented!");
    }

    public Utilizador remove(Object key) {
        Connection conn = DBConnect.connect();
        try {
            Utilizador al = this.get(key);
            Statement stm = conn.createStatement();
            String sql = "delete from Playlist where `Utilizadores_email` = '" +
                    key + "';" +
                    "delete from Media where `owner` = '" + key + "';" +
                    "DELETE FROM Utilizadores WHERE (`email` = " +
                    "'" + key + "');";
            int i = stm.executeUpdate(sql);
            return al;
        } catch (Exception e) {
            throw new NullPointerException(e.getMessage());
        } finally {
            DBConnect.close(conn);
        }
    }

    public int size() {
        Connection conn = DBConnect.connect();
        try {
            int i = 0;
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT name FROM Utilizadores");
            for (; rs.next(); i++) ;
            return i;
        } catch (Exception e) {
            throw new NullPointerException(e.getMessage());
        } finally {
            DBConnect.close(conn);
        }
    }

    public Collection<Utilizador> values() {
        Connection conn = DBConnect.connect();
        try {
            Collection<Utilizador> col = new HashSet<>();
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM Utilizadores");
            for (; rs.next(); ) {
                col.add(new Utilizador(rs.getString(1), rs.getString(2),
                        rs.getString(3)));
            }
            return col;
        } catch (Exception e) {
            throw new NullPointerException(e.getMessage());
        } finally {
            DBConnect.close(conn);
        }
    }
}


