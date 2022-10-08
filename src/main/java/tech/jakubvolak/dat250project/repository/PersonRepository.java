package tech.jakubvolak.dat250project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.jakubvolak.dat250project.model.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {}
