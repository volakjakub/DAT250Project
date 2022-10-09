package tech.dat250project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.dat250project.model.Vote;

public interface VoteRepository extends JpaRepository<Vote, Long> {}
