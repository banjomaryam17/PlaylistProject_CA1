package persistence;
import entities.Genre;
import java.sql.SQLException;
import java.util.List;

public interface GenreDao {
    public Genre create(Genre genre) throws SQLException;
    public Genre findById(int genreID) throws SQLException;
    public List<Genre> findAll() throws SQLException;
    public boolean update(Genre genre) throws SQLException;
    public boolean delete(int genreID) throws SQLException;


}
