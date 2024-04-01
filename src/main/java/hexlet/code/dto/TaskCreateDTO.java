package hexlet.code.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Set;

@Data
public class TaskCreateDTO {

    @NotBlank
    private String title;
    private Integer index;
    private String content;
    @NotNull
    private String status;
    private Long assignee_id;

    private Set<Long> taskLabelIds;
}
