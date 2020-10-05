package com.example.todolist.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class TaskDto implements Serializable {
    private Integer id;
    private String description;
    private String name;
    private TodoDto todo;
}