package tech.dat250project.repository;

import tech.dat250project.model.PollStatistics;
import org.springframework.data.mongodb.repository.MongoRepository;



public interface PollStatisticsRepository extends MongoRepository<PollStatistics, String> {
    
    public PollStatistics findByPollId(Long pollId);
}