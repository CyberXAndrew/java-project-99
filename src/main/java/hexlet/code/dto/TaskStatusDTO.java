package hexlet.code.dto;

import lombok.Data;
import java.time.Instant;

@Data
public class TaskStatusDTO {
    private Long id;
    private String name;
    private String slug;
    private Instant createdAt;
}
