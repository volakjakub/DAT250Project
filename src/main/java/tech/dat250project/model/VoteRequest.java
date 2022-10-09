package tech.dat250project.model;

public class VoteRequest {
    private Boolean answer;
    private Long person_id;
    private Long device_id;
    private Long poll_id;

    public VoteRequest() {}

    public VoteRequest(Boolean answer, Long person_id, Long device_id, Long poll_id) {
        this.answer = answer;
        this.person_id = person_id;
        this.device_id = device_id;
        this.poll_id = poll_id;
    }

    public Boolean getAnswer() {
        return answer;
    }

    public void setAnswer(Boolean answer) {
        this.answer = answer;
    }

    public Long getPerson_id() {
        return person_id;
    }

    public void setPerson_id(Long person_id) {
        this.person_id = person_id;
    }

    public Long getDevice_id() {
        return device_id;
    }

    public void setDevice_id(Long device_id) {
        this.device_id = device_id;
    }

    public Long getPoll_id() {
        return poll_id;
    }

    public void setPoll_id(Long poll_id) {
        this.poll_id = poll_id;
    }
}
