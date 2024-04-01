package hexlet.code.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LabelCreateDTO {

    @NotBlank
    @Size(min = 3, max = 1000)
    @Column(unique = true)
    private String name;

}
