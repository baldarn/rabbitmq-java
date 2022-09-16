package com.coders51.rabbitmq;

import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PublishEndpoint {
    
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    OutboxProcessor outboxProcessor;
    
    @PostMapping("/publish")
    public String message(String msg) throws SQLException {

        jdbcTemplate.update("INSERT INTO outbox(id, msg, created_at) VALUES (?, ?, ?)", UUID.randomUUID(), "ciao", new Date());

        outboxProcessor.process();

        return "OK";
    }
    
}
