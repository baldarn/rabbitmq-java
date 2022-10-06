package com.coders51.rabbitmq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;

@SpringBootApplication
@EnableScheduling
@EnableAutoConfiguration
public class RabbitmqApplication {

	@Bean
	public RestTemplate restTemplate() {
    	return new RestTemplate();
	}

	@Bean
	public TimedAspect timedAspect(MeterRegistry registry) {
		return new TimedAspect(registry);
	}
	
	public static void main(String[] args) {
		SpringApplication.run(RabbitmqApplication.class, args);
	}

}
