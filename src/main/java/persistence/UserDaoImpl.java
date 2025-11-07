package persistence;

import entities.User;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;

/**
 * Implementation of UserDao interface using JDBC.
 * Handles all database operations for User entity.
 * Uses Connector for database connection management.
 *
 * @author [Your Name]
 */
@Slf4j
public class UserDaoImpl implements UserDao {
    private Connector connector;

    public UserDaoImpl(Connector connector) {
        this.connector = connector;
    }

    public void closeConnection() {
        connector.freeConnection();
    }

    @Override
    public boolean LoginUser(String email, String password) {
        Connection conn = connector.getConnection();
        if (conn == null) {
            log.error("LoginUser(): Could not establish connection to database.");
            return false;
        }

        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM users WHERE email = ? AND password = ?")) {

            ps.setString(1, email);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); // Returns true if user found with matching credentials
            } catch (SQLException e) {
                log.error("LoginUser(): An issue occurred when running the query. Exception: ", e.getMessage());
                return false;
            }
        } catch (SQLException e) {
            log.error("LoginUser() - The SQL query could not be prepared. Exception: ", e.getMessage());
            return false;
        }
    }

    @Override
    public User findUserByUsername(String username) {
        Connection conn = connector.getConnection();
        if (conn == null) {
            log.error("findUserByUsername(): Could not establish connection to database.");
            return null;
        }

        User user = null;
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE username = ?")) {
            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    user = mapUserRow(rs);
                }
            } catch (SQLException e) {
                log.error("findUserByUsername(): An issue occurred when running the query. Exception: ", e.getMessage());
            }
        } catch (SQLException e) {
            log.error("findUserByUsername() - The SQL query could not be prepared. Exception: ", e.getMessage());
        }

        return user;
    }

    @Override
    public int registerUser(User newuser) {
        Connection conn = connector.getConnection();
        if (conn == null) {
            log.error("registerUser(): Could not establish connection to database.");
            return -1;
        }

        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO users (username, email, password, userType) VALUES (?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, newuser.getUsername());
            ps.setString(2, newuser.getEmail());
            ps.setString(3, newuser.getPassword());
            ps.setInt(4, newuser.getUserType());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected == 0) {
                log.error("registerUser(): Creating user failed, no rows affected.");
                return -1;
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1); // Return the generated userID
                } else {
                    log.error("registerUser(): Creating user failed, no ID obtained.");
                    return -1;
                }
            } catch (SQLException e) {
                log.error("registerUser(): An issue occurred when getting generated keys. Exception: ", e.getMessage());
                return -1;
            }

        } catch (SQLException e) {
            log.error("registerUser() - The SQL query could not be prepared. Exception: ", e.getMessage());
            return -1;
        }
    }

    @Override
    public User findByEmail(User email) {
        Connection conn = connector.getConnection();
        if (conn == null) {
            log.error("findByEmail(): Could not establish connection to database.");
            return null;
        }

        User user = null;
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE email = ?")) {
            ps.setString(1, email.getEmail());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    user = mapUserRow(rs);
                }
            } catch (SQLException e) {
                log.error("findByEmail(): An issue occurred when running the query. Exception: ", e.getMessage());
            }
        } catch (SQLException e) {
            log.error("findByEmail() - The SQL query could not be prepared. Exception: ", e.getMessage());
        }

        return user;
    }

    @Override
    public User Login(String username, String password) {
        Connection conn = connector.getConnection();
        if (conn == null) {
            log.error("Login(): Could not establish connection to database.");
            return null;
        }

        User user = null;
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM users WHERE username = ? AND password = ?")) {

            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    user = mapUserRow(rs);
                }
            } catch (SQLException e) {
                log.error("Login(): An issue occurred when running the query. Exception: ", e.getMessage());
            }
        } catch (SQLException e) {
            log.error("Login() - The SQL query could not be prepared. Exception: ", e.getMessage());
        }

        return user;
    }

    @Override
    public boolean updateUserPassword(String password, String username) throws RuntimeException {
        Connection conn = connector.getConnection();
        if (conn == null) {
            throw new RuntimeException("updateUserPassword(): Could not establish connection to database.");
        }

        int rowsAffected = 0;
        try (PreparedStatement ps = conn.prepareStatement(
                "UPDATE users SET password = ? WHERE username = ?")) {

            ps.setString(1, password);
            ps.setString(2, username);

            rowsAffected = ps.executeUpdate();

        } catch (SQLException e) {
            log.error("updateUserPassword() - The SQL query could not be prepared. Exception: ", e.getMessage());
            throw new RuntimeException("Failed to update password: " + e.getMessage());
        }

        return rowsAffected > 0;
    }

    /**
     * Maps a ResultSet row to a User object.
     *
     * @param rs the ResultSet positioned at a user row
     * @return User object populated with data from ResultSet
     * @throws SQLException if error accessing ResultSet data
     */
    private static User mapUserRow(ResultSet rs) throws SQLException {
        return User.builder()
                .username(rs.getString("username"))
                .email(rs.getString("email"))
                .password(rs.getString("password"))
                .userType(rs.getInt("userType"))
                .build();
    }
}