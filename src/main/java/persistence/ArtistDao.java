package persistence;

import entities.Artist;
import java.util.List;

/**
 * Minimal Artist data-access API that matches ArtistDaoImpl.
 * Add more methods later once the impl supports them.
 */
public interface ArtistDao {

    /**
     * Find one artist by primary key.
     * @param id the artistID to look up
     * @return the artist if found, otherwise null
     */
    Artist getArtistById(int id);

    /**
     * Find one artist by exact name.
     * @param name exact artist name
     * @return the artist if found, otherwise null
     */
    Artist getArtistByName(String name);

    /**
     * Find artists whose name contains the given text (case depends on DB collation).
     * @param namePart partial name to search for
     * @return matching artists (possibly empty)
     */
    List<Artist> getAllArtistsWhereNameLike(String namePart);

    /**
     * Fetch all artists.
     * @return all artists (possibly empty)
     */
    List<Artist> getAllArtists();
}
