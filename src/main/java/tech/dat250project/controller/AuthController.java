package tech.dat250project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import tech.dat250project.model.Message;
import tech.dat250project.model.Person;
import tech.dat250project.repository.PersonRepository;

@RestController
public class AuthController {
    @Autowired
    private DaoAuthenticationProvider daoAuthenticationProvider;
    @Autowired
    private PersonRepository personRepository;

    @Operation(summary = "Login in to the page")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseEntity.class))})})
    @GetMapping("/login")
    public ResponseEntity<HttpStatus> index() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Login in to the page")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseEntity.class))})})
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody Person person) {

        Authentication authObject;
        try {
            authObject = daoAuthenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(person.getUsername(), person.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authObject);
        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest().body(new Message("Bad username or password!"));
        }

        return ResponseEntity.ok(personRepository.findByUsername(person.getUsername()));
    }

    @Operation(summary = "Logout from to the page")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Logout successful",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseEntity.class))})})
    @PostMapping("/logout")
    public ResponseEntity<HttpStatus> logout() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            SecurityContextHolder.getContext().setAuthentication(null);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
