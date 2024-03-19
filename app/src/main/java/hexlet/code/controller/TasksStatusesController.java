//package hexlet.code.controller;
//
//import hexlet.code.model.TaskStatus;
//import hexlet.code.repository.TaskStatusRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseStatus;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
//@RestController
//@RequestMapping(path = "/api/task_statuses")
//public class TasksStatusesController {
//
//    @Autowired
//    private TaskStatusRepository taskStatusRepository;
//
//    @GetMapping(path = "")
//    @ResponseStatus(HttpStatus.OK)
//    public List<TaskStatusDTO> index() {
//        List<TaskStatus> taskStatuses = taskStatusRepository.findAll();
//
//    }
//
//}
