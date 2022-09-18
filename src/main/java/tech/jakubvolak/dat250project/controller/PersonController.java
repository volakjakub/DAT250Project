package tech.jakubvolak.dat250project.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import tech.jakubvolak.dat250project.dao.PersonDAO;
import tech.jakubvolak.dat250project.model.Person;

@RestController
public class PersonController {
    @GetMapping("/person/{id}")
    Person detail(@PathVariable Long id) {
        PersonDAO personDAO = new PersonDAO();
        return personDAO.findOne(id);
    }
}
