package tech.dat250project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import tech.dat250project.jwt.JwtUtils;
import tech.dat250project.model.Message;
import tech.dat250project.model.Person;
import tech.dat250project.repository.PersonRepository;
import tech.dat250project.security.UserDetailsImpl;

@RestController
public class AuthController {
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    private DaoAuthenticationProvider daoAuthenticationProvider;
    @Autowired
    private PersonRepository personRepository;

    @Operation(summary = "Info about logged user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Info about logged user",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseEntity.class))})})
    @GetMapping("/info")
    public ResponseEntity index() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(personRepository.findByUsername(userDetails.getUsername()));
    }

    @Operation(summary = "Login in to the app")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseEntity.class))})})
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody Person person) {
        Authentication authObject;
        ResponseCookie jwtCookie;
        try {
            authObject = daoAuthenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(person.getUsername(), person.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authObject);
            UserDetailsImpl userDetails = (UserDetailsImpl) authObject.getPrincipal();
            jwtCookie = jwtUtils.generateJwtCookie(userDetails);
        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest().body(new Message("Bad username or password!"));
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(personRepository.findByUsername(person.getUsername()));
    }

    @Operation(summary = "Logout from to the page")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Logout successful",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseEntity.class))})})
    @PostMapping("/logout")
    public ResponseEntity logout() {
        SecurityContextHolder.getContext().setAuthentication(null);
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(new Message("Logout success."));
    }
}
