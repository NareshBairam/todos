package com.example.todolist.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
public class TodoDto implements Serializable {
    private Integer id;
    private String description;
    private String name;
    private Set<TaskDto> tasks;
}