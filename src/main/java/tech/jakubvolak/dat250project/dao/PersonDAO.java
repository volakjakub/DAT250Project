package tech.jakubvolak.dat250project.dao;

import org.springframework.stereotype.Repository;
import tech.jakubvolak.dat250project.model.Person;

@Repository
public class PersonDAO extends AbstractDAO< Person > {
    public PersonDAO() {
        setClazz(Person.class );
    }
}
