package com.example.todolist.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


/**
 * The persistent class for the todo database table.
 */
@Entity
@Getter
@Setter
@NamedEntityGraphs(value = {
        @NamedEntityGraph(
                name = "getTodoDetails",
                attributeNodes = {
                        @NamedAttributeNode(value = "tasks"),
                })
})
public class Todo implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String description;

    private String name;

    //bi-directional many-to-one association to Task
    @OneToMany(mappedBy = "todo", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Task> tasks = new HashSet<>(0);

    public void addTask(Task task) {
        getTasks().add(task);
        task.setTodo(this);

    }

    public void removeTask(Task task) {
        getTasks().remove(task);
        task.setTodo(null);
    }
}