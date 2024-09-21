/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Record.java to edit this template
 */
package com.springboot.restservice.service_organiser;

import java.util.UUID;

/**
 *
 * @author alekseynesterov
 */
public record Task(UUID id, String details, boolean completed) {

    public Task(String details){
        this(UUID.randomUUID(), details, false);
    }
    
    

}
