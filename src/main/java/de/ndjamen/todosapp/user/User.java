package de.ndjamen.todosapp.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.ndjamen.todosapp.todo.ToDo;
import jakarta.persistence.*;

import java.util.Set;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @OneToMany
    @JoinColumn(name = "userId")
    private Set<ToDo> todos;

    private String secret;

    public void setTodos(Set<ToDo> todos) {
        this.todos = todos;
    }

    public Set<ToDo> getTodos() {
        return this.todos;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return this.password;
    }

    public String getSecret() {
        return secret;
    }

    @JsonIgnore
    public void setSecret(String secret) {
        this.secret = secret;
    }
}
