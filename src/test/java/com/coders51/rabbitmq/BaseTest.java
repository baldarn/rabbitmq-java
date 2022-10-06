package com.coders51.rabbitmq;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.Random.class)
@DirtiesContext
public class BaseTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    RabbitAdmin admin;

    @BeforeEach
    void beforeEach() {
        resetAll();
    }

    private void resetAll() {
        jdbcTemplate.execute("TRUNCATE pratica");
        jdbcTemplate.execute("TRUNCATE outbox");
        jdbcTemplate.execute("TRUNCATE outbox_confirmation");
        admin.purgeQueue("service1");
    }
}
