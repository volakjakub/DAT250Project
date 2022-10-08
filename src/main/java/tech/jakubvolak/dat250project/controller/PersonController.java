package tech.jakubvolak.dat250project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import tech.jakubvolak.dat250project.model.Person;
import tech.jakubvolak.dat250project.repository.PersonRepository;

import java.util.Optional;

@RestController
public class PersonController {
    @Autowired
    private PersonRepository personRepository;

    @GetMapping("/person/{id}")
    Person detail(@PathVariable Long id) {
        Optional<Person> person = personRepository.findById(id);
        return person.orElse(null);
    }
}
