package xyz.sadiulhakim.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xyz.sadiulhakim.exception.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    public User findByUsername(String username) throws Exception {
        return repository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Could not find user with username " + username));
    }
}
