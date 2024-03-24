package hexlet.code.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Entity
@Table(name = "tasks")
@Data
@EntityListeners(AuditingEntityListener.class)
public class Task implements BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String name; //обязательное
    private Integer index;
    private String description;
    @NotNull
    @JoinColumn(name = "task_status_id")
    @ManyToOne
    private TaskStatus taskStatus; //- обязательное. Связано с сущностью статуса
//    @JoinColumn(name = "assignee_id")
    @ManyToOne
    private User assignee; //не обязательное. Исполнитель задачи, связан с сущностью пользователя
    @CreatedDate
    private Instant createdAt;
}
