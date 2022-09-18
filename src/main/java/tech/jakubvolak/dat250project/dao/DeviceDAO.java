package tech.jakubvolak.dat250project.dao;

import org.springframework.stereotype.Repository;
import tech.jakubvolak.dat250project.model.Device;

@Repository
public class DeviceDAO extends AbstractDAO<Device> {
    public DeviceDAO() {
        setClazz(Device.class);
    }
}
