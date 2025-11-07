package persistence;

import entities.Playlists;
import java.sql.SQLException;
import java.util.List;

/**
 * Data Access Object interface for Playlists entity.
 *
 */
public interface PlaylistsDao {
    public Playlists create(Playlists playlist) throws SQLException;
    public Playlists findById(int playlistID) throws SQLException;
    public List<Playlists> findAll() throws SQLException;
    public List<Playlists> findByUsername(String username) throws SQLException;
    public List<Playlists> getVisiblePlaylists(String username) throws SQLException;
    public boolean update(Playlists playlist) throws SQLException;
    public boolean delete(int playlistID) throws SQLException;
    public boolean addSong(int playlistID, int songID) throws SQLException;
    public boolean removeSong(int playlistID, int songID) throws SQLException;
}