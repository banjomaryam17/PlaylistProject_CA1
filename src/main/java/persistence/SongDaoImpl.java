package persistence;

import entities. Song;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of SongDao interface
 * Handles all database operations for the songs table
 * Uses the shared Connector class for database connection
 *
 * Database: PLAYLISTMODELS
 * Table: songs
 *
 * @author Jeremiah
 */

public class SongDaoImpl implements SongDao {

    private static final Logger logger = LoggerFactory.getLogger(SongDaoImpl.class);

    private static final String SELECT_ALL = "SELECT * FROM songs ORDER BY song_title";

    private static final String SELECT_BY_ID = "SELECT * FROM songs WHERE SONG_ID = ?";

    private static final String SELECT_BY_ALBUM_ID = "SELECT * FROM songs WHERE album_id = ? ORDER BY track_number";

    private static final String SELECT_BY_ARTIST_ID = "SELECT * FROM songs WHERE artist_id = ? ORDER BY release_year DESC, song_title";

    private static final String SEARCH_BY_TITLE = "SELECT * FROM songs WHERE song_title LIKE ? ORDER BY song_title";

    private static final String SELECT_BY_GENRE_ID =
            "SELECT s.* FROM songs s " + "JOIN songs +genre sg ON s.song_id = sg.song_id " + "WHERE sg.genre_id = ? " + "ORDER BY s.song_title";

    private static final String INSERT_SONG = "INSERT INTO songs (song_title, artist_id, album_id, release_year, track_number, duration) VALUES (?,?,?,?,?,?)";

    private static final String UPDATE_SONG = "UPDATE songs SET song_title =?, artist_id =?, album_id =?, release_year =?, track_number =?, duration =? WHERE song_id =?";

    private static final String DELETE_SONG = "DELETE FROM songs WHERE song_id =?";


