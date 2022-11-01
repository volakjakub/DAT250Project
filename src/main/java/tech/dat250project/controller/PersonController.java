package tech.dat250project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import tech.dat250project.model.Message;
import tech.dat250project.repository.PersonRepository;
import tech.dat250project.model.Person;

import java.util.List;
import java.util.Optional;


@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class PersonController {
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Operation(summary = "Gets all the persons")
    @GetMapping("/person")
    List<Person> all() {
        return personRepository.findAll();
    }

    @Operation(summary = "Gets a person by his/her id")
    @GetMapping("/person/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Person found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Person.class))}),
            @ApiResponse(responseCode = "404", description = "Id not found",
                    content = @Content)
    })
    Person detail(@PathVariable Long id) {
        Optional<Person> person = personRepository.findById(id);
        return person.orElse(null);
    }

    @PostMapping("/register")
    @Operation(summary = "Adds a new person")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Person added succesfully",
                    content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = Person.class))})
    })
    ResponseEntity create(@RequestBody Person person) {
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        try {
            return ResponseEntity.ok(personRepository.save(person));
        } catch(Exception e) {
            return ResponseEntity.badRequest().body(new Message("Something went wrong... Please, try it again."));
        }
    }

    @Operation(summary = "Edits the parameters of a person given his/her id")
    @PutMapping("/person/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Person modified succesfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Person.class))}),
            @ApiResponse(responseCode = "404", description = "Id not found",
                    content = @Content)
    })
    Person update(@RequestBody Person newPerson, @PathVariable Long id) {
        return personRepository.findById(id)
                .map(person -> {
                    person.setUsername(newPerson.getUsername());
                    person.setEmail(newPerson.getEmail());
                    person.setPassword(passwordEncoder.encode(newPerson.getPassword()));
                    return personRepository.save(person);
                })
                .orElseGet(() -> {
                    newPerson.setPassword(passwordEncoder.encode(newPerson.getPassword()));
                    return personRepository.save(newPerson);
                });
    }

    @Operation(summary = "Deletes a person given his/her id")
    @DeleteMapping("/person/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Person deleted succesfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Person.class))}),
            @ApiResponse(responseCode = "404", description = "Id not found",
                    content = @Content)
    })
    void delete(@PathVariable Long id) {
        personRepository.deleteById(id);
    }
}
