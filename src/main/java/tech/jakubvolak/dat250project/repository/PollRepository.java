package tech.jakubvolak.dat250project.repository;

import org.springframework.data.repository.CrudRepository;
import tech.jakubvolak.dat250project.model.Poll;

import java.util.List;

public interface PollRepository  extends CrudRepository<Poll, Long> {
    Poll findById(long id);
    Poll findByCode(String code);
    List<Poll> findAllByAuthorId(long id);
}
