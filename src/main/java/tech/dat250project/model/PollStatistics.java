package tech.dat250project.model;

import javax.persistence.Id;


public class PollStatistics {
    
    @Id
    private String id;

    private Long pollId;
    private String question;
    private int totalVotes;
    private int yes;
    private int no;

    public PollStatistics(){}

    public PollStatistics(Long pollId, String question, int totalVotes, int yes, int no) {
        this.pollId = pollId;
        this.question = question;
        this.totalVotes = totalVotes;
        this.yes = yes;
        this.no = no;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getPollId() {
        return this.pollId;
    }

    public void setPollId(Long pollId) {
        this.pollId = pollId;
    }

    public String getQuestion() {
        return this.question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getTotalVotes() {
        return this.totalVotes;
    }

    public void setTotalVotes(int totalVotes) {
        this.totalVotes = totalVotes;
    }

    public int getYes() {
        return this.yes;
    }

    public void setYes(int yes) {
        this.yes = yes;
    }

    public int getNo() {
        return this.no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    @Override
    public String toString() {
        return this.pollId + " " + this.question + " " + this.totalVotes + " " + this.no + " " + this.yes;
    }
}
