package com.example.todolist.service;

import com.example.todolist.dto.TaskDto;
import com.example.todolist.dto.TodoDto;
import com.example.todolist.entities.Task;
import com.example.todolist.entities.Todo;
import com.example.todolist.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TodoService {
    @Autowired
    private TodoRepository todoRepository;
    @PersistenceContext
    private EntityManager entityManager;

    private TodoDto translateToTodoDto(Todo todo) {
        TodoDto todoDto = new TodoDto();
        todoDto.setId(todo.getId());
        todoDto.setName(todo.getName());
        todoDto.setDescription(todo.getDescription());
        if (!CollectionUtils.isEmpty(todo.getTasks())) {
            Set<TaskDto> tasks = todo.getTasks().stream().map(task -> {
                TaskDto taskDto = new TaskDto();
                taskDto.setId(task.getId());
                taskDto.setName(task.getName());
                taskDto.setDescription(task.getDescription());
                return taskDto;
            }).collect(Collectors.toSet());
            todoDto.setTasks(tasks);
        }
        return todoDto;
    }

    public TodoDto getTodoById(Integer todoId) {
        Todo todo = todoRepository.findByTodoId(todoId);
        if (Objects.nonNull(todo)) {
            return translateToTodoDto(todo);
        }
        return null;
    }

    public Set<TodoDto> getTodoList() {
        Set<TodoDto> todoSet = new HashSet<>();
        Set<Todo> todoList = todoRepository.findAllTodos();
        if (!CollectionUtils.isEmpty(todoList)) {
            todoSet = todoList.stream().map(this::translateToTodoDto).collect(Collectors.toSet());
        }
        return todoSet;
    }

    public TodoDto createTodo(TodoDto todoDto) {
        Todo todo = new Todo();
        todo.setName(todoDto.getName());
        todo.setDescription(todoDto.getDescription());
        todo = todoRepository.saveAndFlush(todo);
        todoDto.setId(todo.getId());
        return todoDto;
    }

    public TodoDto updateTodo(Integer todoId, TodoDto todoDto) {
        Optional<Todo> optionalTodo = todoRepository.findById(todoId);
        if (optionalTodo.isPresent()) {
            Todo todo = optionalTodo.get();
            todo.setName(todoDto.getName());
            todo.setDescription(todoDto.getDescription());
            populateTasks(todo, todoDto.getTasks());
            todoRepository.saveAndFlush(todo);
            return todoDto;
        }
        return null;
    }

    private void populateTasks(Todo todo, Set<TaskDto> tasks) {
        Set<Integer> tasksInDB = todo.getTasks()
                .stream()
                .map(Task::getId)
                .collect(Collectors.toSet());
        Set<Integer> tasksInReq = tasks
                .stream()
                .filter(task -> Objects.nonNull(task.getId()))
                .map(TaskDto::getId)
                .collect(Collectors.toSet());
        tasksInDB.removeAll(tasksInReq);

        tasksInDB.forEach(taskId -> {
            Task task = todo.getTasks().stream().filter(t -> t.getId().equals(taskId)).findAny().orElse(null);
            if (Objects.nonNull(task)) {
                todo.removeTask(task);
            }
        });

        tasks.forEach(taskDto -> {
            Task task = new Task();
            if (Objects.nonNull(taskDto.getId())) {
                task = todo.getTasks().stream().filter(t -> t.getId().equals(taskDto.getId())).findFirst().orElse(null);
                if (Objects.isNull(task)) {
                    task = new Task();
                }
            }
            task.setName(taskDto.getName());
            task.setDescription(taskDto.getDescription());
            todo.addTask(task);
            task.setTodo(todo);
        });
    }

    @Transactional
    public Boolean deleteTodo(Integer todoId) {
        Optional<Todo> optionalTodo = todoRepository.findById(todoId);
        if (optionalTodo.isPresent()) {
            entityManager.remove(optionalTodo.get());
            return true;
        }
        return false;
    }
}
