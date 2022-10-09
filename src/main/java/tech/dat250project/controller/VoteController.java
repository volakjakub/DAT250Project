package tech.dat250project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.dat250project.model.Device;
import tech.dat250project.model.DevicePoll;
import tech.dat250project.model.DeviceVote;
import tech.dat250project.model.Vote;
import tech.dat250project.repository.DevicePollRepository;
import tech.dat250project.repository.DeviceRepository;
import tech.dat250project.repository.VoteRepository;

import java.util.List;

@RestController
public class VoteController {
    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private DevicePollRepository devicePollRepository;

    @GetMapping("/vote")
    List<Vote> all() {
        return voteRepository.findAll();
    }

    @GetMapping("/vote/{id}")
    Vote detail(@PathVariable Long id) {
        return voteRepository.findById(id).orElse(null);
    }

    @PostMapping("/vote")
    Vote create(@RequestBody Vote vote) {
        return voteRepository.save(vote);
    }

    @PutMapping("/vote/device")
    ResponseEntity<HttpStatus> createFromDevice(@RequestBody DeviceVote deviceVote) {
        Device device = deviceRepository.findByAddress(deviceVote.getAddress());
        if(device != null) {
            DevicePoll devicePoll = devicePollRepository.findByDeviceId(device.getId());
            if(devicePoll != null) {
                for(int i = 0; i < deviceVote.getRed(); i++) {
                    voteRepository.save(new Vote(false, null, device, devicePoll.getPoll()));
                }
                for(int i = 0; i < deviceVote.getGreen(); i++) {
                    voteRepository.save(new Vote(true, null, device, devicePoll.getPoll()));
                }

                devicePollRepository.delete(devicePoll);
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/poll/{id}")
    void delete(@PathVariable Long id) {
        voteRepository.deleteById(id);
    }
}
