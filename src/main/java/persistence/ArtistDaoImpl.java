package persistence;

import entities.Artist;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Implementation of ArtistDAO interface using JDBC.
 * handles read operations for Artist entity
 * uses Connector for database connection management
 */
@Slf4j
public class ArtistDaoImpl implements ArtistDao {

    private final Connector connector;

    public ArtistDaoImpl(Connector connector) {
        this.connector = connector;
    }

    public void closeConnection() {
        connector.freeConnection();
    }

    /** Map one result set row to an Artist */
    private static Artist mapArtistRow(ResultSet rs) throws SQLException {
        Artist a = new Artist();
        a.setArtistId(rs.getInt("artistID"));
        a.setArtistName(rs.getString("artistName"));
        a.setGenre(rs.getString("genre"));

        java.sql.Date sqlDob = rs.getDate("dateOfBirth");
        a.setDateOfBirth(sqlDob != null ? new Date(sqlDob.getTime()) : null);

        return a;
    }

    /** Find one artist by primary key. Returns null if no match. */
    @Override
    public Artist getArtistById(int id) {
        Connection conn = connector.getConnection();
        if (conn == null) {
            log.error("getArtistById(): Could not establish connection to database.");
            return null;
        }

        final String sql = "SELECT * FROM Artists WHERE artistID = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapArtistRow(rs) : null;
            }
        } catch (SQLException e) {
            log.error("getArtistById(): SQL error - {}", e.getMessage());
            return null;
        }
    }

    /** Find one artist by exact name. Returns null if no match. */
    @Override
    public Artist getArtistByName(String name) {
        Connection conn = connector.getConnection();
        if (conn == null) {
            log.error("getArtistByName(): Could not establish connection to database.");
            return null;
        }

        final String sql = "SELECT * FROM Artists WHERE artistName = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapArtistRow(rs) : null;
            }
        } catch (SQLException e) {
            log.error("getArtistByName(): SQL error - {}", e.getMessage());
            return null;
        }
    }

    /** Find artists whose name contains the given text DB collation applies  */
    @Override
    public List<Artist> getAllArtistsWhereNameLike(String namePart) {
        Connection conn = connector.getConnection();
        if (conn == null) {
            log.error("getAllArtistsWhereNameLike(): Could not establish connection to database.");
            return Collections.emptyList();
        }

        final String sql = "SELECT * FROM Artists WHERE artistName LIKE ? ORDER BY artistID";
        List<Artist> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + namePart + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapArtistRow(rs));
            }
        } catch (SQLException e) {
            log.error("getAllArtistsWhereNameLike(): SQL error - {}", e.getMessage());
        }
        return list;
    }

    /** Fetch all artists ordered by artistID. */
    @Override
    public List<Artist> getAllArtists() {
        Connection conn = connector.getConnection();
        if (conn == null) {
            log.error("getAllArtists(): Could not establish connection to database.");
            return Collections.emptyList();
        }

        final String sql = "SELECT * FROM Artists ORDER BY artistID";
        List<Artist> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) list.add(mapArtistRow(rs));
        } catch (SQLException e) {
            log.error("getAllArtists(): SQL error - {}", e.getMessage());
        }
        return list;
    }
}
