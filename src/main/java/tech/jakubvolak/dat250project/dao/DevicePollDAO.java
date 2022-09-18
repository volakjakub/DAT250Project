package tech.jakubvolak.dat250project.dao;

import org.springframework.stereotype.Repository;
import tech.jakubvolak.dat250project.model.DevicePoll;

@Repository
public class DevicePollDAO extends AbstractDAO<DevicePoll> {
    public DevicePollDAO() {
        setClazz(DevicePoll.class);
    }
}
