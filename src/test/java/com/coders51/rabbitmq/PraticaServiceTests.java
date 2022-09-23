package com.coders51.rabbitmq;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.coders51.rabbitmq.infra.outbox.OutboxService;
import com.coders51.rabbitmq.infra.pratica.Pratica;
import com.coders51.rabbitmq.infra.pratica.PraticaService;

class PraticaServiceTests extends BaseTest {

	@Autowired
	PraticaService praticaService;

	@Autowired
	OutboxService outboxService;

	@Test
	void itShouldCreateAPratica() {
		Pratica saved = praticaService.create(new Pratica("1"));

		Pratica get = praticaService.getById(saved.getId());
		assertNotNull(get);
	}

	@Test
	void itShouldRetrievePratiche() {
		praticaService.create(new Pratica("1"));
		praticaService.create(new Pratica("2"));
		praticaService.create(new Pratica("3"));

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<Pratica> list = praticaService.getAll();
		assertEquals(3, list.size());
	}

	@Test
	void itShouldUpdatePratiche() {
		Pratica saved = praticaService.create(new Pratica("1"));

		saved.setNome("updated");
		saved = praticaService.update(saved);
		assertEquals("updated", saved.getNome());
	}

	@Test
	void itShouldDeletePratiche() {
		Pratica saved = praticaService.create(new Pratica("1"));

		praticaService.delete(saved.getId());

		Pratica get = praticaService.getById(saved.getId());
		assertNull(get);
	}

}
