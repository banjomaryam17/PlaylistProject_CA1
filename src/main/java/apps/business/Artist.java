package apps.business;

import java.util.Date;
import java.util.Objects;

public class Artist {
    private int artistID;
    private String artistName;
    private String  genre;
    private String   hometown;
    private Date dateOfBirth;


    public Artist(int artistID, String artistName, String genre, String hometown, Date dateOfBirth) {
        this.artistID = artistID;
        this.artistName = artistName;
        this.genre = genre;
        this.hometown = hometown;
        this.dateOfBirth = dateOfBirth;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getGenre() {
        return genre;
    }

    public String getHometown() {
        return hometown;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Artist artist = (Artist) o;
        return artistID == artist.artistID ;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(artistID);
    }

    @Override
    public String toString() {
        return "Artist{" +
                "artistID=" + artistID +
                ", artistName='" + artistName + '\'' +
                ", genre='" + genre + '\'' +
                ", hometown='" + hometown + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                '}';
    }
}
