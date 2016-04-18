package em.watcher;

import em.watcher.mouse.Mouse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@SpringBootApplication
@RestController
public class App {

    @RequestMapping("/")
    public String home() {
        return "Hello World";
    }


    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
        try {
            Mouse.start();
            System.out.println("started");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}