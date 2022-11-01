package tech.dat250project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.dat250project.model.Poll;

@Repository
public interface PollRepository  extends JpaRepository<Poll, Long> {}
