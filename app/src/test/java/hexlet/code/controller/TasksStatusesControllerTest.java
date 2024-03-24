package hexlet.code.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.utils.ModelGenerator;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TasksStatusesControllerTest {
    private TaskStatus testTaskStatus;
    private SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor token;
    private static final String TEST_URL = "/api/task_statuses";
    private static final Long DRAFT_STATUS_DB_ID = 1L;

    @Autowired
    private ModelGenerator modelGenerator;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @BeforeEach
    public void beforeEach() {
        token = jwt().jwt(builder -> builder.subject("hexlet@example.com"));
        testTaskStatus = Instancio.of(modelGenerator.getTaskStatusModel()).create();
        taskStatusRepository.save(testTaskStatus);
    }

    @AfterEach
    public void afterEach() {
        taskStatusRepository.deleteById(testTaskStatus.getId());
    }

    @Test
    public void testIndex() throws Exception {
        mockMvc.perform(
                get(TEST_URL)
                        .with(token)
        ).andExpect(status().isOk());

        List<TaskStatus> statuses = taskStatusRepository.findAll();
        assertThat(statuses.stream().map(stat -> stat.getSlug())).contains("draft", "to_review", "to_be_fixed", "to_publish", "published");
    }

    @Test
    public void testShow() throws Exception {
        mockMvc.perform(
                get(TEST_URL + "/{id}", DRAFT_STATUS_DB_ID) // cast to int?
                        .with(token)
        ).andExpect(status().isOk());

        TaskStatus taskStatus = taskStatusRepository.findById(DRAFT_STATUS_DB_ID).get();
        assertThat(taskStatus.getSlug()).isEqualTo("draft");
        assertThat(taskStatus.getName()).isEqualTo("Draft");
    }

    @Test
    public void testCreate() throws Exception {
        TaskStatus statusToSave = Instancio.of(modelGenerator.getTaskStatusModel()).create();
        mockMvc.perform(
                post(TEST_URL)
                        .with(token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusToSave))
        ).andExpect(status().isCreated());

        TaskStatus savedStatus = taskStatusRepository.findBySlug(statusToSave.getSlug()).get();

        assertNotNull(savedStatus);
        assertThat(savedStatus.getName()).isEqualTo(statusToSave.getName());
        assertThat(savedStatus.getSlug()).isEqualTo(statusToSave.getSlug());
    }

    @Test
    public void testUpdate() throws Exception {
        Map<String, String> updateData = new HashMap<>() {{
            put("name", "Testname");
            put("slug", "Testslug");
        }};
        mockMvc.perform(
                put(TEST_URL + "/{id}", testTaskStatus.getId())
                        .with(token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateData))
        ).andExpect(status().isOk());

        TaskStatus updatedStatus = taskStatusRepository.findById(testTaskStatus.getId()).get();
        assertThat(updatedStatus.getName()).isEqualTo("Testname");
        assertThat(updatedStatus.getSlug()).isEqualTo("Testslug");
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(
                delete(TEST_URL + "/{id}", testTaskStatus.getId())
                        .with(token)
        ).andExpect(status().isNoContent());

        assertThat(taskStatusRepository.findById(testTaskStatus.getId())).isEmpty();
    }
}
