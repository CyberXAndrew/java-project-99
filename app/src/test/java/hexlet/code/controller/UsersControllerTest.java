package hexlet.code.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.UserCreateDTO;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.utils.ModelGenerator;
import net.datafaker.Faker;
import org.instancio.Instancio;
import org.instancio.Model;
import org.instancio.Select;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UsersControllerTest {
    private User testUser;

    @Autowired
    private ModelGenerator modelGenerator;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private Faker faker;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void beforeEach() {
        testUser = Instancio.of(modelGenerator.getUserModel()).create();
        userRepository.save(testUser);
    }
    @AfterEach
    public void afterEach() {
        userRepository.deleteById(testUser.getId());
    }

    @Test
    public void testIndex() throws Exception {
        MvcResult result = mockMvc.perform(
                get("/api/users")
        ).andExpect(status().isOk()).andReturn();
        String content = result.getResponse().getContentAsString();
        assertThat(content).contains(testUser.getEmail());
    }

    @Test
    public void testShow() throws Exception {
        MvcResult result = mockMvc.perform(
                get("/api/users/{id}", testUser.getId())
        ).andExpect(status().isOk()).andReturn();

        String content = result.getResponse().getContentAsString();
        assertThat(content).contains(testUser.getEmail(), testUser.getFirstName());
    }

    @Test
    public void testCreate() throws Exception {
        Model<UserCreateDTO> createDTOModel = Instancio.of(UserCreateDTO.class)
                .supply(Select.field(UserCreateDTO::getEmail), () -> faker.internet().emailAddress())
                .supply(Select.field(UserCreateDTO::getPassword), () -> faker.internet().password())
                .toModel();
        UserCreateDTO createDTO = Instancio.of(createDTOModel).create();

        MvcResult result = mockMvc.perform(
                post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO))
        ).andExpect(status().isCreated()).andReturn();

        String content = result.getResponse().getContentAsString();
        assertThat(content).contains(createDTO.getEmail());
    }

    @Test
    public void testUpdate() throws Exception {
        Map<String, String> updateData = new HashMap<>() {{
            put("firstName", "Andrew");
            put("email", "mail@example.com");
        }};

        MvcResult result = mockMvc.perform(
                put("/api/users/{id}", testUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateData))
        ).andExpect(status().isOk()).andReturn();

        String content = result.getResponse().getContentAsString();

        assertThat(userRepository.findById(testUser.getId()).get().getFirstName()).contains("Andrew");
        assertThat(userRepository.findById(testUser.getId()).get().getEmail()).contains("mail@example.com");
    }

    @Test
    public void testDelete() throws Exception {
        MvcResult result = mockMvc.perform(
                delete("/api/users/{id}", testUser.getId())
        ).andExpect(status().isNoContent()).andReturn();

        assertThat(userRepository.findById(testUser.getId())).isEmpty();
    }
}
