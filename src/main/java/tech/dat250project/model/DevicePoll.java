package tech.dat250project.model;

import tech.dat250project.model.key.DevicePollKey;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@IdClass(DevicePollKey.class)
public class DevicePoll implements Serializable {
    @Id
    private Long device_id;
    @Id
    private Long poll_id;
    @OneToOne
    @JoinColumn(name = "device_id", insertable = false, updatable = false)
    private Device device;
    @OneToOne
    @JoinColumn(name = "poll_id", insertable = false, updatable = false)
    private Poll poll;

    public DevicePoll() {}

    public DevicePoll(Device device, Poll poll) {
        this.device_id = device.getId();
        this.poll_id = poll.getId();
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
