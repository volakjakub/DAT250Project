package tech.jakubvolak.dat250project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import tech.jakubvolak.dat250project.model.Person;
import tech.jakubvolak.dat250project.repository.PersonRepository;

import java.util.List;
import java.util.Optional;

@RestController
public class PersonController {
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/person")
    List<Person> all() {
        return personRepository.findAll();
    }

    @GetMapping("/person/{id}")
    Person detail(@PathVariable Long id) {
        Optional<Person> person = personRepository.findById(id);
        return person.orElse(null);
    }

    @PostMapping("/register")
    Person create(@RequestBody Person person) {
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        return personRepository.save(person);
    }

    @PutMapping("/person/{id}")
    Person update(@RequestBody Person newPerson, @PathVariable Long id) {
        return personRepository.findById(id)
                .map(person -> {
                    person.setUsername(newPerson.getUsername());
                    person.setEmail(newPerson.getEmail());
                    person.setPassword(passwordEncoder.encode(newPerson.getPassword()));
                    return personRepository.save(person);
                })
                .orElseGet(() -> personRepository.save(newPerson));
    }

    @DeleteMapping("/person/{id}")
    void delete(@PathVariable Long id) {
        personRepository.deleteById(id);
    }
}
