package services;

import persistence.GenreDao;
import entities.Genre;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.List;

/**
 * Service layer for Genre operations.
 * Provides business logic and validation for Genre-related actions.
 *
 * @author [Your Name]
 */
@Slf4j
public class GenreService {
    private GenreDao genreDao;

    public GenreService(GenreDao dao) {
        this.genreDao = dao;
    }

    public void shutdownService() {
        genreDao.closeConnection();
    }

    public List<Genre> getAllGenres() throws SQLException {
        log.info("Retrieving all genres");
        return genreDao.findAll();
    }

    public Genre getGenreById(int genreID) throws SQLException {
        log.info("Genre retrieval: {}", genreID);
        return genreDao.findById(genreID);
    }

    public Genre createGenre(String genreName, String description) throws SQLException {
        if (genreName == null || genreName.isBlank()) {
            throw new IllegalArgumentException("Genre name must be provided");
        }

        log.info("Creating genre: {}", genreName);
        Genre genre = Genre.builder()
                .genreName(genreName)
                .description(description)
                .build();

        return genreDao.create(genre);
    }

    public boolean updateGenre(Genre genre) throws SQLException {
        if (genre == null) {
            throw new IllegalArgumentException("Genre cannot be null");
        }
        if (genre.getGenreName() == null || genre.getGenreName().isBlank()) {
            throw new IllegalArgumentException("Genre name must be provided");
        }

        log.info("Updating genre: {}", genre.getGenreID());
        return genreDao.update(genre);
    }

    public boolean deleteGenre(int genreID) throws SQLException {
        log.info("Deleting genre: {}", genreID);
        return genreDao.delete(genreID);
    }
}