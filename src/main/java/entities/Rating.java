package entities;

import lombok.*;
import org.jetbrains.annotations.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@AllArgsConstructor

public class Rating {
    @EqualsAndHashCode.Include
    private final int maxScore;
    @NotNull
    private final int currentScore;
    @NotNull
    private final int songId;
}