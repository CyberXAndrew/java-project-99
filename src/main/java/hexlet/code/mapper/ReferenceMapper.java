package hexlet.code.mapper;

import hexlet.code.model.Label;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskStatusRepository;
import org.mapstruct.TargetType;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.springframework.beans.factory.annotation.Autowired;

import hexlet.code.model.BaseEntity;
import jakarta.persistence.EntityManager;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING
)
public abstract class ReferenceMapper {
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private TaskStatusRepository taskStatusRepository;
    @Autowired
    private LabelRepository labelRepository;

    public <T extends BaseEntity> T toEntity(Long id, @TargetType Class<T> entityClass) {
        return id != null ? entityManager.find(entityClass, id) : null;
    }

    public <T extends BaseEntity> T toEntity(String slug, @TargetType Class<T> entityClass) {
        TaskStatus status = taskStatusRepository.findBySlug(slug).orElseThrow();
        return slug != null ? (T) status : null;
    }

    public Set<Label> idToLabel(Set<Long> labelIds) {
        System.out.println(" idToLabel РАБОТАЕТ МАППИНГ ИЗ Set<LONG> в Set<LABEL> ");
        Set<Label> labels = labelRepository.findByIdIn(labelIds);
        Set<Label> objects = !labels.isEmpty() ? labels : Collections.emptySet();
        return objects;
    }

    public Set<Long> labelToId(Set<Label> labels) {
        System.out.println(" labelToId РАБОТАЕТ МАППИНГ ИЗ Set<LABEL> в Set<LONG> ");
        Set<Long> labelIds = new LinkedHashSet<>();
        for (Label label : labels) {
            labelIds.add(label.getId());
        }
        return labelIds;
    }
}
