package hexlet.code.controller;

import hexlet.code.dto.TaskCreateDTO;
import hexlet.code.dto.TaskDTO;
import hexlet.code.dto.TaskParamsDTO;
import hexlet.code.dto.TaskUpdateDTO;
import hexlet.code.model.Task;
import hexlet.code.repository.TaskRepository;
import hexlet.code.service.TaskService;
import hexlet.code.specification.TaskSpecification;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TasksController {

    @Autowired
    private TaskService taskService;
    @Autowired
    private TaskRepository taskRepository; /////

    @GetMapping(path = "/tasks")
    public ResponseEntity<List<TaskDTO>> index(TaskParamsDTO params, @RequestParam(defaultValue = "1") int page) {
        List<TaskDTO> allTasksWithParams = taskService.getAllTasksWithParams(params, page);
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(allTasksWithParams.size()))
                .body(allTasksWithParams);
    }
    @GetMapping(path = "/api/tasks")
    public ResponseEntity<List<TaskDTO>> index() {
        List<TaskDTO> allTasks = taskService.getAllTasks();
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(allTasks.size()))
                .body(allTasks);
    }

    //////////////////////////////
    @GetMapping(path = "/api/tasks1")
    @ResponseStatus(HttpStatus.OK)
    public Task deleteThis() {
        List<Task> tasks = taskRepository.findAll();
        Task task = new Task();
        task.setName(tasks.get(0).getName());
        task.setTaskStatus(tasks.get(0).getTaskStatus());
        task.setLabels(tasks.get(0).getLabels());
        return task;
    }

    @GetMapping(path = "/api/tasks/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskDTO show(@PathVariable Long id) {
        return taskService.findTaskById(id);
    }

    @PostMapping(path = "/api/tasks")
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDTO create(@Valid @RequestBody TaskCreateDTO createDTO) {
        return taskService.createTask(createDTO);
    }

    @PutMapping(path = "/api/tasks/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskDTO update(@Valid @RequestBody TaskUpdateDTO updateDTO, @PathVariable Long id) {
        return taskService.updateTask(updateDTO, id);
    }

    @DeleteMapping(path = "/api/tasks/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        taskService.deleteTaskById(id);
    }
}
