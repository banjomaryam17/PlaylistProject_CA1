package services;

import entities.Song;
import persistence.SongDao;
import persistence.SongDaoImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Service layer for Song-related business logic.
 * This class acts as an intermediary between the presentation layer (UI/Controllers)
 * and the persistence layer (DAOs).
 *
 * Responsibilities:
 * - Business logic validation
 * - Coordinating multiple DAO operations if needed
 * - Transaction management
 * - Exception handling and logging
 *
 * @author Jeremiah
 * @version 1.0
 */
public class SongServices {

    private static final Logger logger = LoggerFactory.getLogger(SongServices.class);
    private final SongDao songDao;

    /**
     * Default constructor - initializes with SongDaoImpl
     */
    public SongServices() {
        this.songDao = new SongDaoImpl();
        logger.info("SongServices initialized with SongDaoImpl");
    }

    /**
     * Constructor with dependency injection for testing
     * Allows injecting a mock DAO for unit testing
     *
     * @param songDao the DAO implementation to use
     */
    public SongServices(SongDao songDao) {
        this.songDao = songDao;
        logger.info("SongServices initialized with custom SongDao");
    }

    // ==================== CREATE ====================

    /**
     * Creates a new song with business logic validation
     *
     * @param song the Song object to create
     * @return the created Song with generated ID
     * @throws IllegalArgumentException if song data is invalid
     * @throws Exception if database operation fails
     */
    public Song createSong(Song song) throws Exception {
        logger.info("Attempting to create song: {}", song.getSongTitle());

        // Business logic validation
        validateSong(song);

        try {
            Song createdSong = songDao.insertSong(song);
            logger.info("Successfully created song with ID: {}", createdSong.getSongId());
            return createdSong;
        } catch (Exception e) {
            logger.error("Failed to create song: {}", song.getSongTitle(), e);
            throw new Exception("Unable to create song: " + e.getMessage(), e);
        }
    }

    // ==================== READ ====================

