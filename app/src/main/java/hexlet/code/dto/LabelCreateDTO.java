package hexlet.code.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;

@Data
public class LabelCreateDTO {

    @NotBlank
    @Size(min = 3, max = 1000)
    @Column(unique = true)
    private String name;

}
