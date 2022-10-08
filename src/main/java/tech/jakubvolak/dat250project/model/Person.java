package tech.jakubvolak.dat250project.model;

import org.apache.commons.codec.digest.DigestUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

@Entity
public class Person implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String email;
    private String password;
    @OneToMany
    @JoinColumn(name = "person_id")
    private final Collection<Poll> polls = new ArrayList<>();
    @OneToMany
    @JoinColumn(name = "person_id")
    private final Collection<Vote> votes = new ArrayList<>();

    public Person() {}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Collection<Poll> getPolls() {
        return polls;
    }

    public Collection<Vote> getVotes() {
        return votes;
    }
}
