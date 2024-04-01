package hexlet.code.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class TaskStatusDTO {
    private Long id;
    private String name;
    private String slug;
    private LocalDate createdAt;
}
