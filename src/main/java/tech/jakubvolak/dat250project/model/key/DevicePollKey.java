package tech.jakubvolak.dat250project.model.key;

import tech.jakubvolak.dat250project.model.Device;
import tech.jakubvolak.dat250project.model.Poll;

import java.io.Serializable;

public class DevicePollKey implements Serializable {
    private Device device;
    private Poll poll;
}
