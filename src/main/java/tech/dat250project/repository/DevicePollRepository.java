package tech.dat250project.repository;

import org.springframework.data.repository.CrudRepository;
import tech.dat250project.model.DevicePoll;
import tech.dat250project.model.key.DevicePollKey;

public interface DevicePollRepository extends CrudRepository<DevicePoll, DevicePollKey> {
    DevicePoll findByPollId(long id);
    DevicePoll findByDeviceId(long id);
}
