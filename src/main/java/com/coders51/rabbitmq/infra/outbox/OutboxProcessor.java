package com.coders51.rabbitmq.infra.outbox;

import java.sql.SQLException;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OutboxProcessor {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Transactional
    public void process() throws SQLException {
        String sql = "select * " +
            " from outbox o " +
            " where published is false " +
            " order by created_at asc " +
            " limit 1 " +
            " for update skip locked ";
        Outbox outbox = jdbcTemplate.queryForObject(sql, new OutboxMapper());
        CorrelationData correlationData = new CorrelationData(outbox.getId().toString());
        rabbitTemplate.convertAndSend("service1", "ciao", "Hello from RabbitMQ!", correlationData);
    }

}
