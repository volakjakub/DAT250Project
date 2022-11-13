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
    @GetMapping("/poll/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Poll fetched successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Poll.class))}),
            @ApiResponse(responseCode = "404", description = "Poll id not found",
                    content = @Content)
    })
    Poll detail(@PathVariable Long id) {
        return pollRepository.findById(id).orElse(null);
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
        return ResponseEntity.notFound().build();
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
    Poll update(@RequestBody Poll newPoll, @PathVariable Long id) {
        return pollRepository.findById(id)
                .map(poll -> {
                    poll.setQuestion(newPoll.getQuestion());
                    poll.setStatus(newPoll.getStatus());
                    poll.setOpened(newPoll.getOpened());
                    if (poll.getOpened() != newPoll.getOpened()) {
                        dweetPoster.publish(newPoll);
                        sender.send(poll);
                    }
                    return pollRepository.save(poll);
                })
                .orElseGet(() -> pollRepository.save(newPoll));
    }

    @Operation(summary = "Deletes a poll given its id")
    @DeleteMapping("/poll/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Poll deleted successfully",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Poll not found",
                    content = @Content)
    })
    void delete(@PathVariable Long id) {
        pollRepository.deleteById(id);
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
