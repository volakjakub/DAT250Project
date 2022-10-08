package tech.dat250project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.dat250project.model.Device;

public interface DeviceRepository extends JpaRepository<Device, Long> {}
