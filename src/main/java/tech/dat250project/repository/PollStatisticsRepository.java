package tech.dat250project.repository;

import java.util.List;

import tech.dat250project.model.PollStatistics;
import org.springframework.data.mongodb.repository.MongoRepository;



public interface PollStatisticsRepository extends MongoRepository<PollStatistics, String> {

    public PollStatistics findByFirstName(String firstName);
    public List<PollStatistics> findByLastName(String lastName);
    public PollStatistics findByPollId(Long pollId);

}