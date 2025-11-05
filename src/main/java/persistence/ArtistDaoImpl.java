package persistence;

import entities.Artist;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Each call loads the MySQL driver, opens a short lived connection  runs a
 *  * prepared statement, and converts rows into {@link Artist} objects.
 *  * Keep the DB settings below in sync with  the  local MySQL install.
 */
public class ArtistDaoImpl implements ArtistDao {

    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL    = "jdbc:mysql://127.0.0.1:3306/PLAYLISTSMODELS";
    private static final String USER   = "root";
    private static final String PASS   = "root";

    /**
     * Maps one result-set row to an
     * Expected columns: artistID, artistName, genre, dateOfBirth.
     */
    private Artist mapRow(ResultSet rs) throws SQLException {
        Artist a = new Artist();
        a.setArtistId(rs.getInt("artistID"));
        a.setArtistName(rs.getString("artistName"));
        a.setGenre(rs.getString("genre"));

        Date dob = null;
        java.sql.Date sqlDob = rs.getDate("dateOfBirth");
        if (sqlDob != null) dob = new Date(sqlDob.getTime());
        a.setDateOfBirth(dob);

        return a;
    }

    /**
     * Find one artist by primary key.
     * Returns {@code null} if no match.
     */
    @Override
    public Artist getArtistById(int id) {
        final String sql = "SELECT * FROM Artists WHERE artistID = ?";
        Artist artist = null;

        try {
            Class.forName(DRIVER);
            try (Connection con = DriverManager.getConnection(URL, USER, PASS);
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, id);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) artist = mapRow(rs);
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("getArtistById error: " + e.getMessage());
        }

        return artist;
    }

    /**
     * Find one artist by exact name.
     * Returns {@code null} if no match.
     */
    @Override
    public Artist getArtistByName(String name) {
        final String sql = "SELECT * FROM Artists WHERE artistName = ?";
        Artist artist = null;

        try {
            Class.forName(DRIVER);
            try (Connection con = DriverManager.getConnection(URL, USER, PASS);
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, name);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) artist = mapRow(rs);
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("getArtistByName error: " + e.getMessage());
        }

        return artist;
    }

    /**
     * Find artists whose name contains the given text
     * Collation depends on DB/table settings.
     */
    @Override
    public List<Artist> getAllArtistsWhereNameLike(String namePart) {
        final String sql = "SELECT * FROM Artists WHERE artistName LIKE ? ORDER BY artistID";
        List<Artist> list = new ArrayList<>();

        try {
            Class.forName(DRIVER);
            try (Connection con = DriverManager.getConnection(URL, USER, PASS);
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, "%" + namePart + "%");

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) list.add(mapRow(rs));
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("getAllArtistsWhereNameLike error: " + e.getMessage());
        }

        return list;
    }

    /**
     * Fetch all artists ordered by {@code artistID}.
     */
    @Override
    public List<Artist> getAllArtists() {
        final String sql = "SELECT * FROM Artists ORDER BY artistID";
        List<Artist> list = new ArrayList<>();

        try {
            Class.forName(DRIVER);
            try (Connection con = DriverManager.getConnection(URL, USER, PASS);
                 PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("getAllArtists error: " + e.getMessage());
        }

        return list;
    }
}
