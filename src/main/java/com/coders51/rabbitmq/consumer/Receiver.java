package com.coders51.rabbitmq.consumer;

import java.math.BigInteger;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.stereotype.Component;

import lombok.extern.java.Log;

@Component
@Log
public class Receiver {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    Tracer tracer;

    @RabbitListener(queues = "${spring.service-name}")
    public void listen(Message message) {
        log.info("Message read: " + message.getMessageProperties().getCorrelationId());

        Span newSpan = this.tracer.nextSpan().name("exampleExecution");
        try (Tracer.SpanInScope ws = this.tracer.withSpan(newSpan.start())) {
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // ...
            // You can tag a span
            newSpan.tag("exampleValue", "123");
            // ...
            // You can log an event on a span
            newSpan.event("exampleEvent");
        }
        finally {
            // Once done remember to end the span. This will allow collecting
            // the span to send it to a distributed tracing system e.g. Zipkin
            newSpan.end();
        }
    }

}
