package tech.dat250project.model.key;

import java.io.Serializable;

public class DevicePollKey implements Serializable {
    private Long device_id;
    private Long poll_id;

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

    @Override
    public int hashCode() {
        return 0;
    }

    public boolean equals(DevicePollKey obj) {
        return this.device_id.equals(obj.device_id) && this.poll_id.equals(obj.poll_id);
    }
}
