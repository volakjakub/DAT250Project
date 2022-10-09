package tech.dat250project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.dat250project.model.Device;
import tech.dat250project.repository.DeviceRepository;

import java.util.List;
import java.util.Optional;

@RestController
public class DeviceController {
    @Autowired
    private DeviceRepository deviceRepository;

    @Operation(summary = "Get all devices")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the devices",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = List.class)) })})
    @GetMapping("/device")
    List<Device> all() {
        return deviceRepository.findAll();
    }

    @Operation(summary = "Get a book by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the book",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Device.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Book not found",
                    content = @Content) })
    @GetMapping("/device/{id}")
    Device detail(@PathVariable Long id) {
        Optional<Device> device = deviceRepository.findById(id);
        return device.orElse(null);
    }

    @PostMapping("/register/device")
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

    @Operation(summary = "Get a device by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the book",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Void.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Book not found",
                    content = @Content) })
    @DeleteMapping("/device/{id}")
    void delete(@PathVariable Long id) {
        deviceRepository.deleteById(id);
    }
}
