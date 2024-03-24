package hexlet.code.mapper;

import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import hexlet.code.model.BaseEntity;
import jakarta.persistence.EntityManager;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING
)
public abstract class ReferenceMapper {
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private TaskStatusRepository taskStatusRepository;

    public <T extends BaseEntity> T toEntity(Long id, @TargetType Class<T> entityClass) {
        return id != null ? entityManager.find(entityClass, id) : null;
    }

    public <T extends BaseEntity> T toEntity(String slug, @TargetType Class<T> entityClass) {
        TaskStatus status = taskStatusRepository.findBySlug(slug).orElseThrow();
        return slug != null ? (T) status : null;
    }
}
