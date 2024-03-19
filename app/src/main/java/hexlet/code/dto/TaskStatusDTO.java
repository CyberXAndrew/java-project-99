package hexlet.code.dto;

import lombok.Getter;
import lombok.Setter;
import java.sql.Timestamp;

@Getter
@Setter
public class TaskStatusDTO {
    private String name;
    private String slug;
    private Timestamp createdAt;
}
