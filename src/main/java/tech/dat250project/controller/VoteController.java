package tech.dat250project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import tech.dat250project.model.*;
import tech.dat250project.repository.*;
import tech.dat250project.security.UserDetailsImpl;

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
    @Autowired
    private PollStatisticsRepository pollStatisticsRepository;

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
    ResponseEntity create(@RequestBody VoteRequest vote) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Person person = personRepository.findByUsername(userDetails.getUsername()).orElse(null);
        Poll poll = pollRepository.findById(vote.getPoll_id()).orElse(null);
        if(poll == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Message("Poll not found!"));
        if(!poll.getOpened()) return ResponseEntity.badRequest().body(new Message("Poll is closed!"));

        Vote newVote = voteRepository.save(new Vote(vote.getAnswer(), person, null, poll));

        poll = pollRepository.findById(poll.getId()).orElse(null);
        PollStatistics pollStatistics = pollStatisticsRepository.findByPollId(poll.getId());

        if (pollStatistics != null) {
            pollStatistics.setTotalVotes(poll.getVotes().size());
            pollStatistics.setYes(poll.countYes());
            pollStatistics.setNo(poll.countNo());
        } else {
            pollStatistics = new PollStatistics(poll.getId(), poll.getQuestion(), poll.getVotes().size(), poll.countYes(), poll.countNo());
        }

        pollStatisticsRepository.save(pollStatistics);
        return ResponseEntity.ok(newVote);
    }

    @Operation(summary = "Creates a new vote")
    @PostMapping("/public/vote")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vote created successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Vote.class))}),
            @ApiResponse(responseCode = "404", description = "Person, device or poll not found",
                    content = @Content)
    })
    ResponseEntity publicCreate(@RequestBody VoteRequest vote) {
        Poll poll = pollRepository.findById(vote.getPoll_id()).orElse(null);
        if(poll == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Message("Poll not found!"));
        if(!poll.getOpened()) return ResponseEntity.badRequest().body(new Message("Poll is closed!"));
        if(!poll.getStatus()) return ResponseEntity.badRequest().body(new Message("Poll is private. Please, log in."));

        Vote newVote = voteRepository.save(new Vote(vote.getAnswer(), null, null, poll));

        poll = pollRepository.findById(poll.getId()).orElse(null);
        PollStatistics pollStatistics = pollStatisticsRepository.findByPollId(poll.getId());

        if (pollStatistics != null) {
            pollStatistics.setTotalVotes(poll.getVotes().size());
            pollStatistics.setYes(poll.countYes());
            pollStatistics.setNo(poll.countNo());
        } else {
            pollStatistics = new PollStatistics(poll.getId(), poll.getQuestion(), poll.getVotes().size(), poll.countYes(), poll.countNo());
        }

        pollStatisticsRepository.save(pollStatistics);
        return ResponseEntity.ok(newVote);

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
        if(device == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        DevicePoll devicePoll = devicePollRepository.findByDeviceId(device.getId());

        if(devicePoll == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Poll poll = devicePoll.getPoll();

        for(int i = 0; i < deviceVote.getRed(); i++) {
            voteRepository.save(new Vote(false, null, device, poll));
        }

        for(int i = 0; i < deviceVote.getGreen(); i++) {
            voteRepository.save(new Vote(true, null, device, poll));
        }
        devicePollRepository.delete(devicePoll);

        poll = pollRepository.findById(poll.getId()).orElse(null);
        PollStatistics pollStatistics = pollStatisticsRepository.findByPollId(poll.getId());

        if (pollStatistics != null) {
            pollStatistics.setTotalVotes(poll.getVotes().size());
            pollStatistics.setYes(poll.countYes());
            pollStatistics.setNo(poll.countNo());
        } else {
            pollStatistics = new PollStatistics(poll.getId(), poll.getQuestion(), poll.getVotes().size(), poll.countYes(), poll.countNo());
        }
        pollStatisticsRepository.save(pollStatistics);

        return new ResponseEntity<>(HttpStatus.OK);

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
