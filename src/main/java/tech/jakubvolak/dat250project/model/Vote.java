package tech.jakubvolak.dat250project.model;

import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Vote implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Boolean answer;
    @ManyToOne
    @JoinColumn(name = "person_id", insertable = false, updatable = false)
    @Nullable
    private Person person;
    @ManyToOne
    @JoinColumn(name = "device_id", insertable = false, updatable = false)
    @Nullable
    private Device device;
    @ManyToOne
    @JoinColumn(name = "poll_id", insertable = false, updatable = false)
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
