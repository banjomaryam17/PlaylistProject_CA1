package persistence;
import entities.User;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLException;


@Slf4j
public class UserDaolmpl extends MySqlConnector implements UserDao {

    public UserDaolmpl() { super(); }

    // Pass a properties file on the classpath e.g "dbproperties"
    public UserDaolmpl(String propertiesFilename) { super(propertiesFilename); }

    /** Map a row to a User */
    private User mapRow(ResultSet rs) throws SQLException {
        return new User(
                rs.getString("username"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getInt("userType")
        );
    }

    @Override
    public boolean LoginUser(String email, String password) {
        Connection conn = super.getConnection();
        final String sql = "SELECT 1 FROM users WHERE email = ? AND password = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            log.error("LoginUser: SQL error", e);
            return false;
        } finally {
            super.freeConnection();
        }
    }

    @Override
    public User findUserByUsername(String username) {
        Connection conn = super.getConnection();
        final String sql = "SELECT username, email, password, userType FROM users WHERE username = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        } catch (SQLException e) {
            log.error("findUserByUsername: SQL error", e);
            return null;
        } finally {
            super.freeConnection();
        }
    }

    @Override
    public int registerUser(User newuser) {
        Connection conn = super.getConnection();
        final String sql = "INSERT INTO users (username, email, password, userType) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newuser.getUsername());
            ps.setString(2, newuser.getEmail());
            ps.setString(3, newuser.getPassword());
            ps.setInt(4, newuser.getUserType());
            return ps.executeUpdate(); // typically 1
        } catch (SQLIntegrityConstraintViolationException dup) {
            log.warn("registerUser: duplicate username/email", dup);
            return -1;
        } catch (SQLException e) {
            log.error("registerUser: SQL error", e);
            return 0;
        } finally {
            super.freeConnection();
        }
    }

    @Override
    public User findByThereEmail(User email) {
        if (email == null || email.getEmail() == null) return null;

        Connection conn = super.getConnection();
        final String sql = "SELECT username, email, password, userType FROM users WHERE email = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email.getEmail());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        } catch (SQLException e) {
            log.error("findByThereEmail: SQL error", e);
            return null;
        } finally {
            super.freeConnection();
        }
    }

    @Override
    public User Login(String username, String password) {
        Connection conn = super.getConnection();
        final String sql = "SELECT username, email, password, userType FROM users WHERE username = ? AND password = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        } catch (SQLException e) {
            log.error("Login: SQL error", e);
            return null;
        } finally {
            super.freeConnection();
        }
    }

    @Override
    public boolean updateUserPassword(String password, String username) throws RuntimeException {
        Connection conn = super.getConnection();
        final String sql = "UPDATE users SET password = ? WHERE username = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, password);
            ps.setString(2, username);
            int rows = ps.executeUpdate();
            if (rows > 1) throw new RuntimeException("More than one row updated for this username.");
            return rows == 1;
        } catch (SQLException e) {
            log.error("updateUserPassword: SQL error", e);
            return false;
        } finally {
            super.freeConnection();
        }
    }
}