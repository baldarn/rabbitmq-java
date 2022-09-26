package com.coders51.rabbitmq;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.awaitility.Awaitility.await;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import com.coders51.rabbitmq.consumer.Receiver;
import com.coders51.rabbitmq.infra.pratica.Pratica;
import com.coders51.rabbitmq.infra.pratica.PraticaService;

class PraticaServiceTests extends BaseTest {

	@Autowired
	PraticaService praticaService;

	@SpyBean
	private Receiver receiver;

	@Test
	void itShouldCreateAPratica() {
		Pratica saved = praticaService.create(new Pratica("1"));
		praticaService.create(new Pratica("2"));
		praticaService.create(new Pratica("3"));

		await().atMost(5, TimeUnit.SECONDS)
				.untilAsserted(() -> verify(receiver, times(3)).listen(any()));

		Pratica get = praticaService.getById(saved.getId());
		assertNotNull(get);
	}

	@Test
	void itShouldRetrievePratiche() {
		praticaService.create(new Pratica("1"));
		praticaService.create(new Pratica("2"));

		List<Pratica> list = praticaService.getAll();
		assertEquals(2, list.size());
	}

	@Test
	void itShouldUpdatePratiche() {
		Pratica saved = praticaService.create(new Pratica("1"));

		saved.setNome("updated");
		saved = praticaService.update(saved);

		await().atMost(5, TimeUnit.SECONDS)
				.untilAsserted(() -> verify(receiver, times(2)).listen(any()));

		assertEquals("updated", saved.getNome());
	}

	@Test
	void itShouldDeletePratiche() {
		Pratica saved = praticaService.create(new Pratica("1"));

		praticaService.delete(saved.getId());

		await().atMost(5, TimeUnit.SECONDS)
				.untilAsserted(() -> verify(receiver, times(2)).listen(any()));

		Pratica get = praticaService.getById(saved.getId());
		assertNull(get);
	}

}
