package com.coders51.rabbitmq.infra;

import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Return;
import com.rabbitmq.client.ReturnCallback;

import org.springframework.amqp.rabbit.core.RabbitTemplate.ConfirmCallback;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ReturnsCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

@Component
public class RabbitMQPublishConfirm implements ConfirmCallback, ReturnsCallback {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack) {
            jdbcTemplate.update("update outbox set published = true where id = '" + correlationData.getId() + "'");
        } else {
            System.out.println("Message[" + correlationData.getId() + "] failed to send to ExChange:" + cause);
        }
    }

    @Override
    public void returnedMessage(ReturnedMessage returned) {
        System.out.println("The message was not successfully routed to the specified queues");
    }

}
