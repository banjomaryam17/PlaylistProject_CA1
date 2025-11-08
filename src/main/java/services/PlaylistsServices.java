package services;

import persistence.PlaylistsDao;
import entities.Playlists;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.List;

/**
 * Service layer for Playlists operations.
 * Provides business logic and validation for Playlist-related actions.
 * Enforces ownership rules - users can only edit their own playlists.
 *
 * @author [Your Name]
 */
@Slf4j
public class PlaylistsService {
    private PlaylistsDao playlistsDao;

    public PlaylistsService(PlaylistsDao dao) {
        this.playlistsDao = dao;
    }

    public void shutdownService() {
        playlistsDao.closeConnection();
    }

    public List<Playlists> getAllPlaylists() throws SQLException {
        log.info("Retrieving all playlists");
        return playlistsDao.findAll();
    }

    public Playlists getPlaylistById(int playlistID) throws SQLException {
        log.info("Playlist retrieval: {}", playlistID);
        return playlistsDao.findById(playlistID);
    }

    public List<Playlists> getPlaylistsByUser(int userID) throws SQLException {
        log.info("Retrieving playlists for user: {}", userID);
        return playlistsDao.findByUserID(userID);
    }

    public List<Playlists> getVisiblePlaylists(int userID) throws SQLException {
        log.info("Retrieving visible playlists for user: {}", userID);
        return playlistsDao.getVisiblePlaylists(userID);
    }

    public Playlists createPlaylist(int userID, String playlistName, String description, boolean isPublic) throws SQLException {
        if (playlistName == null || playlistName.isBlank()) {
            throw new IllegalArgumentException("Playlist name must be provided");
        }

        log.info("Creating playlist: {} for user: {}", playlistName, userID);
        Playlists playlist = Playlists.builder()
                .userID(userID)
                .playlistName(playlistName)
                .description(description)
                .isPublic(isPublic)
                .build();

        return playlistsDao.create(playlist);
    }

    public boolean updatePlaylist(Playlists playlist, int requestingUserID) throws SQLException {
        if (playlist == null) {
            throw new IllegalArgumentException("Playlist cannot be null");
        }
        if (playlist.getPlaylistName() == null || playlist.getPlaylistName().isBlank()) {
            throw new IllegalArgumentException("Playlist name must be provided");
        }

        // Ownership check - users can only update their own playlists
        Playlists existing = playlistsDao.findById(playlist.getPlaylistID());
        if (existing == null) {
            throw new IllegalArgumentException("Playlist not found");
        }
        if (existing.getUserID() != requestingUserID) {
            throw new IllegalArgumentException("You can only edit your own playlists");
        }

        log.info("Updating playlist: {}", playlist.getPlaylistID());
        return playlistsDao.update(playlist);
    }

    public boolean deletePlaylist(int playlistID, int requestingUserID) throws SQLException {

        Playlists existing = playlistsDao.findById(playlistID);
        if (existing == null) {
            throw new IllegalArgumentException("Playlist not found");
        }
        if (existing.getUserID() != requestingUserID) {
            throw new IllegalArgumentException("You can only delete your own playlists");
        }

        log.info("Deleting playlist: {}", playlistID);
        return playlistsDao.delete(playlistID);
    }

    public boolean addSongToPlaylist(int playlistID, int songID, int requestingUserID) throws SQLException {
        // Ownership check - users can only add songs to their own playlists
        Playlists existing = playlistsDao.findById(playlistID);
        if (existing == null) {
            throw new IllegalArgumentException("Playlist not found");
        }
        if (existing.getUserID() != requestingUserID) {
            throw new IllegalArgumentException("You can only add songs to your own playlists");
        }

        log.info("Adding song {} to playlist {}", songID, playlistID);
        return playlistsDao.addSong(playlistID, songID);
    }

    public boolean removeSongFromPlaylist(int playlistID, int songID, int requestingUserID) throws SQLException {
        // Ownership check - users can only remove songs from their own playlists
        Playlists existing = playlistsDao.findById(playlistID);
        if (existing == null) {
            throw new IllegalArgumentException("Playlist not found");
        }
        if (existing.getUserID() != requestingUserID) {
            throw new IllegalArgumentException("You can only remove songs from your own playlists");
        }

        log.info("Removing song {} from playlist {}", songID, playlistID);
        return playlistsDao.removeSong(playlistID, songID);
    }
}