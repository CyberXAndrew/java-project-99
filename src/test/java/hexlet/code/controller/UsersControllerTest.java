package hexlet.code.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UsersControllerTest {

    private final static String TEST_URL = "/api/users";
    private User testUser;
    private SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor token;

    @Autowired
    private ModelGenerator modelGenerator;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void beforeEach() {
        token = jwt().jwt(builder -> builder.subject("hexlet@example.com"));
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
                get(TEST_URL)
                        .with(jwt())
        ).andExpect(status().isOk()).andReturn();

        String content = result.getResponse().getContentAsString();
        assertThat(content).contains(testUser.getEmail());
    }

    @Test
    public void testShow() throws Exception {
        MvcResult result = mockMvc.perform(
                get(TEST_URL + "/{id}", testUser.getId())
                        .with(jwt())
        ).andExpect(status().isOk()).andReturn();

        String content = result.getResponse().getContentAsString();
        assertThat(content).contains(testUser.getEmail(), testUser.getFirstName());
    }

    @Test
    public void testCreate() throws Exception {
        User createUser = Instancio.of(modelGenerator.getUserModel()).create();

        mockMvc.perform(
                post(TEST_URL)
                        .with(token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUser))
        ).andExpect(status().isCreated());

        User userFromRepo = userRepository.findByEmail(createUser.getEmail()).get();

        assertNotNull(userFromRepo);
        assertThat(createUser.getFirstName()).isEqualTo(userFromRepo.getFirstName());
        assertThat(createUser.getEmail()).isEqualTo(userFromRepo.getEmail());
    }

    @Test
    public void testUpdate() throws Exception {
        Map<String, String> updateData = new HashMap<>() {{
            put("firstName", "Andrew");
            put("email", "mail@example.com");
        }};

        mockMvc.perform(
                put(TEST_URL + "/{id}", testUser.getId())
                        .with(token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateData))
        ).andExpect(status().isOk());

        User updatedUser = userRepository.findById(testUser.getId()).get();
        assertThat(updatedUser.getFirstName()).isEqualTo("Andrew");
        assertThat(updatedUser.getEmail()).isEqualTo("mail@example.com");
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(
                delete(TEST_URL + "/{id}", testUser.getId())
                        .with(token)
        ).andExpect(status().isNoContent());

        assertTrue(userRepository.findById(testUser.getId()).isEmpty());
    }
}
