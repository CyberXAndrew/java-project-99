package hexlet.code.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskStatusCreateDTO {
    @Column(unique = true)
    @NotBlank
    private String name;
    @Column(unique = true)
    @NotBlank
    private String slug;
}
