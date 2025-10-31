package entities;

import java.util.Date;

public class Artist {


     private int artistId;
     private String artistName;
     private String  genre;
     private Date dateOfBirth;


    public Artist() {
    }

    public Artist(int artistId, String artistName, String genre, String hometown, Date dateOfBirth) {
        this.artistId = artistId;
        this.artistName = artistName;
        this.genre = genre;
        this.dateOfBirth = dateOfBirth;
    }

    public static Object builder() {
        return new Artist();
    }


    public String getArtistName() {
        return artistName;
    }



    public int getArtistId() {
        return artistId;
    }

    public String getGenre() {
        return genre;
    }



    public Date getDateOfBirth() {
        return dateOfBirth;
    }


    public void setArtistId(int artistId) {
        this.artistId = artistId;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }


    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public String toString() {
        return "Artist{" +
                "artistId=" + artistId +
                ", artistName='" + artistName + '\'' +
                ", genre='" + genre + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                '}';
    }

    public void setName(String name) {
    }
}


