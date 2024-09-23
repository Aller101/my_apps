/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.springboot.restservice.service_organiser;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcOperationsTaskRepository implements TaskRepository, RowMapper<Task> {

    private final JdbcOperations jdbcOperations;

    public JdbcOperationsTaskRepository(JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    public List<Task> findAll() {
        return jdbcOperations.query("select * from my_schema.t_task", this);
    }

    @Override
    public void save(Task task) {
        this.jdbcOperations.update("""
                                   insert into my_schema.t_task(id, c_details, c_completed)
                                   values (?, ?, ?)
                                   """,
                new Object[]{task.id(), task.details(), task.completed()});
    }

    @Override
    public Optional<Task> findById(UUID id) {
        return this.jdbcOperations.query("select * from my_schema.t_task where id = ?",
                new Object[]{id}, this)
                .stream().findFirst();
    }

    @Override
    public Task mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Task(rs.getObject("id", UUID.class),
                rs.getString("c_details"),
                rs.getBoolean("c_completed"));
    }

}
