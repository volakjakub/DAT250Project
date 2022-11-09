package tech.dat250project.model;

import java.util.Date;

public class PollRequest {
    private String question;
    private Boolean opened;
    private Boolean status;
    private String code;
    private Long person_id;

    public PollRequest() {}

    public PollRequest(String question, Boolean opened, Boolean status, String code, Long person_id) {
        this.question = question;
        this.opened = opened;
        this.status = status;
        this.code = code;
        this.person_id = person_id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Boolean getOpened() {
        return opened;
    }

    public void setOpened(Boolean opened) {
        this.opened = opened;
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

    public void setCode(String code) {
        this.code = code;
    }

    public Long getPerson_id() {
        return person_id;
    }

    public void setPerson_id(Long person_id) {
        this.person_id = person_id;
    }
}
