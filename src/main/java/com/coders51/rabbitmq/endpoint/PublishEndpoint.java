package com.coders51.rabbitmq.endpoint;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.coders51.rabbitmq.dto.Bar;
import com.coders51.rabbitmq.dto.Foo;
import com.coders51.rabbitmq.infra.outbox.OutboxService;
import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
public class PublishEndpoint {
    
    @Autowired
    OutboxService outboxProcessor;
    
    @PostMapping("/foo")
    public String foo(@RequestBody Foo foo) throws SQLException, JsonProcessingException, ClassNotFoundException {
        outboxProcessor.save(foo);
        return "OK";
    }

    @PostMapping("/bar")
    public String bar(@RequestBody Bar bar) throws SQLException, JsonProcessingException, ClassNotFoundException {
        outboxProcessor.save(bar);
        return "OK";
    }
    
}
