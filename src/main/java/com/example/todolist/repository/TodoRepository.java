package com.example.todolist.repository;

import com.example.todolist.entities.Todo;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface TodoRepository extends JpaRepository<Todo, Integer> {

    @Query("SELECT DISTINCT t FROM Todo t WHERE t.id = :todoId")
    @EntityGraph(value = "getTodoDetails")
    Todo findByTodoId(@Param("todoId") Integer todoId);

    @Query("SELECT DISTINCT t FROM Todo t")
    @EntityGraph(value = "getTodoDetails")
    Set<Todo> findAllTodos();
}
