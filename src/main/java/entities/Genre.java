package entities;

import lombok.*;
import java.util.Objects;


@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Genre implements Comparable<Genre> {

    @EqualsAndHashCode.Include
    private int genreID;

    @NonNull
    private String genreName;
    private String description;

    public String format() {
        String formattedText = genreID + ": " + genreName;
        if (description != null && !description.isEmpty()) {
            formattedText = formattedText + "\n\t" + description;
        }
        return formattedText;
    }


    public static boolean deepEquals(Genre g1, Genre g2) {
        return Objects.equals(g1.genreID, g2.genreID)
                && Objects.equals(g1.genreName, g2.genreName)
                && Objects.equals(g1.description, g2.description);
    }

    @Override
    public int compareTo(Genre g) {
        if (genreID < g.genreID) {
            return -1;
        } else if (genreID > g.genreID) {
            return 1;
        }
        return 0;
    }
}