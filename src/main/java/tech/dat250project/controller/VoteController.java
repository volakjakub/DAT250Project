package tech.dat250project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Fetches all the votes")
    @GetMapping("/vote")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Votes fetched successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = List.class))}),
            @ApiResponse(responseCode = "404", description = "No votes were found",
                    content = @Content)
    })
    List<Vote> all() {
        return voteRepository.findAll();
    }

    @Operation(summary = "Fetches a vote given its id")
    @GetMapping("/vote/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vote fetched successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Vote.class))}),
            @ApiResponse(responseCode = "404", description = "Vote id not found",
                    content = @Content)
    })
    Vote detail(@PathVariable Long id) {
        return voteRepository.findById(id).orElse(null);
    }

    @Operation(summary = "Creates a new vote")
    @PostMapping("/vote")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vote created successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Vote.class))}),
            @ApiResponse(responseCode = "404", description = "Person, device or poll not found",
                    content = @Content)
    })
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

    @Operation(summary = "Updates poll with the answers stored in the device and then detaches it")
    @PutMapping("/vote/device")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Poll updated successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseEntity.class))}),
            @ApiResponse(responseCode = "404", description = "Device or poll not found",
                    content = @Content)
    })
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

    @Operation(summary = "Deletes a vote given his/her id")
    @DeleteMapping("/vote/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vote deleted successfully",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Poll id not found",
                    content = @Content)
    })
    void delete(@PathVariable Long id) {
        voteRepository.deleteById(id);
    }
}