package de.ndjamen.todosapp.todo;

import de.ndjamen.todosapp.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class ToDoController {

    // The @Autowired annotation tells Spring to inject the ToDoRepository bean into this field.
    // This is called Dependency Injection.
    // Later we will use this repository to save and retrieve ToDos from the database.
    @Autowired
    private ToDoRepository toDoRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * The @RestController annotation tells Spring that this class will be used to handle incoming HTTP requests.
     * The @GetMapping annotation tells Spring that this method will handle GET requests.
     * The @RequestParam represent the query parameters of the GET request.
     * Each query parameter has it´s own attached value.
     */
    @GetMapping("/greet")
    public ResponseEntity<String> hello(@RequestParam(value = "firstName") String firstName, @RequestParam(value = "lastName") String lastName) {
        if(firstName == null || lastName == null) {
            return new ResponseEntity<>("Please provide a first and last name", HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>("Hello " + firstName + " " + lastName, HttpStatus.OK);
        }
    }

    /**
     * The @PostMapping annotation tells Spring that this method will handle POST requests.
     * The @RequestBody annotation tells Spring that the incoming request body should be mapped to the newToDo parameter.
     * The Request Body that is send as a JSON object should have the same structure as the ToDo class.
     */
    @PostMapping("/todo")
    public ResponseEntity<ToDo> create(@RequestBody ToDo newToDo) {
        if(newToDo.getDescription() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            // save the ToDo in the database
            toDoRepository.save(newToDo);
            return new ResponseEntity<>(newToDo, HttpStatus.CREATED);
        }
    }

    @GetMapping("/todo/{id}")
    public ResponseEntity<ToDo> get(@PathVariable Integer id) {
        if(id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            // Retrieve the ToDo from the database
            Optional<ToDo> toDoFromDb = toDoRepository.findById(id);
            if(toDoFromDb.isPresent()) {
                return new ResponseEntity<>(toDoFromDb.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity("No todo found with the id " + id, HttpStatus.NOT_FOUND);
            }
        }
    }

    @GetMapping("/todo/all")
    public ResponseEntity<Iterable<ToDo>> getAll(@RequestHeader(value = "api-secret") String secret){
        var userBySecret = userRepository.findBySecret(secret);
        if(userBySecret.isPresent()) {
            Iterable<ToDo> allToDos = toDoRepository.findAllByUserId(userBySecret.get().getId());
            return new ResponseEntity<>(allToDos, HttpStatus.OK);
        }
        return new ResponseEntity("Invalid api secret " + secret, HttpStatus.UNAUTHORIZED);
    }

    @DeleteMapping("/todo/{id}")
    public ResponseEntity delete(@PathVariable Integer id) {
        if(id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            if (!toDoRepository.existsById(id)) {
                return new ResponseEntity("No todo found with the id " + id, HttpStatus.NOT_FOUND);
            }
            // Delete the ToDo from the database
            toDoRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @PutMapping("/todo/{id}")
    public ResponseEntity<ToDo> update(@PathVariable Integer id, @RequestBody ToDo updatedToDo) {
        if(id == null || updatedToDo.getDescription() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            // Retrieve the ToDo from the database
            Optional<ToDo> toDoFromDb = toDoRepository.findById(id);
            if(toDoFromDb.isPresent()) {
                ToDo toDo = toDoFromDb.get();
                toDo.setDescription(updatedToDo.getDescription());
                toDo.setIsDone(updatedToDo.getIsDone());
                toDoRepository.save(toDo);
                return new ResponseEntity<>(toDo, HttpStatus.OK);
            } else {
                return new ResponseEntity("No todo found with the id " + id, HttpStatus.NOT_FOUND);
            }
        }
    }

    /**
     * Patch is used to update only one or more fields of a resource.
     * In our case we want to update the isDone field of a ToDo.
     * */
    @PatchMapping("/todo/{id}")
    public ResponseEntity<ToDo> setDone(@PathVariable Integer id, @RequestParam(value = "isDone") Boolean isDone) {
        if(id == null || isDone == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            // Retrieve the ToDo from the database
            Optional<ToDo> toDoFromDb = toDoRepository.findById(id);
            if(toDoFromDb.isPresent()) {
                ToDo toDo = toDoFromDb.get();
                toDo.setIsDone(isDone);
                toDoRepository.save(toDo);
                return new ResponseEntity<>(toDo, HttpStatus.OK);
            } else {
                return new ResponseEntity("No todo found with the id " + id, HttpStatus.NOT_FOUND);
            }
        }
    }
}
