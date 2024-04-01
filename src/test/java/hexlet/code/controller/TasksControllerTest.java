package hexlet.code.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.TaskCreateDTO;
import hexlet.code.dto.TaskDTO;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.utils.ModelGenerator;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TasksControllerTest {
    private static final String TEST_URL = "/api/tasks";
    private SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor token;
    private Task testTask;
    @Autowired
    private ModelGenerator modelGenerator;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private TaskStatusRepository taskStatusRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LabelRepository labelRepository;

    @BeforeEach
    public void beforeEach() {
        token = jwt().jwt(builder -> builder.subject("hexlet@example.com"));
        TaskStatus status = taskStatusRepository.findBySlug("draft").orElseThrow();
        testTask = Instancio.of(modelGenerator.getTaskModel())
                .set(Select.field(Task::getAssignee), null)
                .set(Select.field(Task::getLabels), Set.of())
                .create();
        testTask.setTaskStatus(status);

        taskRepository.save(testTask);
    }
    @AfterEach
    public void afterEach() {
        taskRepository.delete(testTask);
    }

    @Test
    public void testIndex() throws Exception {
        mockMvc.perform(
                get(TEST_URL)
                        .with(token)
        ).andExpect(status().isOk());

        List<Task> tasks = taskRepository.findAll();
        assertThat(tasks.stream().map(Task::getName)).contains(testTask.getName());
    }

    @Test
    @Transactional
    public void testIndexWithParams() throws Exception {
        User user1 = Instancio.of(modelGenerator.getUserModel()).create();
        User user2 = Instancio.of(modelGenerator.getUserModel()).create();
        userRepository.saveAll(List.of(user1, user2));
        TaskStatus status = taskStatusRepository.findBySlug("published").get();
        Label bug = labelRepository.findByName("bug").get();
        Label feature = labelRepository.findByName("feature").get();

        Task task = Instancio.of(modelGenerator.getTaskModel()).create();
        task.setName("Wrong_name");
        task.setAssignee(user2);
        task.setTaskStatus(status);
        task.setLabels(Set.of(bug));
        Task task1 = Instancio.of(modelGenerator.getTaskModel()).create();
        task1.setName("Contains task name");
        task1.setAssignee(user2);
        task1.setTaskStatus(status);
        task1.setLabels(Set.of(bug));
        Task task2 = Instancio.of(modelGenerator.getTaskModel()).create();
        task2.setName("Some_task");
        task2.setAssignee(user1);
        task2.setTaskStatus(status);
        task2.setLabels(Set.of(feature));
        taskRepository.saveAll(List.of(task, task1, task2));

        String queryString1 = "?titleCont=task" +
                "&assigneeId=" + user1.getId() +
                "&status=published" +
                "&labelId=" + feature.getId();

        MvcResult result = mockMvc.perform(
                get(TEST_URL + queryString1)
                        .with(token)
        ).andExpect(status().isOk()).andReturn();
        String content = result.getResponse().getContentAsString();
        List<TaskDTO> taskList = objectMapper.readValue(content, new TypeReference<>() {});
        assertThat(taskList).hasSize(1);
        assertThat(taskList.get(0).getId()).isEqualTo(task2.getId());
        assertThat(taskList.get(0).getTitle()).contains("task");
        assertThat(taskList.get(0).getAssignee_id()).isEqualTo(user1.getId());
        assertThat(taskList.get(0).getStatus()).isEqualTo(status.getSlug());
        assertThat(taskList.get(0).getTaskLabelIds()).isEqualTo(Set.of(2L));
    }

    @Test
    public void testShow() throws Exception {
        mockMvc.perform(
                get(TEST_URL + "/{id}", testTask.getId())
                        .with(token)
        ).andExpect(status().isOk());

        Task task = taskRepository.findById(testTask.getId()).get();
        assertThat(task.getAssignee()).isEqualTo(testTask.getAssignee());
        assertThat(task.getName()).isEqualTo(testTask.getName());
    }

    @Test
    @Transactional
    public void testCreate() throws Exception {
        TaskStatus status = taskStatusRepository.findBySlug("draft").get();
        TaskCreateDTO taskToSave = new TaskCreateDTO();
        taskToSave.setTitle("Test_Name");
        taskToSave.setStatus(status.getSlug());
        taskToSave.setTaskLabelIds(Set.of(1L)); // TODO

        mockMvc.perform(
                post(TEST_URL)
                        .with(token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskToSave))
        ).andExpect(status().isCreated());

        Task savedTask = taskRepository.findByName(taskToSave.getTitle()).get();

        assertNotNull(savedTask);
        assertThat(savedTask.getName()).isEqualTo(taskToSave.getTitle());
        assertThat(savedTask.getTaskStatus().getSlug()).isEqualTo(taskToSave.getStatus());
        assertThat(savedTask.getLabels()).isNotEmpty();
        assertThat(savedTask.getLabels().toString()).contains("bug");
    }

    @Test
    public void testUpdate() throws Exception {
        List<User> users = userRepository.findAll();
        HashMap<Object, Object> taskToUpdate = new HashMap<>(){{
            put("title", "Test_name");
            put("status", "to_review");
            put("index", 123);
            put("assignee_id", users.get(0).getId());
        }};

        mockMvc.perform(
                put(TEST_URL + "/{id}", testTask.getId())
                        .with(token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskToUpdate))
        ).andExpect(status().isOk());

        Task updatedTask = taskRepository.findById(testTask.getId()).get();

        assertThat(updatedTask.getName()).isEqualTo(taskToUpdate.get("title"));
        assertThat(updatedTask.getTaskStatus().getSlug()).isEqualTo(taskToUpdate.get("status"));
        assertThat(updatedTask.getIndex()).isEqualTo(taskToUpdate.get("index"));
        assertThat(updatedTask.getAssignee().getId()).isEqualTo(taskToUpdate.get("assignee_id"));
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(
                delete(TEST_URL + "/{id}", testTask.getId())
                        .with(token)
        ).andExpect(status().isNoContent());

        assertThat(taskRepository.findById(testTask.getId())).isEmpty();
    }
}
