package hexlet.code.dto;

import lombok.Data;

@Data
public class TaskParamsDTO {

    private String titleCont;
    private Long assigneeId;
    private String status; //slug
    private Long labelId;

}
