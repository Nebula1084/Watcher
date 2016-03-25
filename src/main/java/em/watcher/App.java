package em.watcher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class App {

    @RequestMapping("/")
    public String home() {
        return "Hello World";
    }


    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

}