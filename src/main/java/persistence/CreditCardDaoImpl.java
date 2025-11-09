package persistence;

import entities.CreditCard;
import java.sql.*;
import java.util.*;

/**
 * @author ShaunGuiden
 */
public class CreditCardDaoImpl implements CreditCardDao {


    //connection to database
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL    = "jdbc:mysql://127.0.0.1:3306/PLAYLISTSMODELS";
    private static final String USER   = "root";
    private static final String PASSWORD   = "root";

    static {
        try {
            Class.forName(DRIVER);
            System.out.println("MySQL JDBC Driver loaded successfully");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL Driver not found: " + e.getMessage());
        }
    }

    /**
     * gets connection to database
     * @return
     * @throws SQLException
     */
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * adds credit card to database
     * @param card the creditcard class object you want to add
     */
    @Override
    public void addCreditCard(CreditCard card) {
        String sql = "INSERT INTO creditcard (username, last4, brand, expmonth, expyear) VALUES (?,?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, card.getUsername());
            stmt.setString(2, card.getLast4());
            stmt.setString(3, card.getBrand());
            stmt.setInt(4, card.getExpMonth());
            stmt.setInt(5, card.getExpYear());
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error adding credit card: " + e.getMessage());
        }
    }

    /**
     * if the resultset is empty it will truen false and if it contains objects it will return the dataset with a true
     * @param username
     * @return
     */
    @Override
    public Optional<CreditCard> getCreditCardByUsername(String username) {
        String sql = "SELECT * FROM creditcard WHERE username = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToCard(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error fetching credit card: " + e.getMessage());
        }
        return Optional.empty();
    }

    /**
     * gets a list of all credit cards in the database
     * @return List of Creditcards
     */
    @Override
    public List<CreditCard> getAllCreditCards() {
        List<CreditCard> cards = new ArrayList<>();
        String sql = "SELECT * FROM creditcard";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                cards.add(mapResultSetToCard(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error fetching credit cards: " + e.getMessage());
        }
        return cards;
    }

    /**
     * deletes creditcard with the given username
     * @param username
     */
    @Override
    public void deleteCreditCard(String username) {
        String sql = "DELETE FROM creditcard WHERE username = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error deleting credit card: " + e.getMessage());
        }
    }

    /**
     * updates the creditcard information
     * @param card class object
     * @return boolean based on it it was updated
     */
    @Override
    public boolean updateCreditCard(CreditCard card) {
        String sql = "UPDATE creditcard SET last4=?, brand=?, expmonth=?, expyear=? WHERE username=?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, card.getLast4());
            stmt.setString(2, card.getBrand());
            stmt.setInt(3, card.getExpMonth());
            stmt.setInt(4, card.getExpYear());
            stmt.setString(5, card.getUsername());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error updating credit card: " + e.getMessage());
        }
        return false;
    }

    /**
     * returns a credit card using the lombok builder
     * @param res
     * @return creditcard class object
     * @throws SQLException
     */
    private CreditCard mapResultSetToCard(ResultSet res) throws SQLException {
        return CreditCard.builder()
                .username(res.getString("username"))
                .last4(res.getString("last4"))
                .brand(res.getString("brand"))
                .expMonth(res.getInt("expmonth"))
                .expYear(res.getInt("expyear"))
                .build();
    }
}
