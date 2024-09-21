/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.springboot.restservice.service_organiser;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class TaskRepositoryImpl implements TaskRepository {

    //создание списка через анонимный класс {{...}}
    private List<Task> tasks = new LinkedList<>(){{
        add(new Task("Task one"));
        add(new Task("Task two"));
    }};
    
        
    @Override
    public List<Task> findAll() {
        return tasks;
    }

    @Override
    public void save(Task task) {
        tasks.add(task);
    }

    @Override
    public Optional<Task> findById(UUID id) {
        return tasks.stream().filter(task->task.id().equals(id)).findFirst();
    }

}
