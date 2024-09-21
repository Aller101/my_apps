package com.springboot.restservice.service_organiser;

import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
//false - в логах посмотреть на запросы и ответы  
@AutoConfigureMockMvc(printOnlyOnFailure = false)
class TaskControllerIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    TaskRepositoryImpl inMemoryTaskRepo;

    //чистим после выполнения каждого теста 
    @AfterEach
    public void tearDown() {
        this.inMemoryTaskRepo.getTasks().clear();
    }

    @Test
    public void handleGetAllTasks_ReturnValidResponseEntity() throws Exception {
        //given
        var requestBuilder = MockMvcRequestBuilders.get("/api/tasks/");
        this.inMemoryTaskRepo.getTasks().addAll(List.of(
                new Task(UUID.fromString("6f82e592-3051-4ade-a3b7-bc997cace9d9"), "Test one", false),
                new Task(UUID.fromString("f8a4bd0d-4a25-4d7b-89eb-95092e12db29"), "Test two", true)));

        //when
        this.mockMvc.perform(requestBuilder)
                //then
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                        MockMvcResultMatchers.content().json("""
                                                     [
                                                         {
                                                             "id":"6f82e592-3051-4ade-a3b7-bc997cace9d9",
                                                             "details":"Test one",
                                                             "completed":false
                                                         },
                                                         {
                                                             "id":"f8a4bd0d-4a25-4d7b-89eb-95092e12db29",
                                                             "details":"Test two",
                                                             "completed":true
                                                         }
                                                     ]
                                                     """)
                );
    }

    @Test
    public void handleAddNewTask_PayloadIsValid_ReturnValidResponseEntity() throws Exception {
        //given
        var requestBuilder = MockMvcRequestBuilders.post("/api/tasks/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                         {
                         "details":"Test three"
                         }
                         """);

        //when
        this.mockMvc.perform(requestBuilder)
                //then
                .andExpectAll(
                        MockMvcResultMatchers.status().isCreated(),
                        MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                        MockMvcResultMatchers.header().exists(HttpHeaders.LOCATION),
                        MockMvcResultMatchers.content().json("""
                                                             {
                                                             "details":"Test three",
                                                             "completed":false
                                                             }
                                                             """),
                        MockMvcResultMatchers.jsonPath("$.id").exists()
                );
        Assertions.assertNotNull(this.inMemoryTaskRepo.getTasks().get(0).id());
        Assertions.assertEquals(1, this.inMemoryTaskRepo.getTasks().size());
        Assertions.assertEquals("Test three", this.inMemoryTaskRepo.getTasks().get(0).details());
        Assertions.assertFalse(this.inMemoryTaskRepo.getTasks().get(0).completed());

    }
    
    @Test
    public void handleAddNewTask_PayloadIsInalid_ReturnValidResponseEntity() throws Exception {
        //given
        var requestBuilder = MockMvcRequestBuilders.post("/api/tasks/")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.ACCEPT_LANGUAGE,  "ru")
                .content("""
                         {
                         "details":null
                         }
                         """);

        //when
        this.mockMvc.perform(requestBuilder)
                //then
                .andExpectAll(
                        MockMvcResultMatchers.status().isBadRequest(),
                        MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                        MockMvcResultMatchers.header().doesNotExist(HttpHeaders.LOCATION),
                        MockMvcResultMatchers.content().json("""
                                                             {
                                                             "errors": ["Описание задачи должно быть указано"]
                                                             }
                                                             """, true) //второй арг - строг. валид.
                );
        Assertions.assertTrue(this.inMemoryTaskRepo.getTasks().isEmpty());
    }
}
