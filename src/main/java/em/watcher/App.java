package em.watcher;

import org.apache.log4j.PropertyConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class App {

    public static void main(String[] args) {
        String log4jPath = App.class.getClassLoader().getResource("").getPath() + "/log4j.properties";
        PropertyConfigurator.configure(log4jPath);
        SpringApplication.run(App.class, args);

    }

}