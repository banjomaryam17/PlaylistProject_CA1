package entities;

import lombok.*;
import java.util.Date;
import java.util.Objects;

/**
 * Playlists entity - Using advanced Lombok annotations for precise control.
 * Maps to the 'playlists' table in the database.
 *
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Playlists implements Comparable<Playlists> {

    @EqualsAndHashCode.Include
    private int playlistID;

    @NonNull
    private int userID;

    @NonNull
    private String playlistName;

    private String description;
    private Date createdAt;
    private boolean isPublic;  // Added for public/private playlist functionality

    /**
     * Custom format method for displaying playlist information.
     *
     * @return formatted string representation of the playlist
     */
    public String format() {
        String formattedText = playlistID + ": " + playlistName;
        if (description != null && !description.isEmpty()) {
            formattedText += "\n\t" + description;
        }
        formattedText += "\n\t" + (isPublic ? "Public" : "Private");
        return formattedText;
    }

    /**
     * Deep equals comparison - checks all fields for equality.
     *
     * @param p1 first Playlists to compare
     * @param p2 second Playlists to compare
     * @return true if all fields are equal, false otherwise
     */
    public static boolean deepEquals(Playlists p1, Playlists p2) {
        return Objects.equals(p1.playlistID, p2.playlistID)
                && Objects.equals(p1.userID, p2.userID)
                && Objects.equals(p1.playlistName, p2.playlistName)
                && Objects.equals(p1.description, p2.description)
                && Objects.equals(p1.createdAt, p2.createdAt)
                && Objects.equals(p1.isPublic, p2.isPublic);
    }

    /**
     * Compares this Playlists with another Playlists for ordering.
     * Playlists are ordered by their ID.
     *
     * @param p the Playlists to compare to
     * @return negative if this < p, positive if this > p, 0 if equal
     */
    @Override
    public int compareTo(Playlists p) {
        if (playlistID < p.playlistID) {
            return -1;
        } else if (playlistID > p.playlistID) {
            return 1;
        }
        return 0;
    }
}