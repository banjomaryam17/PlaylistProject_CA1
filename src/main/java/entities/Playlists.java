package entities;
import java.util.Date;
import java.util.Objects;

public class Playlists {
//    CREATE TABLE playlists (
//       playlistID INT(11) NOT NULL AUTO_INCREMENT,
//       userID INT(11) NOT NULL,
//       playlistName VARCHAR(50) NOT NULL,
//       description VARCHAR(255) DEFAULT NULL,
//       createdAt DATETIME DEFAULT CURRENT_TIMESTAMP,
//       PRIMARY KEY (playlistID)
//);
    private int playlistID;
    private int userID;
    private String playlistName;
    private String description;
    private Date createdAt;

    public Playlists(){

    }

    public Playlists(int playlistID, int userID, String playlistName, String description, Date createdAt){
        this.userID = userID;
        this.playlistName = playlistName;
        this.playlistID = playlistID;
        this.description = description;
        this.createdAt = createdAt;
    }
    public int getPlaylistID(){
        return playlistID;
    }
    public int getUserID(){
        return userID;
    }
    public String getPlaylistName(){
        return playlistName;
    }

    public String getDescription(String description){
        return description;
    }
    public Date getCreatedAt(Date createdAt) {
        return createdAt;
    }
    public void setPlaylistID(int playlistID){
        this.playlistID = playlistID;
    }
    public void setUserID(int userID){
        this.userID= userID;
    }
    public void setPlaylistName(String playlistName){
        this.playlistName = playlistName;
    }
    public void setDescription(String description){
      this.description=description;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (getClass() != o.getClass()) {
            return false;
        }
        Playlists other = (Playlists) o;
        return playlistID == other.playlistID &&
                Objects.equals(playlistName, other.playlistName) &&
                Objects.equals(description, other.description);
    }
    @Override
    public int hashCode(){
        return Objects.hash(playlistID,playlistName, description);

    }
    @Override
    public String toString(){
        return "Playlists{" + "Playlist ID: "+ playlistID +
                " ,Playlist Name: "+ playlistName +
                " ,Playlist Description: "+ description +
                " , User ID: " + userID +
                " ,Created At: "+ createdAt +
                "}";
    }
}
