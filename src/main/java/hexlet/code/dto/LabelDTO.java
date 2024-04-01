package hexlet.code.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class LabelDTO {

    private Long id;
    private String name;
    private LocalDate createdAt;

}
