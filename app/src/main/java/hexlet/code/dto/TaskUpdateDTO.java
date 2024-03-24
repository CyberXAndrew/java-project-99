package hexlet.code.dto;

import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;

@Data
@Getter
@Setter
public class TaskUpdateDTO {

    private JsonNullable<String> title; //обязательное
    private JsonNullable<Integer> index;
    private JsonNullable<String> content;
    private JsonNullable<String> status; //- обязательное. Связано с сущностью статуса
    private JsonNullable<Long> assignee_id; //не обязательное. Исполнитель задачи, связан с сущностью пользователя

}
