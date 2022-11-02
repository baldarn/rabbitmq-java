package com.coders51.rabbitmq.infra.outbox;

import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.java.Log;

@Service
@Log
public class OutboxService {

    @Value("${spring.service-name}")
    private String serviceName;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Transactional
    public void save(Outoboxable msg) throws SQLException, JsonProcessingException, ClassNotFoundException {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonMsg = objectMapper.writeValueAsString(msg);

        jdbcTemplate.update("INSERT INTO outbox(id, routing_key, type, msg, created_at) VALUES (?, ?, ?, ?, ?)",
                UUID.randomUUID(),
                msg.getRoutingKey(),
                msg.getClass().getName(),
                jsonMsg,
                new Date());
    }

    @Transactional
    public void process() throws SQLException, JsonMappingException, JsonProcessingException, ClassNotFoundException {
        Outbox outbox;
        try {
            String sql = "select * " +
                    " from outbox o " +
                    " where published is false " +
                    " order by created_at asc " +
                    " limit 1 " +
                    " for update skip locked";
            outbox = jdbcTemplate.queryForObject(sql, new OutboxMapper());

            CorrelationData correlationData = new CorrelationData(outbox.getId().toString());
            log.info("processing outbox " + outbox.getId().toString());

            ObjectMapper objectMapper = new ObjectMapper();
            Object object = objectMapper.readValue(outbox.getMsg(), Class.forName(outbox.getType()));

            rabbitTemplate.convertAndSend(serviceName, outbox.getRoutingKey(), object, message -> {
                message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                message.getMessageProperties().setCorrelationId(correlationData.getId());
                return message;
            }, correlationData);

            waitConfirmation(correlationData.getId());
            jdbcTemplate.update(
                    "update outbox set published = true where id = ?",
                    UUID.fromString(outbox.getId()));
            log.info("publish: " + outbox.getId().toString());
        } catch (EmptyResultDataAccessException e) {
            log.finer("nothing to do");
        } catch (Exception e) {
            log.severe(e.toString());
            e.printStackTrace();
            throw e;
        }
    }

    // TODO: metodo per muovere i non confermati come da pubblicare
    private void waitConfirmation(String outboxId) {
        long start = System.currentTimeMillis();
        long end = start + 5 * 1000;
        Boolean confirmed = false;
        do {
            try {
            confirmed = jdbcTemplate.queryForObject(
                    "select confirmed from outbox_confirmation where id = ?",
                    Boolean.class,
                    UUID.fromString(outboxId));
            log.info("confirmed: " + confirmed);
            } catch(Exception e) {
                log.warning("non ancora confermato");
            }
        } while ((confirmed == null || !confirmed) && System.currentTimeMillis() < end);
        if (confirmed != null && !confirmed)
            throw new Error("Message not confirmed");
    }

}
