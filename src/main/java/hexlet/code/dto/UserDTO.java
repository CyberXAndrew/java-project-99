package hexlet.code.dto;

import lombok.Data;
import java.time.Instant;

@Data
public class UserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Instant createdAt;
}
