package de.ndjamen.todosapp.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    private ResponseEntity<User> registerUser(@RequestBody User newUser) {
        // Generate a secret for the user
        newUser.setSecret(UUID.randomUUID().toString());

        var savedUser = userRepository.save(newUser);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @GetMapping("/users/{userId}")
    private ResponseEntity getUser(@PathVariable Integer userId) {
        var user = userRepository.findById(userId);
        if (user.isPresent()) {
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No user found for the given id " + userId, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/user/validate")
    private ResponseEntity<String> validateUser(@RequestParam(value = "email") String email, @RequestParam(value = "password") String password) {
        var user = userRepository.findByEmailAndPassword(email, password);
        if (user.isPresent()) {
            return new ResponseEntity<>("API Secret: " + user.get().getSecret(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Wrong credentials provided", HttpStatus.UNAUTHORIZED);
        }
    }
}
