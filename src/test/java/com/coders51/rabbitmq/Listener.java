package com.coders51.rabbitmq;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

public class Listener {

    @RabbitListener(queues="service1")
    public void consume(Message message) {
        System.out.println(message.toString());
    }

}