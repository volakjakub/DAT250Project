package tech.jakubvolak.dat250project.dao;

import org.springframework.stereotype.Repository;
import tech.jakubvolak.dat250project.model.Poll;

@Repository
public class PollDAO extends AbstractDAO<Poll> {
    public PollDAO() {
        setClazz(Poll.class);
    }
}
