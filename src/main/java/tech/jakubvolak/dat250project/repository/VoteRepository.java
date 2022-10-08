package tech.jakubvolak.dat250project.repository;

import org.springframework.data.repository.CrudRepository;
import tech.jakubvolak.dat250project.model.Vote;

import java.util.List;

public interface VoteRepository extends CrudRepository<Vote, Long> {
    Vote findById(long id);
    List<Vote> findAllByPersonId(long id);
    List<Vote> findAllByDeviceId(long id);
    List<Vote> findAllByPollId(long id);
}
