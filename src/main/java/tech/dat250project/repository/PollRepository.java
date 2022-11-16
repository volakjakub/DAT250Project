package tech.dat250project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tech.dat250project.model.Poll;

import java.util.List;
import java.util.Optional;

@Repository
public interface PollRepository  extends JpaRepository<Poll, Long> {
    @Query("SELECT p FROM Poll p WHERE p.author.id = ?1")
    List<Poll> findAllByPersonId(Long personId);
    @Query("SELECT p FROM Poll p WHERE p.status = true AND p.opened = true")
    List<Poll> findAllPublicPolls();
    @Query("SELECT p FROM Poll p WHERE p.code = ?1")
    Optional<Poll> findByCode(String code);
}