    /**
     * Retrieves all songs from the database
     *
     * @return List of all Song objects, ordered alphabetically by title
     * @throws Exception if a database error occurs
     */
    @Override
    public List<Song> getAllSongs() throws Exception {
        logger.info("getAllSongs() called");
        List<Song> songs = new ArrayList<>();

        Connector connector = new Connector();
        try (Connection connection = connector.getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                songs.add(mapResultSetToSong(rs));
            }

            logger.info("Retrieved {} songs", songs.size());
            return songs;

        } catch (SQLException e) {
            logger.error("Failed to retrieve all songs", e);
            throw new Exception("getAllSongs() failed: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves a specific song by its ID
     *
     * @param songId the ID of the song to retrieve
     * @return Song object if found, null otherwise
     * @throws Exception if a database error occurs
     */
    @Override
    public Song getSongById(int songId) throws Exception {
        logger.info("getSongById() called with ID: {}", songId);

        Connector connector = new Connector();
        try (Connection connection = connector.getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_BY_ID)) {

            ps.setInt(1, songId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Song song = mapResultSetToSong(rs);
                    logger.info("Found song: {}", song.getSongTitle());
                    return song;
                } else {
                    logger.warn("No song found with ID: {}", songId);
                    return null;
                }
            }

        } catch (SQLException e) {
            logger.error("Failed to retrieve song with ID: {}", songId, e);
            throw new Exception("getSongById() failed: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves all songs for a specific album
     * Songs are returned in track order for proper playback sequence
     *
     * @param albumId the ID of the album
     * @return List of Song objects for the album, ordered by track number
     * @throws Exception if a database error occurs
     */
    @Override
    public List<Song> getSongsByAlbumId(int albumId) throws Exception {
        logger.info("getSongsByAlbumId() called with album ID: {}", albumId);
        List<Song> songs = new ArrayList<>();

        Connector connector = new Connector();
        try (Connection connection = connector.getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_BY_ALBUM_ID)) {

            ps.setInt(1, albumId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    songs.add(mapResultSetToSong(rs));
                }
            }

            logger.info("Retrieved {} songs for album ID: {}", songs.size(), albumId);
            return songs;

        } catch (SQLException e) {
            logger.error("Failed to retrieve songs for album ID: {}", albumId, e);
            throw new Exception("getSongsByAlbumId() failed: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves all songs by a specific artist
     * CA Requirement: Part of "Searching for songs by artist"
     *
     * @param artistId the ID of the artist
     * @return List of Song objects for the artist
     * @throws Exception if a database error occurs
     */
    @Override
    public List<Song> getSongsByArtistId(int artistId) throws Exception {
        logger.info("getSongsByArtistId() called with artist ID: {}", artistId);
        List<Song> songs = new ArrayList<>();

        Connector connector = new Connector();
        try (Connection connection = connector.getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_BY_ARTIST_ID)) {

            ps.setInt(1, artistId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    songs.add(mapResultSetToSong(rs));
                }
            }

            logger.info("Retrieved {} songs for artist ID: {}", songs.size(), artistId);
            return songs;

        } catch (SQLException e) {
            logger.error("Failed to retrieve songs for artist ID: {}", artistId, e);
            throw new Exception("getSongsByArtistId() failed: " + e.getMessage(), e);
        }
    }

    /**
     * Searches for songs by title (case-insensitive, partial match)
     * CA Requirement: "Searching for songs by title"
     *
     * @param title the song title to search for
     * @return List of Song objects matching the search criteria
     * @throws Exception if a database error occurs
     */
    @Override
    public List<Song> searchSongsByTitle(String title) throws Exception {
        logger.info("searchSongsByTitle() called with title: {}", title);
        List<Song> songs = new ArrayList<>();

        Connector connector = new Connector();
        try (Connection connection = connector.getConnection();
             PreparedStatement ps = connection.prepareStatement(SEARCH_BY_TITLE)) {

            // Use LIKE with wildcards for partial matching
            ps.setString(1, "%" + title + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    songs.add(mapResultSetToSong(rs));
                }
            }

            logger.info("Found {} songs matching title: {}", songs.size(), title);
            return songs;

        } catch (SQLException e) {
            logger.error("Failed to search songs by title: {}", title, e);
            throw new Exception("searchSongsByTitle() failed: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves all songs for a specific genre
     * CA Requirement: "Searching for songs by genre"
     * <p>
     * Note: This requires a song_genre junction table to exist
     * If you don't have this table yet, this method will fail
     *
     * @param genreId the ID of the genre
     * @return List of Song objects for the genre
     * @throws Exception if a database error occurs
     */
    @Override
    public List<Song> getSongsByGenreId(int genreId) throws Exception {
        logger.info("getSongsByGenreId() called with genre ID: {}", genreId);
        List<Song> songs = new ArrayList<>();

        Connector connector = new Connector();
        try (Connection connection = connector.getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_BY_GENRE_ID)) {

            ps.setInt(1, genreId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    songs.add(mapResultSetToSong(rs));
                }
            }

            logger.info("Retrieved {} songs for genre ID: {}", songs.size(), genreId);
            return songs;

        } catch (SQLException e) {
            logger.error("Failed to retrieve songs for genre ID: {}", genreId, e);
            throw new Exception("getSongsByGenreId() failed: " + e.getMessage(), e);
        }
    }

    /**
     * Inserts a new song into the database
     * The song_id will be auto-generated by the database
     *
     * @param song the Song object to insert (songId will be ignored)
     * @return the Song object with the generated songId populated
     * @throws Exception if a database error occurs
     */
    @Override
    public Song insertSong(Song song) throws Exception {
        logger.info("insertSong() called for song: {}", song.getSongTitle());

        Connector connector = new Connector();
        try (Connection connection = connector.getConnection();
             PreparedStatement ps = connection.prepareStatement(INSERT_SONG, Statement.RETURN_GENERATED_KEYS)) {

            // Set parameters (song_id is auto-generated, so we skip it)
            ps.setInt(1, song.getAlbumId());
            ps.setInt(2, song.getArtistId());
            ps.setString(3, song.getSongTitle());
            ps.setInt(4, song.getDurationSeconds());
            ps.setInt(5, song.getTrackNumber());
            ps.setInt(6, song.getReleaseYear());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                // Retrieve the auto-generated ID
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1);
                        song.setSongId(generatedId);
                        logger.info("Song inserted successfully with ID: {}", generatedId);
                        return song;
                    }
                }
            }

