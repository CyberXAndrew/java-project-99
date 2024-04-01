package hexlet.code.mapper;

import hexlet.code.dto.TaskCreateDTO;
import hexlet.code.dto.TaskDTO;
import hexlet.code.dto.TaskUpdateDTO;
import hexlet.code.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
        uses = {JsonNullableMapper.class, ReferenceMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class TaskMapper {

    @Mapping(target = "name", source = "title")
    @Mapping(target = "description", source = "content")
    @Mapping(target = "assignee", source = "assignee_id")
    @Mapping(target = "taskStatus", source = "status")
    @Mapping(target = "labels", source = "taskLabelIds")
    public abstract Task mapToModel(TaskCreateDTO taskCreateDTO);

    @Mapping(target = "title", source = "name")
    @Mapping(target = "content", source = "description")
    @Mapping(target = "assignee_id", source = "assignee.id")
    @Mapping(target = "status", source = "taskStatus.slug")
    @Mapping(target = "taskLabelIds", source = "labels")
//    @InheritInverseConfiguration
    public abstract TaskDTO map(Task task);

//    @InheritConfiguration(name = "mapToModel")
    @Mapping(source = "title", target = "name")
    @Mapping(source = "content", target = "description")
    @Mapping(source = "assignee_id", target = "assignee")
    @Mapping(source = "status", target = "taskStatus")
    @Mapping(source = "taskLabelIds", target = "labels")
    public abstract void update(TaskUpdateDTO updateDTO, @MappingTarget Task task);
}
