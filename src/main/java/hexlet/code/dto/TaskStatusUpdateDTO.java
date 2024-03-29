package hexlet.code.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@Setter
public class TaskStatusUpdateDTO {
    @Column(unique = true)
    private JsonNullable<String> name;
    @Column(unique = true)
    private JsonNullable<String> slug;
}
