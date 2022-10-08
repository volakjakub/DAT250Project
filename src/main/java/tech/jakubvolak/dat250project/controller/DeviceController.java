package tech.jakubvolak.dat250project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.jakubvolak.dat250project.model.Device;
import tech.jakubvolak.dat250project.repository.DeviceRepository;

import java.util.List;
import java.util.Optional;

@RestController
public class DeviceController {
    @Autowired
    private DeviceRepository deviceRepository;

    @GetMapping("/device")
    List<Device> all() {
        return deviceRepository.findAll();
    }

    @GetMapping("/device/{id}")
    Device detail(@PathVariable Long id) {
        Optional<Device> device = deviceRepository.findById(id);
        return device.orElse(null);
    }

    @PostMapping("/device")
    Device create(@RequestBody Device device) {
        return deviceRepository.save(device);
    }

    @PutMapping("/device/{id}")
    Device update(@RequestBody Device newDevice, @PathVariable Long id) {
        return deviceRepository.findById(id)
                .map(device -> {
                    device.setName(newDevice.getName());
                    device.setAddress(newDevice.getAddress());
                    return deviceRepository.save(device);
                })
                .orElseGet(() -> deviceRepository.save(newDevice));
    }

    @DeleteMapping("/device/{id}")
    void delete(@PathVariable Long id) {
        deviceRepository.deleteById(id);
    }
}
