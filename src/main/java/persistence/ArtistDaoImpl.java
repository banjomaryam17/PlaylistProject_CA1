package persistence;

import entities.Artist;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ArtistDaoImpl extends MySQLDao implements ArtistDao {

    /**
     * Ges  the connection from the superclass by passing , database name
     * @param dbName  the name of the database
     */
    public ArtistDaoImpl(String dbName) {
        super(dbName);
    }
    /** Get one artist by id, or null if not found */

    @Override
    public Artist getArtistById(int id) {
        Artist artist = null;
        final String query = "SELECT * FROM Artists WHERE artistID = ?";

        try (Connection con = super.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    artist = mapRowBasic(rs);
                }
            }

        } catch (SQLException e) {
            System.out.println("Exception occurred in getArtistById(): " + e.getMessage());
        }
        return artist;
    }
    /** Get one artist by exact name match, or null if not fond */

    @Override
    public Artist getArtistByName(String name) {
        Artist artist = null;
        final String query = "SELECT * FROM Artists WHERE name = ?";

        try (Connection con = super.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, name);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    artist = mapRowBasic(rs);
                }
            }

        } catch (SQLException e) {
            System.out.println("Exception occurred in getArtistByName(): " + e.getMessage());
        }
        return artist;
    }
    /** Find artists where name contains the text */

    @Override
    public List<Artist> getAllArtistsWhereNameLike(String artistName) {
        List<Artist> result = new ArrayList<>();
        final String query = "SELECT * FROM Artists WHERE name LIKE ? ORDER BY artistID";

        try (Connection con = super.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, "%" + artistName + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRowBasic(rs));
                }
            }

        } catch (SQLException e) {
            System.out.println("Exception occurred in getAllArtistsWhereNameLike(): " + e.getMessage());
        }
        return result;
    }
    /** Return all artists */

    @Override
    public List<Artist> getAllArtists() {
        List<Artist> result = new ArrayList<>();
        final String query = "SELECT * FROM Artists ORDER BY artistID";

        try (Connection con = super.getConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                result.add(mapRowBasic(rs));
            }

        } catch (SQLException e) {
            System.out.println("Exception occurred in getAllArtists(): " + e.getMessage());
        }
        return result;
    }
    /** Map current row to Artist */

    private Artist mapRowBasic(ResultSet rs) throws SQLException {
        Artist a = new Artist();
        a.setArtistId(rs.getInt("artistID"));
        a.setName(rs.getString("name"));
        return a;
    }
}
