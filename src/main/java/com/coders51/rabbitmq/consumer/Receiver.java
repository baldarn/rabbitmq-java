package com.coders51.rabbitmq.consumer;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Receiver {
    
    @Autowired
    RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "${spring.service-name}")
    public void listen(Message message) {
        System.out.println("Message read: " + message.toString());
    }


}
