package persistence;

import entities.Genre;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import  java.util.List;




/**
 * Implementation of GenreDAO interface using JDBC.
 * Handles all database operations for Genre entity.
 * Uses Connector for database connection management.
 *
 *
 */
@Slf4j
public class GenreDaoImpl implements GenreDao{
    private Connector connector;

    public GenreDaoImpl(Connector connector) {
        this.connector = connector;
    }

    public void closeConnection() {
        connector.freeConnection();
    }

    @Override
    public Genre create(Genre genre) throws SQLException {
        Connection conn = connector.getConnection();
        if (conn == null) {
            throw new SQLException("create(): Could not establish connection to database.");
        }

        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO genres (genreName, description) VALUES (?, ?)",
                Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, genre.getGenreName());
            ps.setString(2, genre.getDescription());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected == 0) {
                throw new SQLException("Creating genre failed, no rows affected.");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    genre.setGenreID(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating genre failed, no ID obtained.");
                }
            } catch (SQLException e) {
                System.err.println("create(): An issue occurred when getting generated keys. Exception: " + e.getMessage());
                throw e;
            }

        } catch (SQLException e) {
            System.err.println("create() - The SQL query could not be prepared. Exception: " + e.getMessage());
            throw e;
        }

        return genre;
    }

    @Override
    public Genre findById(int genreID) throws SQLException {
        Connection conn = connector.getConnection();
        if (conn == null) {
            throw new SQLException("findById(): Could not establish connection to database.");
        }

        Genre genre = null;
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM genres WHERE genreID = ?")) {
            ps.setInt(1, genreID);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    genre = mapGenreRow(rs);
                }
            } catch (SQLException e) {
                System.err.println("findById(): An issue occurred when running the query or processing the resultset. Exception: " + e.getMessage());
                throw e;
            }
        } catch (SQLException e) {
            System.err.println("findById() - The SQL query could not be prepared. Exception: " + e.getMessage());
            throw e;
        }

        return genre;
    }

    @Override
    public List<Genre> findAll() throws SQLException {
        Connection conn = connector.getConnection();
        if (conn == null) {
            throw new SQLException("findAll(): Could not establish connection to database.");
        }

        ArrayList<Genre> genres = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM genres ORDER BY genreName")) {
            try (ResultSet rs = ps.executeQuery()) {
                // Loop through the result set
                while (rs.next()) {
                    Genre genre = mapGenreRow(rs);
                    genres.add(genre);
                }
            } catch (SQLException e) {
                System.err.println("findAll(): An issue occurred when running the query or processing the resultset. Exception: " + e.getMessage());
                throw e;
            }
        } catch (SQLException e) {
            System.err.println("findAll() - The SQL query could not be prepared. Exception: " + e.getMessage());
            throw e;
        }
        return genres;
    }

    @Override
    public boolean update(Genre genre) throws SQLException {
        Connection conn = connector.getConnection();
        if (conn == null) {
            throw new SQLException("update(): Could not establish connection to database.");
        }

        int rowsAffected = 0;
        try (PreparedStatement ps = conn.prepareStatement(
                "UPDATE genres SET genreName = ?, description = ? WHERE genreID = ?")) {

            ps.setString(1, genre.getGenreName());
            ps.setString(2, genre.getDescription());
            ps.setInt(3, genre.getGenreID());

            rowsAffected = ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("update() - The SQL query could not be prepared. Exception: " + e.getMessage());
            throw e;
        }

        return rowsAffected > 0;
    }

    @Override
    public boolean delete(int genreID) throws SQLException {
        Connection conn = connector.getConnection();
        if (conn == null) {
            throw new SQLException("delete(): Could not establish connection to database.");
        }

        int deletedRows = 0;
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM genres WHERE genreID = ?")) {
            ps.setInt(1, genreID);
            deletedRows = ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("delete() - The SQL query could not be prepared. Exception: " + e.getMessage());
            throw e;
        }

        return deletedRows > 0;
    }

    /**
     * Maps a ResultSet row to a Genre object.
     *
     * @param rs the ResultSet positioned at a genre row
     * @return Genre object populated with data from ResultSet
     * @throws SQLException if error accessing ResultSet data
     */
    private static Genre mapGenreRow(ResultSet rs) throws SQLException {
        return Genre.builder()
                .genreID(rs.getInt("genreID"))
                .genreName(rs.getString("genreName"))
                .description(rs.getString("description"))
                .build();
    }
}