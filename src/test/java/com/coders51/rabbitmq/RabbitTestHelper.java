package com.coders51.rabbitmq;

import org.springframework.amqp.rabbit.test.RabbitListenerTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RabbitListenerTest
public class RabbitTestHelper {
    
    @Bean
    public Listener listener() {
        return new Listener();
    }

}
