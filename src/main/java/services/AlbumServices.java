package services;

import entities.Album;
import persistence.AlbumDao;
import persistence.AlbumDaoImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Service layer for Album-related business logic.
 * and the persistence layer (DAOs).
 *
 * @author Jeremiah
 *
 */
public class AlbumServices {

    private static final Logger logger = LoggerFactory.getLogger(AlbumServices.class);
    private final AlbumDao albumDao;

    /**
     * Default constructor - initializes with AlbumDaoImpl
     */
    public AlbumServices() {
        this.albumDao = new AlbumDaoImpl();
        logger.info("AlbumServices initialized with AlbumDaoImpl");
    }

    /**
     * Constructor with dependency injection for testing
     * Allows injecting a mock DAO for unit testing
     *
     * @param albumDao the DAO implementation to use
     */
    public AlbumServices(AlbumDao albumDao) {
        this.albumDao = albumDao;
        logger.info("AlbumServices initialized with custom AlbumDao");
    }



    /**
     * Creates a new album with business logic validation
     *
     * @param album the Album object to create
     * @return the created Album with generated ID
     * @throws IllegalArgumentException if album data is invalid
     * @throws Exception if database operation fails
     */
    public Album createAlbum(Album album) throws Exception {
        logger.info("Attempting to create album: {}", album.getAlbumTitle());

        // Business logic validation
        validateAlbum(album);

        try {
            Album createdAlbum = albumDao.insertAlbum(album);
            logger.info("Successfully created album with ID: {}", createdAlbum.getAlbumId());
            return createdAlbum;
        } catch (Exception e) {
            logger.error("Failed to create album: {}", album.getAlbumTitle(), e);
            throw new Exception("Unable to create album: " + e.getMessage(), e);
        }
    }



