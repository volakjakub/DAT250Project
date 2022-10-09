package tech.dat250project.model;

import java.util.Date;

public class PollRequest {
    private String question;
    private Date date_from;
    private Date date_to;
    private Boolean status;
    private String code;
    private Long person_id;

    public PollRequest() {}

    public PollRequest(String question, Date date_from, Date date_to, Boolean status, String code, Long person_id) {
        this.question = question;
        this.date_from = date_from;
        this.date_to = date_to;
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
