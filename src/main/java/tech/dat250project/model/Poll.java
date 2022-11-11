package tech.dat250project.model;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

@Entity
public class Poll implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String question;
    private Boolean opened;
    private Boolean status;
    @Column(unique=true)
    private String code;
    @ManyToOne
    @JoinColumn(name = "person_id")
    @JsonIncludeProperties({"id"})
    private Person author;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "poll_id")
    @JsonIgnore
    private final Collection<Vote> votes = new ArrayList<>();

    public Poll() {}

    public Poll(String question, Boolean opened, Boolean status, Person author) {
        final char[] idchars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        char[] id = new char[7];
        Random r = new Random(System.currentTimeMillis());
        for (int i = 0;  i < 7;  i++) {
            id[i] = idchars[r.nextInt(idchars.length)];

        }
        this.question = question;
        this.opened = opened;
        this.status = status;
        this.code = new String(id);
        this.author = author;
    }

    public Long getId() {
        return id;
    }
    public String getQuestion() {
        return question;
    }
    public void setQuestion(String question) {
        this.question = question;
    }
    public Boolean getOpened() { return opened; }
    public void setOpened(Boolean opened) { this.opened = opened; }
    public Boolean getStatus() {
        return status;
    }
    public void setStatus(Boolean status) {
        this.status = status;
    }
    public String getCode() {
        return code;
    }
    public Person getAuthor() {
        return author;
    }
    public Collection<Vote> getVotes() {
        return votes;
    }
}
