package hexlet.code.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserCreateDTO {
    private String firstName;
    private String lastName;
    @NotBlank
    @Email
    @Column(unique = true)
    private String email;
    @NotNull
    @Size(min = 3)
    private String password;
}
