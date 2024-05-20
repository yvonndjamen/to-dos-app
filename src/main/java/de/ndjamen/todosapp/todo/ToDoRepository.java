package de.ndjamen.todosapp.todo;

import org.springframework.data.repository.CrudRepository;

import java.util.Set;


// The repository is an interface that extends the CrudRepository interface.
// The CrudRepository interface provides methods to save, retrieve, update and delete entities from the database.
// The ToDoRepository interface extends the CrudRepository interface and specifies that the entity is of type ToDo and the primary key is of type Integer.
public interface ToDoRepository extends CrudRepository<ToDo, Integer> {

    // Set of ToDo will return all ToDos that are associated with the given userId.
    Set<ToDo> findAllByUserId(Integer userId);
}
