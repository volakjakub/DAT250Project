package tech.dat250project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.dat250project.model.Poll;
import tech.dat250project.repository.PollRepository;

import java.util.List;

@RestController
public class PollController {
    @Autowired
    private PollRepository pollRepository;

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
}
