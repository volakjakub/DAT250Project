package tech.jakubvolak.dat250project.model;

import tech.jakubvolak.dat250project.model.key.DevicePollKey;

import javax.persistence.*;

@Entity
@IdClass(DevicePollKey.class)
public class DevicePoll {
    @Id
    @OneToOne
    @JoinColumn(name = "device_id", insertable = false, updatable = false)
    private Device device;
    @Id
    @OneToOne
    @JoinColumn(name = "poll_id", insertable = false, updatable = false)
    private Poll poll;

    public DevicePoll() {}

    public DevicePoll(Device device, Poll poll) {
        this.device = device;
        this.poll = poll;
    }

    public Device getDevice() {
        return device;
    }

    public Poll getPoll() {
        return poll;
    }
}
