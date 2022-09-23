package com.coders51.rabbitmq;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
public class BaseTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    void resetDb() {
        jdbcTemplate.execute("TRUNCATE pratica");
        jdbcTemplate.execute("TRUNCATE outbox");
    }
}
