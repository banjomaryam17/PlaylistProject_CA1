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

}
