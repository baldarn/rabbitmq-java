package com.coders51.rabbitmq.infra;

import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.stereotype.Component;

import org.springframework.amqp.rabbit.core.RabbitTemplate.ReturnsCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

@Component
public class RabbitMQReturnsCallback implements ReturnsCallback {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public void returnedMessage(ReturnedMessage returned) {
        String correlationId = returned.getMessage().getMessageProperties().getCorrelationId();
        jdbcTemplate.update("update outbox set return_cause = 'NO_ROUTE' where id = '" + correlationId + "'");
    }

}
