package com.coders51.rabbitmq;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.Random.class)
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
