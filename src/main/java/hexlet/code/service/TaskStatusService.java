package hexlet.code.service;

import hexlet.code.dto.TaskStatusCreateDTO;
import hexlet.code.dto.TaskStatusUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.TaskStatusMapper;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import hexlet.code.dto.TaskStatusDTO;
import java.util.List;

@Service
public class TaskStatusService {

    @Autowired
    private TaskStatusRepository taskStatusRepository;
    @Autowired
    private TaskStatusMapper mapper;

    public List<TaskStatusDTO> getAllStatuses() {
        List<TaskStatus> taskStatuses = taskStatusRepository.findAll();
        return taskStatuses.stream()
                .map(mapper::map)
                .toList();
    }

    public TaskStatusDTO findStatusById(Long id) {
        TaskStatus taskStatus = taskStatusRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("No task status with id " + id + " found"));
        return mapper.map(taskStatus);
    }

    public TaskStatusDTO createStatus(TaskStatusCreateDTO statusCreateDTO) {
        TaskStatus status = mapper.map(statusCreateDTO);
        taskStatusRepository.save(status);
        return mapper.map(status);
    }

    public TaskStatusDTO updateStatus(TaskStatusUpdateDTO statusUpdateDTO, Long id) {
        TaskStatus taskStatus = taskStatusRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("No task status with id " + id + " found"));
        mapper.update(statusUpdateDTO, taskStatus);
        taskStatusRepository.save(taskStatus);
        return mapper.map(taskStatus);
    }

    public void deleteStatusById(Long id) {
        taskStatusRepository.deleteById(id);
    }
}
