package tech.dat250project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import tech.dat250project.dweetIO.DweetPoster;
import tech.dat250project.message.Sender;
import tech.dat250project.model.*;
import tech.dat250project.repository.DevicePollRepository;
import tech.dat250project.repository.DeviceRepository;
import tech.dat250project.repository.PersonRepository;
import tech.dat250project.repository.PollRepository;
import tech.dat250project.security.UserDetailsImpl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
public class PollController {

    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private DevicePollRepository devicePollRepository;

    @Autowired
    private DweetPoster dweetPoster;

    @Autowired
    private Sender sender;

    @Operation(summary = "Fetches all the polls for logged user")
    @GetMapping("/poll")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Polls fetched successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = List.class))})
    })
    ResponseEntity all() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(pollRepository.findAllByPersonId(userDetails.getId()));
    }

    @Operation(summary = "Fetches all the public polls")
    @GetMapping("/public/poll")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Polls fetched successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = List.class))})
    })
    List<Poll> allPublic() { return pollRepository.findAllPublicPolls(); }

    @Operation(summary = "Fetches a poll given its id")
    @GetMapping("/public/poll/{code}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Poll fetched successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Poll.class))}),
            @ApiResponse(responseCode = "404", description = "Poll id not found",
                    content = @Content)
    })
    ResponseEntity publicDetail(@PathVariable String code) {
        Optional<Poll> poll = pollRepository.findByCode(code);
        if(poll.isPresent()) {
            if(poll.get().getStatus()) {
                Poll p = poll.get();
                return ResponseEntity.ok(new PollResponse(p.getId(), p.getQuestion(), p.getCode(), p.getOpened(), p.getStatus(), p.getAuthor().getId(), p.countYes(), p.countNo()));
            }
            return ResponseEntity.badRequest().body(new Message("This poll is not public."));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Message("Poll not found!"));
    }

    @Operation(summary = "Fetches a poll given its id")
    @GetMapping("/poll/{code}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Poll fetched successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Poll.class))}),
            @ApiResponse(responseCode = "404", description = "Poll id not found",
                    content = @Content)
    })
    ResponseEntity detail(@PathVariable String code) {
        Optional<Poll> poll = pollRepository.findByCode(code);
        if(poll.isPresent()) {
            Poll p = poll.get();
            return ResponseEntity.ok(new PollResponse(p.getId(), p.getQuestion(), p.getCode(), p.getOpened(), p.getStatus(), p.getAuthor().getId(), p.countYes(), p.countNo()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Message("Poll not found!"));
    }

    @Operation(summary = "Creates a new poll")
    @PostMapping("/poll")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Poll created successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Poll.class))}),
            @ApiResponse(responseCode = "404", description = "Person not found",
                    content = @Content)
    })
    ResponseEntity create(@RequestBody PollRequest poll) {
        Person person = personRepository.findById(poll.getPerson_id()).orElse(null);
        if(person != null) {
            Poll p = new Poll(poll.getQuestion(), poll.getOpened(), poll.getStatus(), person);
            try {
                Poll saved = pollRepository.save(p);
                dweetPoster.publish(saved);
                return ResponseEntity.ok(saved);
            } catch(Exception e) {
                return ResponseEntity.internalServerError().body(new Message("Something went wrong... Please, try it again."));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Message("User not found."));
    }

    @Operation(summary = "Updates a poll given its id")
    @PutMapping("/poll/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Poll updated successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Poll.class))}),
            @ApiResponse(responseCode = "404", description = "Poll not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Poll.class))})
    })
    ResponseEntity update(@RequestBody Poll newPoll, @PathVariable Long id) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Person person = personRepository.findByUsername(userDetails.getUsername()).orElse(null);
        return pollRepository.findById(id)
                .map(poll -> {
                    if(Objects.equals(person.getId(), poll.getAuthor().getId())) {
                        if (poll.getOpened() != newPoll.getOpened()) {
                            dweetPoster.publish(newPoll);
                            sender.send(poll);
                        }
                        poll.setQuestion(newPoll.getQuestion());
                        poll.setStatus(newPoll.getStatus());
                        poll.setOpened(newPoll.getOpened());
                        return ResponseEntity.ok(pollRepository.save(poll));
                    } else {
                        return ResponseEntity.badRequest().body(new Message("You are not author of this poll!"));
                    }
                })
                .orElseGet(() -> ResponseEntity.ok(pollRepository.save(new Poll(newPoll.getQuestion(), newPoll.getOpened(), newPoll.getStatus(), person))));
    }

    @Operation(summary = "Deletes a poll given its id")
    @DeleteMapping("/poll/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Poll deleted successfully",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Poll not found",
                    content = @Content)
    })
    ResponseEntity delete(@PathVariable Long id) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Person person = personRepository.findByUsername(userDetails.getUsername()).orElse(null);
        Poll poll = pollRepository.findById(id).orElse(null);
        if(poll == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Message("Poll not found!"));
        if(!Objects.equals(person.getId(), poll.getAuthor().getId())) return ResponseEntity.badRequest().body(new Message("You are not author of this poll!"));
        pollRepository.deleteById(id);
        return ResponseEntity.ok(new Message("Successfully deleted."));
    }

    @Operation(summary = "Attaches one or more devices to a poll given its id")
    @PostMapping("/poll/{id}/device")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Device(s) attached successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseEntity.class))}),
            @ApiResponse(responseCode = "404", description = "Poll not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseEntity.class))})
    })
    ResponseEntity<HttpStatus> assignDevices(@RequestBody List<Integer> ids, @PathVariable Long id) {
        Poll poll = pollRepository.findById(id).orElse(null);
        if(poll != null) {
            Device device;
            for (Integer deviceId : ids) {
                device = deviceRepository.findById(deviceId.longValue()).orElse(null);
                if(device != null) {
                    DevicePoll devicePoll = new DevicePoll(device, poll);
                    devicePollRepository.save(devicePoll);
                }
            }
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Deletes the link between a poll and a device given its id")
    @DeleteMapping("/poll/{id}/device/{deviceId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Device detached from poll successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseEntity.class))}),
            @ApiResponse(responseCode = "404", description = "Poll or device not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseEntity.class))})
    })
    ResponseEntity<HttpStatus> deleteDevice(@PathVariable Long deviceId, @PathVariable Long id) {
        DevicePoll devicePoll = devicePollRepository.findByDeviceIdAndPollId(deviceId, id);
        if(devicePoll != null) {
            devicePollRepository.delete(devicePoll);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
