package tech.dat250project.model;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Entity
public class Poll implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String question;
    private Date date_from;
    private Date date_to;
    private Boolean status;
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

    public Poll(String question, Date date_from, Date date_to, Boolean status, String code, Person author) {
        this.question = question;
        this.date_from = date_from;
        this.date_to = date_to;
        this.status = status;
        this.code = code;
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
    public Date getDate_from() {
        return date_from;
    }
    public void setDate_from(Date date_from) {
        this.date_from = date_from;
    }
    public Date getDate_to() {
        return date_to;
    }
    public void setDate_to(Date date_to) {
        this.date_to = date_to;
    }
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
