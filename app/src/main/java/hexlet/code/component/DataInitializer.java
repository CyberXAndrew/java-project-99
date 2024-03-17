package hexlet.code.component;

import hexlet.code.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import hexlet.code.model.User;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class DataInitializer implements ApplicationRunner {

    @Autowired
    private final UserService userService;

    @Override
    public void run(ApplicationArguments args) {
        User userData = new User();
//        userData.setFirstName("Admin"); // сервис не поддерживает поле
        userData.setEmail("hexlet@example.com");
        userData.setPasswordDigest("qwerty");
        userService.createUser(userData);
    }
}
