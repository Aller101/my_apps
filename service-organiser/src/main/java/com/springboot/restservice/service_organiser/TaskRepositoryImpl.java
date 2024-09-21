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

    //немодифицируемый список
    private List<Task> tasks1 = new LinkedList<>(List.of(
            new Task("Test one"), new Task("Test two")));
    
    //создание списка через анонимный класс {{...}}
    private List<Task> tasks = new LinkedList<>(){{
        add(new Task("Task one"));
        add(new Task("Task two"));
    }};
    
    //создание списка через отдельный класс MyTaskList
    private List<Task> tasks2 = new MyTaskList();
    
    

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

class MyTaskList extends LinkedList<Task>{

    public MyTaskList() {
        add(new Task("Task one"));
        add(new Task("Task two"));
    }

}
