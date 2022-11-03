package com.coders51.rabbitmq.endpoint;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.coders51.rabbitmq.infra.pratica.Pratica;
import com.coders51.rabbitmq.infra.pratica.PraticaService;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

@RestController
@Timed(value = "rest.pratica")
public class PraticaEndpoint {

	@Autowired
	PraticaService praticaService;

	@Autowired
    private RestTemplate restTemplate;

	@Autowired
	MeterRegistry meterRegistry;

	@GetMapping("/pratiche")
	public List<Pratica> getAll() throws InterruptedException {
		Thread.sleep(2000);
		return praticaService.getAll();
	}

	@GetMapping("/pratiche/{id}")
	public Pratica get(@PathVariable UUID id) {
		return praticaService.getById(id);
	}

	@DeleteMapping("/pratiche/{id}")
	public void delete(@PathVariable UUID id) {
		// authorization test
		String resp = restTemplate.getForObject("http://localhost:8081/auth.json", String.class);
		if(resp.contains("false")) {
			// metrica
			Counter c = meterRegistry.counter("delete.unauthorized");
			c.increment(1);
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		}

		praticaService.delete(id);
	}

	@PostMapping("/pratiche/{id}")
	public Pratica update(@PathVariable UUID id, @RequestBody Pratica pratica) {
		pratica.setId(id);
		return praticaService.update(pratica);
	}

	@PostMapping("/pratiche")
	public Pratica create(@RequestBody Pratica pratica) {
		return praticaService.create(pratica);
	}

}
