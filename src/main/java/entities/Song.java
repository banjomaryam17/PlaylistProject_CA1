package entities;

import lombok.NonNull;

import java.time.LocalTime;
import java.util.Objects;

public class Song {


 private int  songID;
    private String title;
    private int artistID;
    private LocalTime length;
    private int albumID;
    private int ratingCount;
    private int ratingsSum;
    private double averageRating;

//  I i try to do  builder , but it didnt work ):    = build , is way to create complex objects  step  by step  with a fluent , API
     // of calling  a big  hard  to read constructor
//
//    public Builder songID(int songID) { this.songID = songID; return this; }
//    public Builder title(String title) { this.title = title; return this; }
//    public Builder albumID(int albumID) { this.albumID = albumID; return this; }
//    public Builder artistID(int artistID) { this.artistID = artistID; return this; }
//    public Builder length(LocalTime length) { this.length = length; return this; }
//    public Builder ratingCount(int ratingCount) { this.ratingCount = ratingCount; return this; }
//    public Builder averageRating(double averageRating) { this.averageRating = averageRating; return this; }
//    public Builder ratingsSum(int ratingsSum) { this.ratingsSum = ratingsSum; return this; }
//
//    public Song build() {
//        if (title == null) throw new NullPointerException("title is marked non-null but is null");
//        if (length == null) throw new NullPointerException("length is marked non-null but is null");
//        return new Song(songID, title, albumID, artistID, length, ratingCount, averageRating, ratingsSum);
//    }
//}



    public int getSongID() {
        return songID;
    }

    public String getTitle() {
        return title;
    }

    public int getArtistID() {
        return artistID;
    }

    public LocalTime getLength() {
        return length;
    }

    public int getAlbumID() {
        return albumID;
    }

    public int getRatingCount() {
        return ratingCount;
    }

    public int getRatingsSum() {
        return ratingsSum;
    }

    public double getAverageRating() {
        return averageRating;

    }


    public void setSongID(int songID) {
        this.songID = songID;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setArtistID(int artistID) {
        this.artistID = artistID;
    }

    public void setLength(LocalTime length) {
        this.length = length;
    }

    public void setAlbumID(int albumID) {
        this.albumID = albumID;
    }

    public void setRatingCount(int ratingCount) {
        this.ratingCount = ratingCount;
    }

    public void setRatingsSum(int ratingsSum) {
        this.ratingsSum = ratingsSum;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public Song(String title, int artistID, int albumID, LocalTime length) {
        this.title = title;
        this.artistID = artistID;
        this.albumID = albumID;
        this.length = length;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Song song)) return false;
        return songID == song.songID && artistID == song.artistID && albumID == song.albumID && ratingCount == song.ratingCount && ratingsSum == song.ratingsSum && Double.compare(averageRating, song.averageRating) == 0 && Objects.equals(title, song.title) && Objects.equals(length, song.length);
    }

    @Override
    public int hashCode() {
        return Objects.hash(songID, title, artistID, length, albumID, ratingCount, ratingsSum, averageRating);
    }

    @Override
    public String toString() {
        return "Song{" +
                "songID=" + songID +
                ", title='" + title + '\'' +
                ", artistID=" + artistID +
                ", length=" + length +
                ", albumID=" + albumID +
                ", ratingCount=" + ratingCount +
                ", ratingsSum=" + ratingsSum +
                ", averageRating=" + averageRating +
                '}';
    }



}
