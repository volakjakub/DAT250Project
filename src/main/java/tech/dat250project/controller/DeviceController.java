package tech.dat250project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.dat250project.model.Device;
import tech.dat250project.model.DevicePoll;
import tech.dat250project.repository.DevicePollRepository;
import tech.dat250project.repository.DeviceRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class DeviceController {
    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private DevicePollRepository devicePollRepository;

    @Operation(summary = "Get all free devices")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the free devices",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = List.class)) })})
    @GetMapping("/device")
    ResponseEntity all() {
        List<Device> devices = deviceRepository.findAll();
        List<DevicePoll> devicePolls = devicePollRepository.findAll();
        for(DevicePoll devicePoll: devicePolls) {
           devices.remove(devicePoll.getDevice());
        }
        return ResponseEntity.ok(devices);
    }

    @Operation(summary = "Get a device by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the device",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Device.class)) }),
            @ApiResponse(responseCode = "404", description = "Device with id not found",
                    content = @Content) })
    @GetMapping("/device/{id}")
    Device detail(@PathVariable Long id) {
        Optional<Device> device = deviceRepository.findById(id);
        return device.orElse(null);
    }

    @Operation(summary = "Saves a device in the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns device saved",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Device.class)) })})
    @PostMapping("/register/device")
    Device create(@RequestBody Device device) {
        return deviceRepository.save(device);
    }

    @Operation(summary = "Updates or creates device by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the device",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Device.class)) })})
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

    @Operation(summary = "Deletes device")
    @DeleteMapping("/device/{id}")
    void delete(@PathVariable Long id) {
        deviceRepository.deleteById(id);
    }
}
