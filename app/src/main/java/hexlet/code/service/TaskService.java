package hexlet.code.service;

import hexlet.code.dto.TaskCreateDTO;
import hexlet.code.dto.TaskDTO;
import hexlet.code.dto.TaskParamsDTO;
import hexlet.code.dto.TaskUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.model.Task;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.specification.TaskSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TaskService {

    public static final int DEF_PAGE_SIZE = 10;

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private TaskSpecification specBuilder;

    public List<TaskDTO> getAllTasksWithParams(TaskParamsDTO params, int page) {
        Specification<Task> specification = specBuilder.build(params);
        List<Task> allTasksWithParams = taskRepository.findAll(specification); //, PageRequest.of(page - 1, DEF_PAGE_SIZE));
        return allTasksWithParams.stream()
                .map(taskMapper::map)
                .toList();
    }

    public List<TaskDTO> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream()
                .map(taskMapper::map)
                .toList();
    }

    public TaskDTO findTaskById(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User with id " + id + " not found"));
        return taskMapper.map(task);
    }

    public TaskDTO createTask(TaskCreateDTO createDTO) {
        Task task = taskMapper.mapToModel(createDTO);
        taskRepository.save(task);
        return taskMapper.map(task);
    }

    public TaskDTO updateTask(TaskUpdateDTO updateDTO, Long id) {
        Task task = taskRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User with id " + id + " not found"));
        taskMapper.update(updateDTO, task);
        taskRepository.save(task);
        return taskMapper.map(task);
    }

    public void deleteTaskById(Long id) {
        taskRepository.deleteById(id);
    }
}
