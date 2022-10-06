package com.coders51.rabbitmq.infra;

import java.util.Date;
import java.util.UUID;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.stereotype.Component;

import lombok.extern.java.Log;

import org.springframework.amqp.rabbit.core.RabbitTemplate.ConfirmCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

@Component
@Log
public class RabbitMQConfirmCallback implements ConfirmCallback {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        log.info("correlationId: " + correlationData.getId() + " ack: " + ack);
            jdbcTemplate.update(
                    "insert into outbox_confirmation(id, confirmed, confirmation_cause, created_at) values (?,?,?,?)",
                    UUID.fromString(correlationData.getId()), ack, cause, new Date());
    }

}