    /**
     * Retrieves a song by its ID
     *
     * @param songId the ID of the song to retrieve
     * @return the Song object if found, null otherwise
     * @throws IllegalArgumentException if songId is invalid
     * @throws Exception if database operation fails
     */
    public Song getSongById(int songId) throws Exception {
        logger.info("Retrieving song with ID: {}", songId);

        if (songId <= 0) {
            throw new IllegalArgumentException("Song ID must be positive");
        }

        try {
            Song song = songDao.getSongById(songId);
            if (song != null) {
                logger.info("Found song: {}", song.getSongTitle());
            } else {
                logger.warn("No song found with ID: {}", songId);
            }
            return song;
        } catch (Exception e) {
            logger.error("Failed to retrieve song with ID: {}", songId, e);
            throw new Exception("Unable to retrieve song: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves all songs from the database
     *
     * @return List of all Song objects (empty list if none found)
     * @throws Exception if database operation fails
     */
    public List<Song> getAllSongs() throws Exception {
        logger.info("Retrieving all songs");

        try {
            List<Song> songs = songDao.getAllSongs();
            logger.info("Retrieved {} songs", songs.size());
            return songs;
        } catch (Exception e) {
            logger.error("Failed to retrieve all songs", e);
            throw new Exception("Unable to retrieve songs: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves all songs from a specific album
     *
     * @param albumId the ID of the album
     * @return List of Song objects for that album (empty list if none found)
     * @throws IllegalArgumentException if albumId is invalid
     * @throws Exception if database operation fails
     */
    public List<Song> getSongsByAlbum(int albumId) throws Exception {
        logger.info("Retrieving songs for album ID: {}", albumId);

        if (albumId <= 0) {
            throw new IllegalArgumentException("Album ID must be positive");
        }

        try {
            List<Song> songs = songDao.getSongsByAlbumId(albumId);
            logger.info("Found {} songs for album ID: {}", songs.size(), albumId);
            return songs;
        } catch (Exception e) {
            logger.error("Failed to retrieve songs for album ID: {}", albumId, e);
            throw new Exception("Unable to retrieve songs for album: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves all songs by a specific artist
     *
     * @param artistId the ID of the artist
     * @return List of Song objects for that artist (empty list if none found)
     * @throws IllegalArgumentException if artistId is invalid
     * @throws Exception if database operation fails
     */
    public List<Song> getSongsByArtist(int artistId) throws Exception {
        logger.info("Retrieving songs for artist ID: {}", artistId);

        if (artistId <= 0) {
            throw new IllegalArgumentException("Artist ID must be positive");
        }

        try {
            List<Song> songs = songDao.getSongsByArtistId(artistId);
            logger.info("Found {} songs for artist ID: {}", songs.size(), artistId);
            return songs;
        } catch (Exception e) {
            logger.error("Failed to retrieve songs for artist ID: {}", artistId, e);
            throw new Exception("Unable to retrieve songs for artist: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves all songs for a specific genre
     * CA Requirement: "Searching for songs by genre"
     *
     * @param genreId the ID of the genre
     * @return List of Song objects for that genre (empty list if none found)
     * @throws IllegalArgumentException if genreId is invalid
     * @throws Exception if database operation fails
     */
    public List<Song> getSongsByGenre(int genreId) throws Exception {
        logger.info("Retrieving songs for genre ID: {}", genreId);

        if (genreId <= 0) {
            throw new IllegalArgumentException("Genre ID must be positive");
        }

        try {
            List<Song> songs = songDao.getSongsByGenreId(genreId);
            logger.info("Found {} songs for genre ID: {}", songs.size(), genreId);
            return songs;
        } catch (Exception e) {
            logger.error("Failed to retrieve songs for genre ID: {}", genreId, e);
            throw new Exception("Unable to retrieve songs for genre: " + e.getMessage(), e);
        }
    }

    /**
     * Searches for songs by title (partial match)
     *
     * @param title the title or partial title to search for
     * @return List of matching Song objects (empty list if none found)
     * @throws IllegalArgumentException if title is null or empty
     * @throws Exception if database operation fails
     */
    public List<Song> searchSongsByTitle(String title) throws Exception {
        logger.info("Searching songs with title: {}", title);

        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Search title cannot be null or empty");
        }

        try {
            List<Song> songs = songDao.searchSongsByTitle(title);
            logger.info("Found {} songs matching title: {}", songs.size(), title);
            return songs;
        } catch (Exception e) {
            logger.error("Failed to search songs by title: {}", title, e);
            throw new Exception("Unable to search songs: " + e.getMessage(), e);
        }
    }

    // ==================== UPDATE ====================

    /**
     * Updates an existing song with business logic validation
     *
     * @param song the Song object with updated data
     * @return true if update was successful, false otherwise
     * @throws IllegalArgumentException if song data is invalid
     * @throws Exception if database operation fails
     */
    public boolean updateSong(Song song) throws Exception {
        logger.info("Attempting to update song ID: {}", song.getSongId());

        // Business logic validation
        validateSong(song);

        if (song.getSongId() <= 0) {
            throw new IllegalArgumentException("Song ID must be positive for updates");
        }

        try {
            boolean success = songDao.updateSong(song);
            if (success) {
                logger.info("Successfully updated song ID: {}", song.getSongId());
            } else {
                logger.warn("Song ID: {} not found for update", song.getSongId());
            }
            return success;
        } catch (Exception e) {
            logger.error("Failed to update song ID: {}", song.getSongId(), e);
            throw new Exception("Unable to update song: " + e.getMessage(), e);
        }
    }

    // ==================== DELETE ====================

    /**
     * Deletes a song by its ID
     * Note: This will CASCADE DELETE all ratings for this song
     *
     * @param songId the ID of the song to delete
     * @return true if deletion was successful, false if song not found
     * @throws IllegalArgumentException if songId is invalid
     * @throws Exception if database operation fails
     */
    public boolean deleteSong(int songId) throws Exception {
        logger.info("Attempting to delete song ID: {}", songId);

        if (songId <= 0) {
            throw new IllegalArgumentException("Song ID must be positive");
        }

        try {
            boolean success = songDao.deleteSong(songId);
            if (success) {
                logger.info("Successfully deleted song ID: {}", songId);
            } else {
                logger.warn("Song ID: {} not found for deletion", songId);
            }
            return success;
        } catch (Exception e) {
            logger.error("Failed to delete song ID: {}", songId, e);
            throw new Exception("Unable to delete song: " + e.getMessage(), e);
        }
    }

    // ==================== VALIDATION ====================

    /**
     * Validates song data according to business rules
     *
     * @param song the Song object to validate
     * @throws IllegalArgumentException if validation fails
     */
    private void validateSong(Song song) {
        if (song == null) {
            throw new IllegalArgumentException("Song cannot be null");
        }

        // Validate song title
        if (song.getSongTitle() == null || song.getSongTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Song title cannot be null or empty");
        }

        if (song.getSongTitle().length() > 200) {
            throw new IllegalArgumentException("Song title cannot exceed 200 characters");
        }

        // Validate album ID
        if (song.getAlbumId() <= 0) {
            throw new IllegalArgumentException("Album ID must be positive");
        }

        // Validate artist ID
        if (song.getArtistId() <= 0) {
            throw new IllegalArgumentException("Artist ID must be positive");
        }

        // Validate duration
        if (song.getDurationSeconds() != 0 && song.getDurationSeconds() <= 0) {
            throw new IllegalArgumentException("Duration must be positive");
        }

        // Validate maximum duration (e.g., 60 minutes = 3600 seconds)
        if (song.getDurationSeconds() != 0 && song.getDurationSeconds() > 3600) {
            throw new IllegalArgumentException("Song duration cannot exceed 60 minutes");
        }

        // Validate track number
        if (song.getTrackNumber() != 0 && song.getTrackNumber() <= 0) {
            throw new IllegalArgumentException("Track number must be positive");
        }

        // Validate track number maximum (reasonable limit)
        if (song.getTrackNumber() != 0 && song.getTrackNumber() > 999) {
            throw new IllegalArgumentException("Track number cannot exceed 999");
        }

        // Validate release year (optional field, but if present should be reasonable)
        if (song.getReleaseYear() != 0) {
            int currentYear = java.time.Year.now().getValue();
            if (song.getReleaseYear() < 1900 || song.getReleaseYear() > currentYear + 1) {
                throw new IllegalArgumentException("Release year must be between 1900 and " + (currentYear + 1));
            }
        }

        logger.debug("Song validation passed for: {}", song.getSongTitle());
    }

    // ==================== UTILITY METHODS ====================

    /**
     * Checks if a song exists in the database
     *
     * @param songId the ID of the song to check
     * @return true if song exists, false otherwise
     * @throws Exception if database operation fails
     */
    public boolean songExists(int songId) throws Exception {
        Song song = getSongById(songId);
        return song != null;
    }

    /**
     * Gets the total count of songs in the database
     *
     * @return the total number of songs
     * @throws Exception if database operation fails
     */
    public int getSongCount() throws Exception {
        return getAllSongs().size();
    }

    /**
     * Gets the total count of songs for a specific album
     *
     * @param albumId the ID of the album
     * @return the number of songs on that album
     * @throws Exception if database operation fails
     */
    public int getSongCountByAlbum(int albumId) throws Exception {
        return getSongsByAlbum(albumId).size();
    }

    /**
     * Gets the total count of songs for a specific artist
     *
     * @param artistId the ID of the artist
     * @return the number of songs by that artist
     * @throws Exception if database operation fails
     */
    public int getSongCountByArtist(int artistId) throws Exception {
        return getSongsByArtist(artistId).size();
    }

    /**
     * Gets the total count of songs for a specific genre
     *
     * @param genreId the ID of the genre
     * @return the number of songs in that genre
     * @throws Exception if database operation fails
     */
    public int getSongCountByGenre(int genreId) throws Exception {
        return getSongsByGenre(genreId).size();
    }

    /**
     * Formats a song duration in seconds to MM:SS format
     *
     * @param durationSeconds the duration in seconds
     * @return formatted duration string (e.g., "3:45")
     */
    public String formatDuration(int durationSeconds) {
        int minutes = durationSeconds / 60;
        int seconds = durationSeconds % 60;
        return String.format("%d:%02d", minutes, seconds);
    }

    /**
     * Gets the total duration of all songs in an album
     *
     * @param albumId the ID of the album
     * @return total duration in seconds
     * @throws Exception if database operation fails
     */
    public int getTotalAlbumDuration(int albumId) throws Exception {
        List<Song> songs = getSongsByAlbum(albumId);
        int totalDuration = 0;
        for (Song song : songs) {
            if (song.getDurationSeconds() != 0) {
                totalDuration += song.getDurationSeconds();
            }
        }
        return totalDuration;
    }
}