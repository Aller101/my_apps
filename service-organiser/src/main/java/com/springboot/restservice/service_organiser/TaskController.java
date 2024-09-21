/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.springboot.restservice.service_organiser;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

/**
 *
 * @author alekseynesterov
 */
@RestController
@RequestMapping("api/tasks/")
public class TaskController {

    private final TaskRepository taskRepository;
    private final MessageSource messageSource;

    public TaskController(TaskRepository taskRepository,
            MessageSource messageSource) {
        this.taskRepository = taskRepository;
        this.messageSource = messageSource;
    }

    @GetMapping
    public ResponseEntity<List<Task>> handleGetAllTasks() {
        var tasks = this.taskRepository.findAll();
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(tasks);
    }

    @PostMapping
    public ResponseEntity<?> handleAddNewTask(@RequestBody TaskPayload payload,
            UriComponentsBuilder uriComponentsBuilder,
            Locale locale) {
        if (payload.details() == null || payload.details().isBlank()) {
            var messages = messageSource.getMessage("tasks.create.details.errors.not_set",
                    new Object[0], locale);
            return ResponseEntity
                    .badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new TaskExceptionHandler(List.of(messages)));
        } else {
            var task = new Task(payload.details());
            this.taskRepository.save(task);
            return ResponseEntity.created(uriComponentsBuilder
                    .path("/api/tasks/{taskId}")
                    .build(Map.of("taskId", task.id())))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(task);
        }
    }
    
    @GetMapping("{id}")
    public ResponseEntity<Task> handleGetTaskById(@PathVariable("id") UUID id){
        var task = this.taskRepository.findById(id);
        return ResponseEntity.of(task);
    }

}
