package persistence;

import entities.Playlists;
import java.sql.SQLException;
import java.util.List;


/**
 * Data Access Object interface for Playlists entity.
 * Defines CRUD operations for Playlist management.
 *
 *
 */
public interface PlaylistsDao {
    public Playlists create(Playlists playlist) throws SQLException;
    public Playlists findById(int playlistID) throws SQLException;
    public List<Playlists> findAll() throws SQLException;
    public List<Playlists> findByUserID(int userID) throws SQLException;
    public List<Playlists> getVisiblePlaylists(int userID) throws SQLException;
    public boolean update(Playlists playlist) throws SQLException;
    public boolean delete(int playlistID) throws SQLException;
    public boolean addSong(int playlistID, int songID) throws SQLException;
    public boolean removeSong(int playlistID, int songID) throws SQLException;


}