    /**
     * Retrieves an album by its ID
     *
     * @param albumId the ID of the album to retrieve
     * @return the Album object if found, null otherwise
     * @throws IllegalArgumentException if albumId is invalid
     * @throws Exception if database operation fails
     */
    public Album getAlbumById(int albumId) throws Exception {
        logger.info("Retrieving album with ID: {}", albumId);

        if (albumId <= 0) {
            throw new IllegalArgumentException("Album ID must be positive");
        }

        try {
            Album album = albumDao.getAlbumById(albumId);
            if (album != null) {
                logger.info("Found album: {}", album.getAlbumTitle());
            } else {
                logger.warn("No album found with ID: {}", albumId);
            }
            return album;
        } catch (Exception e) {
            logger.error("Failed to retrieve album with ID: {}", albumId, e);
            throw new Exception("Unable to retrieve album: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves all albums from the database
     *
     * @return List of all Album objects (empty list if none found)
     * @throws Exception if database operation fails
     */
    public List<Album> getAllAlbums() throws Exception {
        logger.info("Retrieving all albums");

        try {
            List<Album> albums = albumDao.getAllAlbums();
            logger.info("Retrieved {} albums", albums.size());
            return albums;
        } catch (Exception e) {
            logger.error("Failed to retrieve all albums", e);
            throw new Exception("Unable to retrieve albums: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves all albums by a specific artist
     *
     * @param artistId the ID of the artist
     * @return List of Album objects for that artist (empty list if none found)
     * @throws IllegalArgumentException if artistId is invalid
     * @throws Exception if database operation fails
     */
    public List<Album> getAlbumsByArtist(int artistId) throws Exception {
        logger.info("Retrieving albums for artist ID: {}", artistId);

        if (artistId <= 0) {
            throw new IllegalArgumentException("Artist ID must be positive");
        }

        try {
            List<Album> albums = albumDao.getAlbumsByArtistId(artistId);
            logger.info("Found {} albums for artist ID: {}", albums.size(), artistId);
            return albums;
        } catch (Exception e) {
            logger.error("Failed to retrieve albums for artist ID: {}", artistId, e);
            throw new Exception("Unable to retrieve albums for artist: " + e.getMessage(), e);
        }
    }

    /**
     * Searches for albums by title (partial match)
     *
     * @param title the title or partial title to search for
     * @return List of matching Album objects (empty list if none found)
     * @throws IllegalArgumentException if title is null or empty
     * @throws Exception if database operation fails
     */
    public List<Album> searchAlbumsByTitle(String title) throws Exception {
        logger.info("Searching albums with title: {}", title);

        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Search title cannot be null or empty");
        }

        try {
            List<Album> albums = albumDao.searchAlbumsByTitle(title);
            logger.info("Found {} albums matching title: {}", albums.size(), title);
            return albums;
        } catch (Exception e) {
            logger.error("Failed to search albums by title: {}", title, e);
            throw new Exception("Unable to search albums: " + e.getMessage(), e);
        }
    }



    /**
     * Updates an existing album with business logic validation
     *
     * @param album the Album object with updated data
     * @return true if update was successful, false otherwise
     * @throws IllegalArgumentException if album data is invalid
     * @throws Exception if database operation fails
     */
    public boolean updateAlbum(Album album) throws Exception {
        logger.info("Attempting to update album ID: {}", album.getAlbumId());

        // Business logic validation
        validateAlbum(album);

        if (album.getAlbumId() <= 0) {
            throw new IllegalArgumentException("Album ID must be positive for updates");
        }

        try {
            boolean success = albumDao.updateAlbum(album);
            if (success) {
                logger.info("Successfully updated album ID: {}", album.getAlbumId());
            } else {
                logger.warn("Album ID: {} not found for update", album.getAlbumId());
            }
            return success;
        } catch (Exception e) {
            logger.error("Failed to update album ID: {}", album.getAlbumId(), e);
            throw new Exception("Unable to update album: " + e.getMessage(), e);
        }
    }



    /**
     * Deletes an album by its ID
     * Note: This will CASCADE DELETE all songs on this album
     *
     * @param albumId the ID of the album to delete
     * @return true if deletion was successful, false if album not found
     * @throws IllegalArgumentException if albumId is invalid
     * @throws Exception if database operation fails
     */
    public boolean deleteAlbum(int albumId) throws Exception {
        logger.info("Attempting to delete album ID: {}", albumId);

        if (albumId <= 0) {
            throw new IllegalArgumentException("Album ID must be positive");
        }

        try {
            boolean success = albumDao.deleteAlbum(albumId);
            if (success) {
                logger.info("Successfully deleted album ID: {}", albumId);
            } else {
                logger.warn("Album ID: {} not found for deletion", albumId);
            }
            return success;
        } catch (Exception e) {
            logger.error("Failed to delete album ID: {}", albumId, e);
            throw new Exception("Unable to delete album: " + e.getMessage(), e);
        }
    }


    /**
     * Validates album data according to business rules
     *
     * @param album the Album object to validate
     * @throws IllegalArgumentException if validation fails
     */
    private void validateAlbum(Album album) {
        if (album == null) {
            throw new IllegalArgumentException("Album cannot be null");
        }

        // Validate album title
        if (album.getAlbumTitle() == null || album.getAlbumTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Album title cannot be null or empty");
        }

        if (album.getAlbumTitle().length() > 200) {
            throw new IllegalArgumentException("Album title cannot exceed 200 characters");
        }

        // Validate artist ID
        if (album.getArtistId() <= 0) {
            throw new IllegalArgumentException("Artist ID must be positive");
        }

        // Validate release year (optional field, but if present should be reasonable)
        if (album.getReleaseYear() != 0) {
            int currentYear = java.time.Year.now().getValue();
            if (album.getReleaseYear() < 1900 || album.getReleaseYear() > currentYear + 1) {
                throw new IllegalArgumentException("Release year must be between 1900 and " + (currentYear + 1));
            }
        }

        logger.debug("Album validation passed for: {}", album.getAlbumTitle());
    }



    /**
     * Checks if an album exists in the database
     *
     * @param albumId the ID of the album to check
     * @return true if album exists, false otherwise
     * @throws Exception if database operation fails
     */
    public boolean albumExists(int albumId) throws Exception {
        Album album = getAlbumById(albumId);
        return album != null;
    }

    /**
     * Gets the total count of albums in the database
     *
     * @return the total number of albums
     * @throws Exception if database operation fails
     */
    public int getAlbumCount() throws Exception {
        return getAllAlbums().size();
    }

    /**
     * Gets the total count of albums for a specific artist
     *
     * @param artistId the ID of the artist
     * @return the number of albums for that artist
     * @throws Exception if database operation fails
     */
    public int getAlbumCountByArtist(int artistId) throws Exception {
        return getAlbumsByArtist(artistId).size();
    }
}