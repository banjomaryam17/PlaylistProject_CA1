package persistence;

import entities.Genre;
import java.sql.*;
import java.util.ArrayList;
import  java.util.List;



/**
 * Implementation of GenreDAO interface using JDBC.
 * Handles all database operations for Genre entity.
 *
 *
 */
public class GenreDaoImpl implements GenreDao {

    @Override
    public Genre create(Genre genre) throws SQLException {
        // Create variables to hold database details
        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://127.0.0.1:3306/PLAYLISTSMODELS";
        String username = "root";
        String password = "root";

        try {
            // Load the database driver
            Class.forName(driver);
            // TRY to get a connection to the database
            try (Connection conn = DriverManager.getConnection(url, username, password)) {
                try (PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO genres (genreName, description) VALUES (?, ?)",
                        Statement.RETURN_GENERATED_KEYS)) {

                    // Fill in the blanks, i.e. parameterize the query
                    ps.setString(1, genre.getGenreName());
                    ps.setString(2, genre.getDescription());

                    // Execute the operation
                    int rowsAffected = ps.executeUpdate();

                    if (rowsAffected == 0) {
                        throw new SQLException("Creating genre failed, no rows affected.");
                    }

                    // Get the generated key (ID)
                    try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            genre.setGenreID(generatedKeys.getInt(1));
                        } else {
                            throw new SQLException("Creating genre failed, no ID obtained.");
                        }
                    } catch (SQLException e) {
                        System.out.println("SQL Exception occurred when getting generated keys.");
                        System.out.println("Error: " + e.getMessage());
                    }

                } catch (SQLException e) {
                    System.out.println("SQL Exception occurred when attempting to prepare/execute SQL.");
                    System.out.println("Error: " + e.getMessage());
                    throw e;
                }
            } catch (SQLException e) {
                System.out.println("SQL Exception occurred when attempting to connect to database.");
                System.out.println(e.getMessage());
                throw e;
            }
        } catch (ClassNotFoundException e) {
            System.out.println("ClassNotFoundException occurred when trying to load driver: " + e.getMessage());
            throw new SQLException("Database driver not found", e);
        }

        return genre;
    }

    @Override
    public Genre findById(int genreID) throws SQLException {
        Genre genre = null;

        // Create variables to hold database details
        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://127.0.0.1:3306/PLAYLISTSMODELS";
        String username = "root";
        String password = "root";

        try {
            Class.forName(driver);
            try (Connection conn = DriverManager.getConnection(url, username, password)) {
                try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM genres WHERE genreID = ?")) {
                    // Fill in the blanks, i.e. parameterize the query
                    ps.setInt(1, genreID);

                    // TRY to execute the query
                    try (ResultSet rs = ps.executeQuery()) {
                        // Extract the information from the result set
                        if (rs.next()) {
                            // Get the pieces of a genre from the resultset and create a new Genre using Builder
                            genre = Genre.builder()
                                    .genreID(rs.getInt("genreID"))
                                    .genreName(rs.getString("genreName"))
                                    .description(rs.getString("description"))
                                    .build();
                        }
                    } catch (SQLException e) {
                        System.out.println("SQL Exception occurred when executing SQL or processing results.");
                        System.out.println("Error: " + e.getMessage());
                    }
                } catch (SQLException e) {
                    System.out.println("SQL Exception occurred when attempting to prepare SQL for execution");
                    System.out.println("Error: " + e.getMessage());
                }
            } catch (SQLException e) {
                System.out.println("SQL Exception occurred when attempting to connect to database.");
                System.out.println(e.getMessage());
            }
        } catch (ClassNotFoundException e) {
            System.out.println("ClassNotFoundException occurred when trying to load driver: " + e.getMessage());
        }

        return genre;
    }

    @Override
    public List<Genre> findAll() throws SQLException {
        // Create variable to hold the genre info from the database
        List<Genre> genres = new ArrayList<>();

        // Create variables to hold database details
        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://127.0.0.1:3306/PLAYLISTSMODELS";
        String username = "root";
        String password = "root";

        try {
            // Load the database driver
            Class.forName(driver);
            // TRY to get a connection to the database
            try (Connection conn = DriverManager.getConnection(url, username, password)) {
                // Get a statement from the connection
                try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM genres ORDER BY genreName")) {
                    // Execute the query
                    try (ResultSet rs = ps.executeQuery()) {
                        // Loop through the result set
                        while (rs.next()) {
                            // Get the pieces of a genre from the resultset and create a new Genre
                            Genre g = Genre.builder()
                                    .genreID(rs.getInt("genreID"))
                                    .genreName(rs.getString("genreName"))
                                    .description(rs.getString("description"))
                                    .build();

                            genres.add(g);
                        }
                    } catch (SQLException e) {
                        System.out.println("SQL Exception occurred when executing SQL or processing results.");
                        System.out.println("Error: " + e.getMessage());
                    }
                } catch (SQLException e) {
                    System.out.println("SQL Exception occurred when attempting to prepare SQL for execution");
                    System.out.println("Error: " + e.getMessage());
                }
            } catch (SQLException e) {
                System.out.println("SQL Exception occurred when attempting to connect to database.");
                System.out.println(e.getMessage());
            }
        } catch (ClassNotFoundException e) {
            System.out.println("ClassNotFoundException occurred when trying to load driver: " + e.getMessage());
        }
        return genres;
    }

    @Override
    public boolean update(Genre genre) throws SQLException {
        int rowsAffected = 0;

        // Create variables to hold database details
        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://127.0.0.1:3306/PLAYLISTSMODELS";
        String username = "root";
        String password = "root";

        try {
            // Load the database driver
            Class.forName(driver);
            // TRY to get a connection to the database
            try (Connection conn = DriverManager.getConnection(url, username, password)) {
                try (PreparedStatement ps = conn.prepareStatement(
                        "UPDATE genres SET genreName = ?, description = ? WHERE genreID = ?")) {
                    // Fill in the blanks, i.e. parameterize the query
                    ps.setString(1, genre.getGenreName());
                    ps.setString(2, genre.getDescription());
                    ps.setInt(3, genre.getGenreID());

                    // Execute the operation
                    // Remember that when you are doing an update, a delete or an insert,
                    // your only result will be a number indicating how many rows were affected
                    rowsAffected = ps.executeUpdate();
                } catch (SQLException e) {
                    System.out.println("SQL Exception occurred when attempting to prepare/execute SQL.");
                    System.out.println("Error: " + e.getMessage());
                }
            } catch (SQLException e) {
                System.out.println("SQL Exception occurred when attempting to connect to database.");
                System.out.println(e.getMessage());
            }
        } catch (ClassNotFoundException e) {
            System.out.println("ClassNotFoundException occurred when trying to load driver: " + e.getMessage());
        }

        return rowsAffected > 0;
    }

    @Override
    public boolean delete(int genreID) throws SQLException {
        int rowsAffected = 0;

        // Create variables to hold database details
        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://127.0.0.1:3306/PLAYLISTSMODELS";
        String username = "root";
        String password = "root";

        try {
            // Load the database driver
            Class.forName(driver);
            // TRY to get a connection to the database
            try (Connection conn = DriverManager.getConnection(url, username, password)) {
                try (PreparedStatement ps = conn.prepareStatement("DELETE FROM genres WHERE genreID = ?")) {
                    // Fill in the blanks, i.e. parameterize the query
                    ps.setInt(1, genreID);

                    // Execute the operation
                    // Remember that when you are doing an update, a deleted or an insert,
                    // your only result will be a number indicating how many rows were affected
                    rowsAffected = ps.executeUpdate();
                } catch (SQLException e) {
                    System.out.println("SQL Exception occurred when attempting to prepare/execute SQL.");
                    System.out.println("Error: " + e.getMessage());
                }
            } catch (SQLException e) {
                System.out.println("SQL Exception occurred when attempting to connect to database.");
                System.out.println(e.getMessage());
            }
        } catch (ClassNotFoundException e) {
            System.out.println("ClassNotFoundException occurred when trying to load driver: " + e.getMessage());
        }

        return rowsAffected > 0;
    }
}