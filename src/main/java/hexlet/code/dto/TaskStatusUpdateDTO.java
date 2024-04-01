package hexlet.code.dto;

import jakarta.persistence.Column;
import lombok.Data;
import org.openapitools.jackson.nullable.JsonNullable;

@Data
public class TaskStatusUpdateDTO {
    @Column(unique = true)
    private JsonNullable<String> name;
    @Column(unique = true)
    private JsonNullable<String> slug;
}
