package de.ndjamen.todosapp.user;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {

    // Optional User will return a user if it is present, otherwise it will return an empty Optional.
    Optional<User> findByEmailAndPassword(String email, String password);
    Optional<User> findBySecret(String secret);
}
