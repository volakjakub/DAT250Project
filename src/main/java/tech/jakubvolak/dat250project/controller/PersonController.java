package tech.jakubvolak.dat250project.controller;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.jakubvolak.dat250project.model.Person;
import tech.jakubvolak.dat250project.repository.PersonRepository;

import java.util.List;
import java.util.Optional;

@RestController
public class PersonController {
    @Autowired
    private PersonRepository personRepository;

    @GetMapping("/person")
    List<Person> all() {
        return personRepository.findAll();
    }

    @GetMapping("/person/{id}")
    Person detail(@PathVariable Long id) {
        Optional<Person> person = personRepository.findById(id);
        return person.orElse(null);
    }

    @PostMapping("/person")
    Person create(@RequestBody Person person) {
        person.setPassword(DigestUtils.sha256Hex(person.getPassword()));
        return personRepository.save(person);
    }

    @PutMapping("/person/{id}")
    Person update(@RequestBody Person newPerson, @PathVariable Long id) {
        return personRepository.findById(id)
                .map(person -> {
                    person.setUsername(newPerson.getUsername());
                    person.setEmail(newPerson.getEmail());
                    person.setPassword(DigestUtils.sha256Hex(newPerson.getPassword()));
                    return personRepository.save(person);
                })
                .orElseGet(() -> personRepository.save(newPerson));
    }

    @DeleteMapping("/person/{id}")
    void delete(@PathVariable Long id) {
        personRepository.deleteById(id);
    }
}
