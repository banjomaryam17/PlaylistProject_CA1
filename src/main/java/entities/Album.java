package entities;

import java.util.Date;
import java.util.Objects;

public class Album {
    private Integer albumId;
    private Date releaseDate;
    private String title;
    private Integer artistId;




    public Album() {
    }

    public Integer getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Integer albumId) {
        this.albumId = albumId;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getArtistId() {
        return artistId;
    }

    public void setArtistId(Integer artistId) {
        this.artistId = artistId;
    }

    public Album(Integer albumId, Date releaseDate, String title, Integer artistId) {
        this.albumId = albumId;
        this.releaseDate = releaseDate;
        this.title = title;
        this.artistId = artistId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Album album = (Album) o;
        return Objects.equals(albumId, album.albumId) && Objects.equals(releaseDate, album.releaseDate) && Objects.equals(title, album.title) && Objects.equals(artistId, album.artistId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(albumId, releaseDate, title, artistId);
    }

    @Override
    public String toString() {
        return "Album{" +
                "albumId=" + albumId +
                ", releaseDate=" + releaseDate +
                ", title='" + title + '\'' +
                ", artistId=" + artistId +
                '}';
    }
}
