package tech.dat250project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import tech.dat250project.model.PollStatistics;
import tech.dat250project.repository.PollStatisticsRepository;

@RestController
public class PollStatisticsController {
    @Autowired
    private PollStatisticsRepository pollStatisticsRepository;

    @Operation(summary = "Gets statistics of a poll by the poll id")
    @GetMapping("/statistics/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Statistics found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PollStatistics.class))}),
            @ApiResponse(responseCode = "404", description = "Id not found",
                    content = @Content)
    })
    PollStatistics detail(@PathVariable Long id) {
        PollStatistics pollStatistics = pollStatisticsRepository.findByPollId(id);
        if(pollStatistics == null) return null;
        return pollStatistics;
    }
}
