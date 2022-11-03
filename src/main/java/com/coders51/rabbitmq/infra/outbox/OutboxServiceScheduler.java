package com.coders51.rabbitmq.infra.outbox;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Service
public class OutboxServiceScheduler {

    @Autowired
    OutboxService outboxService;

    @Scheduled(fixedDelay = 2000)
    public void process() throws JsonMappingException, JsonProcessingException, ClassNotFoundException, SQLException {
        outboxService.process();
    }

}
