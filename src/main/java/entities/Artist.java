package entities;

import java.util.Date;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor

@EqualsAndHashCode

public class Artist {

        @EqualsAndHashCode.Include
        private int artistId;

        private String artistName;
        private String genre;
        private Date dateOfBirth;
    }




