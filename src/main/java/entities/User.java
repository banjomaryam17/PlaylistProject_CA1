package entities;

import java.util.Objects;
import lombok.*;
@Getter
@Setter
@ToString

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {


    private String username;
    @ToString.Exclude // keeps password out of logs
     private String  password;
    private String email;
    private int userType;

}