package exercise.controller;

import exercise.daytime.Daytime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

// BEGIN
@RestController
public class WelcomeController {
    @Autowired
    private Daytime daytime;
    //GET-запросы на адрес /welcome и выводить приветствие в зависимости от текущего времени. Приветствие должно иметь вид It is night now! Welcome to Spring!
    @GetMapping("/welcome")
    public String welcome() {
        return String.format("It is %s now! Welcome to Spring!", daytime.getName());
    }
}
// END
