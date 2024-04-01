package hexlet.code.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.Set;

@Data
public class TaskDTO {

    private Long id;
    private String title;
    private Integer index;
    private String content;
    private String status;
    private Long assignee_id;
    private LocalDate createdAt;
    private Set<Long> taskLabelIds;
}
