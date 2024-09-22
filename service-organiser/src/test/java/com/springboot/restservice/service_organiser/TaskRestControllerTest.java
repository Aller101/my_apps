/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.springboot.restservice.service_organiser;

import java.net.URI;
import java.util.List;
import java.util.Locale;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.util.UriComponentsBuilder;

/**
 *
 * @author alekseynesterov
 */
@ExtendWith(MockitoExtension.class)
public class TaskRestControllerTest {

    @Mock
    TaskRepository taskRepository;

    @Mock
    MessageSource messageSource;

    @InjectMocks
    TaskRestController controller;

    @Test
    public void handleGetAllTasks_ReturnValidResponseEntity() {
        //given
        var tasks = List.of(
                new Task("Test one"),
                new Task("Test two")
        );
        Mockito.doReturn(tasks).when(this.taskRepository).findAll();

//        or
//        Mockito.when(this.taskRepository.findAll()).thenReturn(tasks);
        //when
        var responseEntity = this.controller.handleGetAllTasks();

        //then
        Assertions.assertNotNull(responseEntity);
        Assertions.assertEquals(MediaType.APPLICATION_JSON, responseEntity
                .getHeaders()
                .getContentType());
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertEquals(tasks, responseEntity.getBody());
    }

    @Test
    public void handleAddNewTask_PayloadIsValid_ReturnValidResponseEntity() {
        //given
        var details = "Test three";

        //when
        var responseEntity = this.controller.handleAddNewTask(
                new TaskPayload(details),
                UriComponentsBuilder.fromUriString("http://localhost:8080"),
                Locale.ENGLISH);

        //then
        Assertions.assertNotNull(responseEntity);
        Assertions.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        Assertions.assertEquals(MediaType.APPLICATION_JSON, responseEntity
                .getHeaders()
                .getContentType());

        //в тестах недопустимы ветвления
        if (responseEntity.getBody() instanceof Task task) {
            Assertions.assertNotNull(task.id());
            Assertions.assertEquals(details, task.details());
            Assertions.assertFalse(task.completed());

            Assertions.assertEquals(URI.create("http://localhost:8080/api/tasks/"
                    + task.id()), responseEntity.getHeaders().getLocation());
            //проверка добавилась ли новая запись
            Mockito.verify(this.taskRepository).save(task);

        } else {
            Assertions.assertInstanceOf(Task.class, responseEntity.getBody());
        }

    }

    @Test
    public void handleAddNewTask_PayloadIsInvalid_ReturnValidResponseEntity() {
        //given
        var details = "   ";
        var locale = Locale.US;
        var errorMessage = "Details is empty";

        Mockito.doReturn(errorMessage).when(this.messageSource)
                .getMessage("tasks.create.details.errors.not_set",
                        new Object[0], locale);

        //when
        var responseEntity = this.controller.handleAddNewTask(
                new TaskPayload(details), UriComponentsBuilder
                        .fromUriString("http://localhost:8080"), locale);

        //then
        Assertions.assertNotNull(responseEntity);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        Assertions.assertEquals(MediaType.APPLICATION_JSON, responseEntity
                .getHeaders()
                .getContentType());

        Assertions.assertEquals(new TaskExceptionHandler(List.of(errorMessage)),
                responseEntity.getBody());

        //проверить что не было обращений к репоз
        Mockito.verifyNoInteractions(taskRepository);

    }

}
