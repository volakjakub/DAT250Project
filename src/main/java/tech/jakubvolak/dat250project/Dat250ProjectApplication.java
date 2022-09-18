package tech.jakubvolak.dat250project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@SpringBootApplication
public class Dat250ProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(Dat250ProjectApplication.class, args);
    }
}