            logger.error("Failed to insert song, no rows affected");
            throw new Exception("insertSong() failed: No rows affected");

        } catch (SQLException e) {
            logger.error("Failed to insert song: {}", song.getSongTitle(), e);
            throw new Exception("insertSong() failed: " + e.getMessage(), e);
        }
    }

    /**
     * Updates an existing song in the database
     * All fields are updated based on the songId
     *
     * @param song the Song object with updated data
     * @return true if update was successful, false otherwise
     * @throws Exception if a database error occurs
     */
    @Override
    public boolean updateSong(Song song) throws Exception {
        logger.info("updateSong() called for song ID: {}", song.getSongId());

        Connector connector = new Connector();
        try (Connection connection = connector.getConnection();
             PreparedStatement ps = connection.prepareStatement(UPDATE_SONG)) {

            // Set parameters
            ps.setInt(1, song.getAlbumId());
            ps.setInt(2, song.getArtistId());
            ps.setString(3, song.getSongTitle());
            ps.setInt(4, song.getDurationSeconds());
            ps.setInt(5, song.getTrackNumber());
            ps.setInt(6, song.getReleaseYear());
            ps.setInt(7, song.getSongId());  // WHERE clause

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                logger.info("Song updated successfully: {}", song.getSongTitle());
                return true;
            } else {
                logger.warn("No song found with ID: {} to update", song.getSongId());
                return false;
            }

        } catch (SQLException e) {
            logger.error("Failed to update song ID: {}", song.getSongId(), e);
            throw new Exception("updateSong() failed: " + e.getMessage(), e);
        }
    }

    /**
     * Deletes a song from the database
     * Note: This will CASCADE DELETE all playlist associations for this song
     *
     * @param songId the ID of the song to delete
     * @return true if deletion was successful, false otherwise
     * @throws Exception if a database error occurs
     */
    @Override
    public boolean deleteSong(int songId) throws Exception {
        logger.info("deleteSong() called for song ID: {}", songId);

        Connector connector = new Connector();
        try (Connection connection = connector.getConnection();
             PreparedStatement ps = connection.prepareStatement(DELETE_SONG)) {

            ps.setInt(1, songId);

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                logger.info("Song deleted successfully with ID: {}", songId);
                return true;
            } else {
                logger.warn("No song found with ID: {} to delete", songId);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Failed to delete song with ID: {}", songId, e);
            throw new Exception("deleteSong() failed: " + e.getMessage(), e);
        }
    }

    /**
     * Helper method to map a ResultSet row to a Song object
     * This method extracts data from the current row of the ResultSet
     * and creates a Song object with that data
     * <p>
     * Maps database columns to Java fields:
     * - song_id → songId
     * - album_id → albumId
     * - artist_id → artistId
     * - song_title → songTitle
     * - duration_seconds → durationSeconds
     * - track_number → trackNumber
     * - release_year → releaseYear
     *
     * @param rs the ResultSet positioned at the current row
     * @return Song object created from the ResultSet data
     * @throws SQLException if a database access error occurs
     */
    private Song mapResultSetToSong(ResultSet rs) throws SQLException {
        return new Song(
                rs.getInt("song_id"),
                rs.getInt("album_id"),
                rs.getInt("artist_id"),
                rs.getString("song_title"),
                rs.getInt("duration_seconds"),
                rs.getInt("track_number"),
                rs.getInt("release_year")
        );
    }
}






