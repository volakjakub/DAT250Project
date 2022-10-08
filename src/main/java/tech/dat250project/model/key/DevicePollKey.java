package tech.dat250project.model.key;

import tech.dat250project.model.Device;
import tech.dat250project.model.Poll;

import java.io.Serializable;

public class DevicePollKey implements Serializable {
    private Device device;
    private Poll poll;

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public Poll getPoll() {
        return poll;
    }

    public void setPoll(Poll poll) {
        this.poll = poll;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    public boolean equals(DevicePollKey obj) {
        return this.device.getId().equals(obj.getDevice().getId()) && this.poll.getId().equals(obj.getPoll().getId());
    }
}
