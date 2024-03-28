package hexlet.code.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

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
    private User assignee; // Не обязательное. Исполнитель задачи, связан с сущностью пользователя
    @CreatedDate
    private Instant createdAt;

    @ManyToMany //(fetch = FetchType.EAGER)
    @JoinTable(name = "task_label",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn( name = "label_id")
    )
    private Set<Label> labels = new LinkedHashSet<>();
}
