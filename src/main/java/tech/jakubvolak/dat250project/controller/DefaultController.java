package tech.jakubvolak.dat250project.controller;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;

@Controller
public class DefaultController {
    @Autowired
    private DataSource dataSource;

    @RequestMapping("/")
    String index() {
        return "index";
    }
    @RequestMapping("/db")
    String initializer() throws SQLException {
        Connection connection = dataSource.getConnection();
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("INSERT INTO person (username, email, password) VALUES ('Jakub', 'volakjak@fit.cvut.cz', '" + DigestUtils.sha256Hex("123456") + "')");
        stmt.executeUpdate("INSERT INTO device (address, name) VALUES ('192.168.0.69', 'Local voting device')");
        stmt.executeUpdate("INSERT INTO poll (question, date_from, date_to, status, code, person_id) VALUES ('Are you OK?', '" + new Date(2022, 9, 18) + "', '" + new Date(2022, 9, 20) + "', true, '#1', 1)");
        stmt.executeUpdate("INSERT INTO device_poll (device_id, poll_id) VALUES (1, 1)");
        stmt.executeUpdate("INSERT INTO vote (answer, person_id, device_id, poll_id) VALUES (true, 1, null, 1)");
        stmt.executeUpdate("INSERT INTO vote (answer, person_id, device_id, poll_id) VALUES (false, null, 1, 1)");
        return "db";
    }
}
