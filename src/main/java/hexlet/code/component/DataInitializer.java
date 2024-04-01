package hexlet.code.component;

import hexlet.code.dto.UserCreateDTO;
import hexlet.code.model.Label;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;

import java.util.List;

@Component
@AllArgsConstructor
public class DataInitializer implements ApplicationRunner {

    @Autowired
    private final UserService userService;
    @Autowired
    private TaskStatusRepository taskStatusRepository;
    @Autowired
    private LabelRepository labelRepository;

    @Override
    public void run(ApplicationArguments args) {
        createAdministrator();
        createDefaultStatuses();
        createDefaultLabels();
    }

    public void createAdministrator() {
        UserCreateDTO userData = new UserCreateDTO();
        userData.setFirstName("Admin");
        userData.setEmail("hexlet@example.com");
        userData.setPassword("qwerty");
        userService.createUser(userData);
    }

    public void createDefaultStatuses() {
        TaskStatus draft = new TaskStatus();
        draft.setName("Draft");
        draft.setSlug("draft");

        TaskStatus toReview = new TaskStatus();
        toReview.setName("To review");
        toReview.setSlug("to_review");

        TaskStatus toBeFixed = new TaskStatus();
        toBeFixed.setName("To be fixed");
        toBeFixed.setSlug("to_be_fixed");

        TaskStatus toPublish = new TaskStatus();
        toPublish.setName("To publish");
        toPublish.setSlug("to_publish");

        TaskStatus published = new TaskStatus();
        published.setName("Published");
        published.setSlug("published");

        taskStatusRepository.saveAll(List.of(draft, toReview, toBeFixed, toPublish, published));
    }

    public void createDefaultLabels() {
        Label bugLabel = new Label();
        bugLabel.setName("bug");

        Label featureLabel = new Label();
        featureLabel.setName("feature");

        labelRepository.saveAll(List.of(bugLabel, featureLabel));
    }
}
