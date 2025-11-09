package persistence;

import entities.Rating;
import java.sql.*;
import java.util.*;

/**
 * @author ShaunGuiden
 */
public class RatingDaoImpl implements RatingDao {

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

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * add the song id and then add a rating of 1-5
     * @param songId - current song
     * @param score - the rating you give it
     */
    @Override
    public void addRating(int songId, int score) {
        // Try updating existing rating first
        String updateSql = "UPDATE rating SET currentScore = currentScore + score, max_score = max_score + 5 WHERE song_id = ?";
        String insertSql = "INSERT INTO rating (songId, currentScore, maxScore) VALUES (?, ?, 5)";

        try (Connection conn = getConnection()) {
            PreparedStatement updateStmt = conn.prepareStatement(updateSql);
            updateStmt.setInt(1, score);
            updateStmt.setInt(2, songId);

            int rowsUpdated = updateStmt.executeUpdate();

            if (rowsUpdated == 0) {
                PreparedStatement insertStmt = conn.prepareStatement(insertSql);
                insertStmt.setInt(1, songId);
                insertStmt.setInt(2, score);
                insertStmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("Error adding rating: " + e.getMessage());
        }
    }

    /**
     * Returns a false if isEmpty and a true if not
     * @param songId selected songs songid
     * @return if its empty it just returns false, if not it returns true if the results set has objects in it
     */
    @Override
    public Optional<Rating> getRatingBySongId(int songId) {
        String sql = "SELECT * FROM rating WHERE songId = ?";
        try (Connection conn = getConnection();
             PreparedStatement sqlStatement = conn.prepareStatement(sql)) {

            sqlStatement.setInt(0, songId);
            ResultSet res = sqlStatement.executeQuery();

            if (res.next()) {
                return Optional.of(mapResultSetToRating(res));
            }

        } catch (SQLException e) {
            System.err.println("Error fetching rating: " + e.getMessage());
        }
        return Optional.empty();
    }

    /**
     * gets all ratings from the rating database
     * @return a list of ratings
     */
    @Override
    public List<Rating> getAllRatings() {
        List<Rating> ratings = new ArrayList<>();
        String sql = "SELECT * FROM ratings";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                ratings.add(mapResultSetToRating(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error fetching ratings: " + e.getMessage());
        }
        return ratings;
    }

    /**
     * Deletes rating for the song relating to the current songId
     * @param songId seleted songs songId
     */
    @Override
    public void deleteRating(int songId) {
        String sql = "DELETE FROM rating WHERE songId = ?";
        try (Connection conn = getConnection();
             PreparedStatement sqlStatement = conn.prepareStatement(sql)) {

            sqlStatement.setInt(1, songId);
            sqlStatement.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error deleting rating: " + e.getMessage());
        }
    }

    /**
     * Get average score for a specific song
     * @param songId
     * @return double based on the average of the selected songs max and current
     */
    @Override
    public double getAverageScore(int songId) {
        String sql = "SELECT currentScore, maxScore FROM rating WHERE songId = ?";
        try (Connection conn = getConnection();
             PreparedStatement sqlStatement = conn.prepareStatement(sql)) {

            sqlStatement.setInt(1, songId);
            ResultSet res = sqlStatement.executeQuery();

            if (res.next()) {
                int current = res.getInt("currentScore");
                int max = res.getInt("maxScore");
                return max == 0 ? 0.0 : ((double) current / max) * 5;
            }

        } catch (SQLException e) {
            System.err.println("Error calculating average score: " + e.getMessage());
        }
        return 0.0;
    }

    /**
     * Maps resultSet to the database
     * @param res - input ResultSet
     * @return - using lomboks builder it creates a Ratings class from the ResultSet
     * @throws SQLException
     */
    private Rating mapResultSetToRating(ResultSet res) throws SQLException {
        return Rating.builder()
                .songId(res.getInt("songId"))
                .currentScore(res.getInt("currentScore"))
                .maxScore(res.getInt("maxScore"))
                .build();
    }
}
