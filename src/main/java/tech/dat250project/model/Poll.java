package tech.dat250project.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "person_id", insertable = false, updatable = false)
    @JsonBackReference
    private Person author;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "poll_id")
    @JsonManagedReference
    private final Collection<Vote> votes = new ArrayList<>();

    public Poll() {}

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
