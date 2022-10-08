package tech.dat250project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.dat250project.model.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {}
