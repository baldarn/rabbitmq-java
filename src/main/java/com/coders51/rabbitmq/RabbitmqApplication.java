package com.coders51.rabbitmq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAutoConfiguration
public class RabbitmqApplication {

	public static void main(String[] args) {
		SpringApplication.run(RabbitmqApplication.class, args);
	}

}
