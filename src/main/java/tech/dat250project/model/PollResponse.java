package tech.dat250project.model;

public class PollResponse {
    private final Long id;
    private final String question;
    private final String code;
    private final Boolean opened;
    private final Boolean status;
    private final Long person_id;
    private final Integer yes;
    private final Integer no;

    public PollResponse(Long id, String question, String code, Boolean opened, Boolean status, Long person_id, Integer yes, Integer no) {
        this.id = id;
        this.question = question;
        this.code = code;
        this.opened = opened;
        this.status = status;
        this.person_id = person_id;
        this.yes = yes;
        this.no = no;
    }

    public Long getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public String getCode() { return code; }

    public Boolean getOpened() {
        return opened;
    }

    public Boolean getStatus() {
        return status;
    }

    public Long getPerson_id() {
        return person_id;
    }

    public Integer getYes() {
        return yes;
    }

    public Integer getNo() {
        return no;
    }
}
