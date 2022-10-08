package tech.dat250project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tech.dat250project.model.Person;

@RestController
public class AuthController {
    @Autowired
    private DaoAuthenticationProvider daoAuthenticationProvider;

    @GetMapping("/login")
    public ResponseEntity<HttpStatus> index() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<HttpStatus> login(@RequestBody Person person) throws Exception {

        Authentication authObject;
        try {
            authObject = daoAuthenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(person.getUsername(), person.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authObject);
        } catch (BadCredentialsException e) {
            throw new Exception("Invalid credentials");
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<HttpStatus> logout() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            SecurityContextHolder.getContext().setAuthentication(null);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
