package com.example.todolist.controller;


import com.example.todolist.dto.TodoDto;
import com.example.todolist.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Set;

@RestController
public class TodoController {

    @Autowired
    private TodoService todoService;

    @GetMapping("todos/{todoId}")
    public ResponseEntity<Object> getTodoById(@PathVariable Integer todoId) {
        TodoDto todoDetails;
        try {
            todoDetails = todoService.getTodoById(todoId);
            if (Objects.isNull(todoDetails)) {
                return new ResponseEntity<>("No todo found", HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Exception while getting the todo information", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(todoDetails, HttpStatus.OK);
    }


    @GetMapping("todos")
    public ResponseEntity<Object> getTodoList() {
        Set<TodoDto> todoList;
        try {
            todoList = todoService.getTodoList();
            if (CollectionUtils.isEmpty(todoList)) {
                return new ResponseEntity<>("No todo found", HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Exception while getting the todo information", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(todoList, HttpStatus.OK);
    }

    @PostMapping(path = "todos")
    public ResponseEntity<?> createTodo(@RequestBody TodoDto todo) {
        TodoDto todoDetails;
        try {
            todoDetails = todoService.createTodo(todo);
            if (Objects.isNull(todoDetails)) {
                return new ResponseEntity<>(todoDetails, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Exception while saving the todo information", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(todoDetails, HttpStatus.OK);
    }

    @PutMapping(path = "todos/{todoId}")
    public ResponseEntity<?> updateTodo(@PathVariable Integer todoId, @RequestBody TodoDto todo) {
        TodoDto todoDetails;
        try {
            todoDetails = todoService.updateTodo(todoId, todo);
            if (Objects.isNull(todoDetails)) {
                return new ResponseEntity<>("No todo exist with id " + todo.getId(), HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Exception while updating the todo information", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(path = "todos/{todoId}")
    public ResponseEntity<?> deleteTodo(@PathVariable Integer todoId) {
        try {
            Boolean isDeleteSuccess = todoService.deleteTodo(todoId);
            if (Boolean.FALSE.equals(isDeleteSuccess)) {
                return new ResponseEntity<>("Unable delete todo with id " + todoId, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Exception while deleting the todo information", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
