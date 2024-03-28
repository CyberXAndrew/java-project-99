package hexlet.code.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import hexlet.code.utils.ModelGenerator;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class LabelsControllerTest {

    private final static String TEST_URL = "/api/labels";
    private Label testLabel;
    private SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor token;

    @Autowired
    private ModelGenerator modelGenerator;
    @Autowired
    private LabelRepository labelRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void beforeEach() {
        token = jwt().jwt(builder -> builder.subject("hexlet@example.com"));
        testLabel = Instancio.of(modelGenerator.getLabelModel()).create();
        labelRepository.save(testLabel);
    }

    @AfterEach
    public void afterEach() {
        labelRepository.delete(testLabel);
    }

    @Test
    public void testIndex() throws Exception {
        MvcResult result = mockMvc.perform(
                get(TEST_URL)
                        .with(token)
        ).andExpect(status().isOk()).andReturn();

        String content = result.getResponse().getContentAsString();
        assertThat(content).contains(testLabel.getName());
    }

    @Test
    public void testShow() throws Exception {
        mockMvc.perform(
                get(TEST_URL + "/{id}", testLabel.getId())
                        .with(token)
        ).andExpect(status().isOk());

        Label label = labelRepository.findById(testLabel.getId()).get();
        assertThat(label.getName()).isEqualTo(testLabel.getName());
        assertThat(label.getId()).isEqualTo(testLabel.getId());
    }

    @Test
    public void testCreate() throws Exception {
        Label labelToSave = Instancio.of(modelGenerator.getLabelModel()).create();

        mockMvc.perform(
                post(TEST_URL)
                        .with(token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(labelToSave))
        ).andExpect(status().isCreated());

        Label labelFromRepo = labelRepository.findByName(labelToSave.getName()).get();
        assertThat(labelFromRepo.getName()).isEqualTo(labelToSave.getName());
    }

    @Test
    public void testUpdate() throws Exception {
        Map<String, String> map = new HashMap<>(){{
            put("name", "Test_Name");
        }};

        mockMvc.perform(
                put(TEST_URL + "/{id}", testLabel.getId())
                        .with(token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(map))
        ).andExpect(status().isOk());

        Label updatedLabel = labelRepository.findByName("Test_Name").get();
        assertThat(updatedLabel.getName()).isEqualTo("Test_Name");
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(
                delete(TEST_URL + "/{id}", testLabel.getId())
                        .with(token)
        ).andExpect(status().isNoContent());

        Optional<Label> optional = labelRepository.findById(testLabel.getId());
        assertTrue(optional.isEmpty());
    }
}

