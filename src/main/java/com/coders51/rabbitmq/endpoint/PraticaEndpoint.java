package com.coders51.rabbitmq.endpoint;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.coders51.rabbitmq.infra.pratica.Pratica;
import com.coders51.rabbitmq.infra.pratica.PraticaService;

@RestController
public class PraticaEndpoint {

	@Autowired
	PraticaService praticaService;

	@GetMapping("/pratiche")
	public List<Pratica> get() {
		return praticaService.getAll();
	}

	@GetMapping("/pratiche/{id}")
	public Pratica get(@PathVariable UUID id) {
		return praticaService.getById(id);
	}

	@DeleteMapping("/pratiche/{id}")
	public void delete(@PathVariable UUID id) {
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
