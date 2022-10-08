package tech.jakubvolak.dat250project.repository;

import org.springframework.data.repository.CrudRepository;
import tech.jakubvolak.dat250project.model.Device;

public interface DeviceRepository extends CrudRepository<Device, Long> {
    Device findById(long id);
}
