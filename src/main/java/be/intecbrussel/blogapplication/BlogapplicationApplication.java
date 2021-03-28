package be.intecbrussel.blogapplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@PropertySource(value = {"file:/config/application-dev.properties", "file:config/application.properties", "file:config/application-prod.properties"})
@SpringBootApplication
public class BlogapplicationApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogapplicationApplication.class, args);
    }

}
