package tech.dat250project.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tech.dat250project.model.Person;
import tech.dat250project.repository.PersonRepository;

import javax.transaction.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private PersonRepository personRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Person existingUser = personRepository.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException("User not found"));

        return UserDetailsImpl.build(existingUser);
    }
}
