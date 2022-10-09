package tech.dat250project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.dat250project.model.Device;
import tech.dat250project.model.DevicePoll;
import tech.dat250project.model.Poll;
import tech.dat250project.repository.DevicePollRepository;
import tech.dat250project.repository.PollRepository;

import java.util.List;

@RestController
public class PollController {
    @Autowired
    private PollRepository pollRepository;
    @Autowired
    private DevicePollRepository devicePollRepository;

    @GetMapping("/poll")
    List<Poll> all() {
        return pollRepository.findAll();
    }

    @GetMapping("/poll/{id}")
    Poll detail(@PathVariable Long id) {
        return pollRepository.findById(id).orElse(null);
    }

    @PostMapping("/poll")
    Poll create(@RequestBody Poll poll) {
        return pollRepository.save(poll);
    }

    @PutMapping("/poll/{id}")
    Poll update(@RequestBody Poll newPoll, @PathVariable Long id) {
        return pollRepository.findById(id)
                .map(poll -> {
                    poll.setQuestion(newPoll.getQuestion());
                    poll.setStatus(newPoll.getStatus());
                    poll.setDate_from(newPoll.getDate_from());
                    poll.setDate_to(newPoll.getDate_to());
                    return pollRepository.save(poll);
                })
                .orElseGet(() -> pollRepository.save(newPoll));
    }

    @DeleteMapping("/poll/{id}")
    void delete(@PathVariable Long id) {
        pollRepository.deleteById(id);
    }

    @PostMapping("/poll/{id}/device")
    ResponseEntity<HttpStatus> assignDevices(@RequestBody List<Device> devices, @PathVariable Long id) {
        Poll poll = pollRepository.findById(id).orElse(null);
        if(poll != null) {
            for (Device device : devices) {
                devicePollRepository.save(new DevicePoll(device, poll));
            }
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/poll/{id}/device/{deviceId}")
    ResponseEntity<HttpStatus> deleteDevice(@PathVariable Long deviceId, @PathVariable Long id) {
        DevicePoll devicePoll = devicePollRepository.findByDeviceIdAndPollId(deviceId, id);
        if(devicePoll != null) {
            devicePollRepository.delete(devicePoll);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
