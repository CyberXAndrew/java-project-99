package hexlet.code.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {

    @GetMapping(path = "/welcome")
    @ResponseStatus(HttpStatus.OK)
    public String homepage() {
        return "Welcome to Spring";
    }
}
