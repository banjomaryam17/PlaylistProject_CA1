package entities;

import lombok.*;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Rating {

    @EqualsAndHashCode.Include
    private int songId; // current songs id, foreign key
    @NotNull
    private int currentScore;  // accumulated score from users
    @NotNull
    private int maxScore;      // total possible score (increments by 5 each rating)
}
