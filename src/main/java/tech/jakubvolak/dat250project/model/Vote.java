package tech.jakubvolak.dat250project.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Vote implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Boolean answer;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "person_id", insertable = false, updatable = false)
    @Nullable
    @JsonBackReference
    private Person person;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "device_id", insertable = false, updatable = false)
    @Nullable
    @JsonBackReference
    private Device device;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "poll_id", insertable = false, updatable = false)
    @JsonBackReference
    private Poll poll;

    public Vote() {}

    public Vote(Boolean answer, @Nullable Person person, @Nullable Device device, Poll poll) {
        this.answer = answer;
        this.person = person;
        this.device = device;
        this.poll = poll;
    }

    public Long getId() {
        return id;
    }

    public Boolean getAnswer() {
        return answer;
    }

    @Nullable
    public Person getPerson() {
        return person;
    }

    @Nullable
    public Device getDevice() {
        return device;
    }

    public Poll getPoll() {
        return poll;
    }
}
