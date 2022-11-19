package tech.dat250project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
public class Dat250ProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(Dat250ProjectApplication.class, args);
    }
}
