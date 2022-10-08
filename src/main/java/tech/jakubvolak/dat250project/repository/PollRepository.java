package tech.jakubvolak.dat250project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.jakubvolak.dat250project.model.Poll;

public interface PollRepository  extends JpaRepository<Poll, Long> {}
