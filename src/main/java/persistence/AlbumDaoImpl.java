package persistence;

import entities.Album;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of AlbumDao interface
 * Handles all database operations for the Album table
 * Uses the shared Connector class for database connections
 *
 * @author Jeremiah
 */
public class AlbumDaoImpl implements AlbumDao {

    private static final Logger logger = LoggerFactory.getLogger(AlbumDaoImpl.class);

    private static final String SELECT_ALL = "SELECT * FROM Album ORDER BY release_date DESC, album_title";

    private static final String SELECT_BY_ID = "SELECT * FROM Album WHERE album_id = ?";

    private static final String SELECT_BY_ARTIST_ID = "SELECT * FROM Album WHERE artistId =? ORDER BY release_date DESC";

    private static final String SEARCH_BY_TITLE = "SELECT * FROM Album WHERE album_title LIKE ? ORDER BY album_title";

    private static final String INSERT_ALBUM = "INSERT INTO Album (artistId, album_title, release_year, album_art_art_url, total_tracks) " +"VALUES (?,?,?,?,?,?)";

    private static final String UPDATE_ALBUM = "UPDATE Album SET artistId = ?, album_title = ?, release_year = ?, album_art_art_url = ?, total_tracks = ? WHERE album_id = ?";

    private static final String DELETE_ALBUM = "DELETE FROM Album WHERE album_id = ?";

    /**
     * Retrieves all albums from the database
     *
     * @return List of all Album objects, ordered by release year (descending) and title
     * @throws Exception if a database error occurs
     */
    @Override
    public List<Album> getAllAlbums() throws Exception {
        logger.info("getAllAlbums() called");
        List<Album> albums = new ArrayList<>();


        try (Connection connection = Connector.getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                albums.add(mapResultSetToAlbum(rs));
            }

            logger.info("Retrieved {} albums", albums.size());
            return albums;

        } catch (SQLException e) {
            logger.error("Failed to retrieve all albums", e);
            throw new Exception("getAllAlbums() failed: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves a specific album by its ID
     *
     * @param albumId the ID of the album to retrieve
     * @return Album object if found, null otherwise
     * @throws Exception if a database error occurs
     */
    @Override
    public Album getAlbumById(int albumId) throws Exception {
        logger.info("getAlbumById() called with ID: {}", albumId);

        try (Connection connection = Connector.getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_BY_ID)) {

            ps.setInt(1, albumId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Album album = mapResultSetToAlbum(rs);
                    logger.info("Found album: {}", album.getAlbumTitle());
                    return album;
                } else {
                    logger.warn("No album found with ID: {}", albumId);
                    return null;
                }
            }

        } catch (SQLException e) {
            logger.error("Failed to retrieve album with ID: {}", albumId, e);
            throw new Exception("getAlbumById() failed: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves all albums for a specific artist
     * CA Requirement: "Viewing all albums for an artist"
     *
     * @param artistId the ID of the artist
     * @return List of Album objects for the specified artist, ordered by release year
     * @throws Exception if a database error occurs
     */
    @Override
    public List<Album> getAlbumsByArtistId(int artistId) throws Exception {
        logger.info("getAlbumsByArtistId() called with artist ID: {}", artistId);
        List<Album> albums = new ArrayList<>();

        try (Connection connection = Connector.getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_BY_ARTIST_ID)) {

            ps.setInt(1, artistId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    albums.add(mapResultSetToAlbum(rs));
                }
            }

            logger.info("Retrieved {} albums for artist ID: {}", albums.size(), artistId);
            return albums;

        } catch (SQLException e) {
            logger.error("Failed to retrieve albums for artist ID: {}", artistId, e);
            throw new Exception("getAlbumsByArtistId() failed: " + e.getMessage(), e);
        }
    }

    /**
     * Searches for albums by title (case-insensitive, partial match)
     * CA Requirement: Part of "Searching for songs by title/artist/album/genre"
     *
     * @param title the album title to search for
     * @return List of Album objects matching the search criteria
     * @throws Exception if a database error occurs
     */
    @Override
    public List<Album> searchAlbumsByTitle(String title) throws Exception {
        logger.info("searchAlbumsByTitle() called with title: {}", title);
        List<Album> albums = new ArrayList<>();

        try (Connection connection = Connector.getConnection();
             PreparedStatement ps = connection.prepareStatement(SEARCH_BY_TITLE)) {

            // Use LIKE with wildcards for partial matching
            ps.setString(1, "%" + title + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    albums.add(mapResultSetToAlbum(rs));
                }
            }

            logger.info("Found {} albums matching title: {}", albums.size(), title);
            return albums;

        } catch (SQLException e) {
            logger.error("Failed to search albums by title: {}", title, e);
            throw new Exception("searchAlbumsByTitle() failed: " + e.getMessage(), e);
        }
    }

