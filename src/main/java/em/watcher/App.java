package em.watcher;

import em.watcher.mouse.Mouse;
import em.watcher.user.UserService;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

@SpringBootApplication
public class App {

    public static void main(String[] args) {
        String log4jPath = App.class.getClassLoader().getResource("").getPath() + "/log4j.properties";
        PropertyConfigurator.configure(log4jPath);
        SpringApplication.run(App.class, args);

    }

}