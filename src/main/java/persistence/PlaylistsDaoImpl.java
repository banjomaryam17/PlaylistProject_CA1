package persistence;

import entities.Playlists;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of PlaylistsDao interface using JDBC.
 * Handles all database operations for Playlists entity.
 * Uses Connector for database connection management.
 *
 */
@Slf4j
public class PlaylistsDaoImpl implements PlaylistsDao {
    private Connector connector;

    public PlaylistsDaoImpl(Connector connector) {
        this.connector = connector;
    }

    public void closeConnection() {
        connector.freeConnection();
    }

    @Override
    public Playlists create(Playlists playlist) throws SQLException {
        Connection conn = connector.getConnection();
        if (conn == null) {
            throw new SQLException("create(): Could not establish connection to database.");
        }

        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO playlists (userID, playlistName, description, isPublic) VALUES (?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, playlist.getUserID());
            ps.setString(2, playlist.getPlaylistName());
            ps.setString(3, playlist.getDescription());
            ps.setBoolean(4, playlist.isPublic());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected == 0) {
                throw new SQLException("Creating playlist failed, no rows affected.");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    playlist.setPlaylistID(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating playlist failed, no ID obtained.");
                }
            } catch (SQLException e) {
                log.error("create(): An issue occurred when getting generated keys. Exception: ", e.getMessage());
                throw e;
            }

        } catch (SQLException e) {
            log.error("create() - The SQL query could not be prepared. Exception: ", e.getMessage());
            throw e;
        }

        return playlist;
    }

    @Override
    public Playlists findById(int playlistID) throws SQLException {
        Connection conn = connector.getConnection();
        if (conn == null) {
            throw new SQLException("findById(): Could not establish connection to database.");
        }

        Playlists playlist = null;
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM playlists WHERE playlistID = ?")) {
            ps.setInt(1, playlistID);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    playlist = mapPlaylistRow(rs);
                }
            } catch (SQLException e) {
                log.error("findById(): An issue occurred when running the query or processing the resultset. Exception: ", e.getMessage());
                throw e;
            }
        } catch (SQLException e) {
            log.error("findById() - The SQL query could not be prepared. Exception: ", e.getMessage());
            throw e;
        }

        return playlist;
    }

    @Override
    public List<Playlists> findAll() throws SQLException {
        Connection conn = connector.getConnection();
        if (conn == null) {
            throw new SQLException("findAll(): Could not establish connection to database.");
        }

        ArrayList<Playlists> playlists = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM playlists ORDER BY playlistName")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Playlists playlist = mapPlaylistRow(rs);
                    playlists.add(playlist);
                }
            } catch (SQLException e) {
                log.error("findAll(): An issue occurred when running the query or processing the resultset. Exception: ", e.getMessage());
                throw e;
            }
        } catch (SQLException e) {
            log.error("findAll() - The SQL query could not be prepared. Exception: ", e.getMessage());
            throw e;
        }
        return playlists;
    }

    @Override
    public List<Playlists> findByUserID(int userID) throws SQLException {
        Connection conn = connector.getConnection();
        if (conn == null) {
            throw new SQLException("findByUserID(): Could not establish connection to database.");
        }

        ArrayList<Playlists> playlists = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM playlists WHERE userID = ? ORDER BY playlistName")) {
            ps.setInt(1, userID);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Playlists playlist = mapPlaylistRow(rs);
                    playlists.add(playlist);
                }
            } catch (SQLException e) {
                log.error("findByUserID(): An issue occurred when running the query or processing the resultset. Exception: ", e.getMessage());
                throw e;
            }
        } catch (SQLException e) {
            log.error("findByUserID() - The SQL query could not be prepared. Exception: ", e.getMessage());
            throw e;
        }
        return playlists;
    }

    @Override
    public List<Playlists> getVisiblePlaylists(int userID) throws SQLException {
        Connection conn = connector.getConnection();
        if (conn == null) {
            throw new SQLException("getVisiblePlaylists(): Could not establish connection to database.");
        }

        ArrayList<Playlists> playlists = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM playlists WHERE userID = ? OR isPublic = 1 ORDER BY playlistName")) {
            ps.setInt(1, userID);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Playlists playlist = mapPlaylistRow(rs);
                    playlists.add(playlist);
                }
            } catch (SQLException e) {
                log.error("getVisiblePlaylists(): An issue occurred when running the query or processing the resultset. Exception: ", e.getMessage());
                throw e;
            }
        } catch (SQLException e) {
            log.error("getVisiblePlaylists() - The SQL query could not be prepared. Exception: ", e.getMessage());
            throw e;
        }
        return playlists;
    }

    @Override
    public boolean update(Playlists playlist) throws SQLException {
        Connection conn = connector.getConnection();
        if (conn == null) {
            throw new SQLException("update(): Could not establish connection to database.");
        }

        int rowsAffected = 0;
        try (PreparedStatement ps = conn.prepareStatement(
                "UPDATE playlists SET playlistName = ?, description = ?, isPublic = ? WHERE playlistID = ?")) {

            ps.setString(1, playlist.getPlaylistName());
            ps.setString(2, playlist.getDescription());
            ps.setBoolean(3, playlist.isPublic());
            ps.setInt(4, playlist.getPlaylistID());

            rowsAffected = ps.executeUpdate();

        } catch (SQLException e) {
            log.error("update() - The SQL query could not be prepared. Exception: ", e.getMessage());
            throw e;
        }

        return rowsAffected > 0;
    }

    @Override
    public boolean delete(int playlistID) throws SQLException {
        Connection conn = connector.getConnection();
        if (conn == null) {
            throw new SQLException("delete(): Could not establish connection to database.");
        }

        int deletedRows = 0;
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM playlists WHERE playlistID = ?")) {
            ps.setInt(1, playlistID);
            deletedRows = ps.executeUpdate();

        } catch (SQLException e) {
            log.error("delete() - The SQL query could not be prepared. Exception:", e.getMessage());
            throw e;
        }

        return deletedRows > 0;
    }

    @Override
    public boolean addSong(int playlistID, int songID) throws SQLException {
        Connection conn = connector.getConnection();
        if (conn == null) {
            throw new SQLException("addSong(): Could not establish connection to database.");
        }

        int rowsAffected = 0;
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO playlist_songs (playlistID, songID) VALUES (?, ?)")) {

            ps.setInt(1, playlistID);
            ps.setInt(2, songID);

            rowsAffected = ps.executeUpdate();

        } catch (SQLException e) {
            log.error("addSong() - The SQL query could not be prepared. Exception: ", e.getMessage());
            throw e;
        }

        return rowsAffected > 0;
    }

    @Override
    public boolean removeSong(int playlistID, int songID) throws SQLException {
        Connection conn = connector.getConnection();
        if (conn == null) {
            throw new SQLException("removeSong(): Could not establish connection to database.");
        }

        int deletedRows = 0;
        try (PreparedStatement ps = conn.prepareStatement(
                "DELETE FROM playlist_songs WHERE playlistID = ? AND songID = ?")) {

            ps.setInt(1, playlistID);
            ps.setInt(2, songID);
            deletedRows = ps.executeUpdate();

        } catch (SQLException e) {
            log.error("removeSong() - The SQL query could not be prepared. Exception:", e.getMessage());
            throw e;
        }

        return deletedRows > 0;
    }

    /**
     * Maps a ResultSet row to a Playlists object.
     *
     * @param rs the ResultSet positioned at a playlist row
     * @return Playlists object populated with data from ResultSet
     * @throws SQLException if error accessing ResultSet data
     */
    private static Playlists mapPlaylistRow(ResultSet rs) throws SQLException {
        return Playlists.builder()
                .playlistID(rs.getInt("playlistID"))
                .userID(rs.getInt("userID"))
                .playlistName(rs.getString("playlistName"))
                .description(rs.getString("description"))
                .isPublic(rs.getBoolean("isPublic"))
                .createdAt(rs.getTimestamp("createdAt"))
                .build();
    }
}