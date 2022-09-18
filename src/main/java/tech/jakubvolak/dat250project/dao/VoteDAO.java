package tech.jakubvolak.dat250project.dao;

import org.springframework.stereotype.Repository;
import tech.jakubvolak.dat250project.model.Vote;

@Repository
public class VoteDAO extends AbstractDAO<Vote>{
    public VoteDAO() {
        setClazz(Vote.class);
    }
}