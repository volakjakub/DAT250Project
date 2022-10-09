package tech.dat250project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.dat250project.model.*;
import tech.dat250project.repository.*;

import java.util.List;

@RestController
public class VoteController {
    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private PollRepository pollRepository;
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
    Vote create(@RequestBody VoteRequest vote) {
        if(vote.getPerson_id() != null) {
            Person person = personRepository.findById(vote.getPerson_id()).orElse(null);
            Poll poll = pollRepository.findById(vote.getPoll_id()).orElse(null);
            if(poll != null) {
                return voteRepository.save(new Vote(vote.getAnswer(), person, null, poll));
            }
        }
        if(vote.getDevice_id() != null) {
            Device device = deviceRepository.findById(vote.getDevice_id()).orElse(null);
            Poll poll = pollRepository.findById(vote.getPoll_id()).orElse(null);
            if(poll != null) {
                return voteRepository.save(new Vote(vote.getAnswer(), null, device, poll));
            }
        }
        return null;
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

    @DeleteMapping("/vote/{id}")
    void delete(@PathVariable Long id) {
        voteRepository.deleteById(id);
    }
}
