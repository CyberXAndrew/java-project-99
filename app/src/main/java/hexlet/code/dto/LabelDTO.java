package hexlet.code.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class LabelDTO {

    private Long id;
    private String name;
    private Instant createdAt;

}
