package hexlet.code.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

import java.util.Set;

@Data
@Getter
@Setter
public class TaskUpdateDTO {

    private JsonNullable<String> title;
    private JsonNullable<Integer> index;
    private JsonNullable<String> content;
    private JsonNullable<String> status;
    private JsonNullable<Long> assignee_id;

    //Сделайте возможность добавления меток в задачи
    // при их создании и изменении
    private JsonNullable<Set<Long>> labelIds;
}
