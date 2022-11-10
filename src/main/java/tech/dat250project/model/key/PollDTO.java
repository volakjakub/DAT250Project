package tech.dat250project.model.key;

import tech.dat250project.model.Poll;

import java.io.Serializable;

public class PollDTO implements Serializable {
    private Long id;
    private String question;
    private Boolean opened;
    private Boolean status;
    private String code;
    private Long author;

    public PollDTO(Poll p){
        this.id = p.getId();
        this.question = p.getQuestion();
        this.opened = p.getOpened();
        this.status = p.getStatus();
        this.code = p.getCode();
        this.author = p.getAuthor().getId();
    }
}
