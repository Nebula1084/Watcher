package em.watcher;

import em.watcher.mouse.Mouse;
import em.watcher.user.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

@SpringBootApplication
public class App {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(App.class, args);
        try {
            Mouse.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        UserService userService = (UserService) WatcherContextUtil.getBean("userService");
        userService.register("a2", "p1", "n1");
    }

}