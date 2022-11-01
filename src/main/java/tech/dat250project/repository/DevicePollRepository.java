package tech.dat250project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.dat250project.model.DevicePoll;
import tech.dat250project.model.key.DevicePollKey;

@Repository
public interface DevicePollRepository extends JpaRepository<DevicePoll, DevicePollKey> {
    DevicePoll findByPollId(long id);
    DevicePoll findByDeviceId(long id);
    DevicePoll findByDeviceIdAndPollId(long deviceId, long pollId);
}
