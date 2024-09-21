/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.springboot.restservice.service_organiser;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 *
 * @author alekseynesterov
 */
interface TaskRepository {
    
    List<Task> findAll();

    void save(Task task);
    
    Optional<Task> findById(UUID id);
    
}
