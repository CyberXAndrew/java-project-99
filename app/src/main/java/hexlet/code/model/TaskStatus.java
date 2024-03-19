//package hexlet.code.model;
//
//import jakarta.persistence.Entity;
//import jakarta.persistence.EntityListeners;
//import jakarta.persistence.Table;
//import jakarta.validation.constraints.NotBlank;
//import lombok.Getter;
//import lombok.Setter;
//import org.springframework.data.annotation.CreatedDate;
//import org.springframework.data.jpa.domain.support.AuditingEntityListener;
//import java.sql.Timestamp;
//
//@Entity
//@Table(name = "task_statuses")
//@Getter
//@Setter
//@EntityListeners(AuditingEntityListener.class)
//public class TaskStatus {
//    @NotBlank
//    private String name;
//    @NotBlank
//    private String slug;
//    @CreatedDate
//    private Timestamp createdAt;
//}