    /**
     * Inserts a new album into the database
     * The album_id will be auto-generated by the database
     *
     * @param album the Album object to insert (albumId will be ignored)
     * @return the Album object with the generated albumId populated
     * @throws Exception if a database error occurs
     */
    @Override
    public Album insertAlbum(Album album) throws Exception {
        logger.info("insertAlbum() called for album: {}", album.getAlbumTitle());

        try (Connection connection = Connector.getConnection();
             PreparedStatement ps = connection.prepareStatement(INSERT_ALBUM, Statement.RETURN_GENERATED_KEYS)) {

            // Set parameters (albumId is auto-generated, so we skip it)
            ps.setInt(1, album.getArtistId());
            ps.setString(2, album.getAlbumTitle());
            ps.setInt(3, album.getReleaseYear());
            ps.setString(4, album.getAlbumArtUrl());
            ps.setInt(5, album.getTotalTracks());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                // Retrieve the auto-generated ID
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1);
                        album.setAlbumId(generatedId);
                        logger.info("Album inserted successfully with ID: {}", generatedId);
                        return album;
                    }
                }
            }

            logger.error("Failed to insert album, no rows affected");
            throw new Exception("insertAlbum() failed: No rows affected");

        } catch (SQLException e) {
            logger.error("Failed to insert album: {}", album.getAlbumTitle(), e);
            throw new Exception("insertAlbum() failed: " + e.getMessage(), e);
        }
    }

    /**
     * Updates an existing album in the database
     * All fields are updated based on the albumId
     *
     * @param album the Album object with updated data
     * @return true if update was successful, false otherwise
     * @throws Exception if a database error occurs
     */
    @Override
    public boolean updateAlbum(Album album) throws Exception {
        logger.info("updateAlbum() called for album ID: {}", album.getAlbumId());

        try (Connection connection = Connector.getConnection();
             PreparedStatement ps = connection.prepareStatement(UPDATE_ALBUM)) {

            // Set parameters
            ps.setInt(1, album.getArtistId());
            ps.setString(2, album.getAlbumTitle());
            ps.setInt(3, album.getReleaseYear());
            ps.setString(4, album.getAlbumArtUrl());
            ps.setInt(5, album.getTotalTracks());
            ps.setInt(6, album.getAlbumId());  // WHERE clause

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                logger.info("Album updated successfully: {}", album.getAlbumTitle());
                return true;
            } else {
                logger.warn("No album found with ID: {} to update", album.getAlbumId());
                return false;
            }

        } catch (SQLException e) {
            logger.error("Failed to update album ID: {}", album.getAlbumId(), e);
            throw new Exception("updateAlbum() failed: " + e.getMessage(), e);
        }
    }

    /**
     * Deletes an album from the database
     * Note: This will CASCADE DELETE all songs on this album
     *
     * @param albumId the ID of the album to delete
     * @return true if deletion was successful, false otherwise
     * @throws Exception if a database error occurs
     */
    @Override
    public boolean deleteAlbum(int albumId) throws Exception {
        logger.info("deleteAlbum() called for album ID: {}", albumId);

        try (Connection connection = Connector.getConnection();
             PreparedStatement ps = connection.prepareStatement(DELETE_ALBUM)) {

            ps.setInt(1, albumId);

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                logger.info("Album deleted successfully with ID: {}", albumId);
                return true;
            } else {
                logger.warn("No album found with ID: {} to delete", albumId);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Failed to delete album with ID: {}", albumId, e);
            throw new Exception("deleteAlbum() failed: " + e.getMessage(), e);
        }
    }

    /**
     * Helper method to map a ResultSet row to an Album object
     * This method extracts data from the current row of the ResultSet
     * and creates an Album object with that data
     *
     * @param rs the ResultSet positioned at the current row
     * @return Album object created from the ResultSet data
     * @throws SQLException if a database access error occurs
     */
    private Album mapResultSetToAlbum(ResultSet rs) throws SQLException {
        return new Album(
                rs.getInt("album_id"),
                rs.getInt("artist_id"),
                rs.getString("album_title"),
                rs.getInt("release_year"),
                rs.getString("album_art_url"),
                rs.getInt("total_tracks")
        );
    }



}
