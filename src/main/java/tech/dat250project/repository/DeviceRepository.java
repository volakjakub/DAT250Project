package tech.dat250project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.dat250project.model.Device;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
    Device findByAddress(String address);
}
