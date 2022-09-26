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
import org.springframework.boot.test.web.client.TestRestTemplate;

import com.coders51.rabbitmq.consumer.Receiver;
import com.coders51.rabbitmq.infra.pratica.Pratica;

class PraticaEndpointTests extends BaseTest {

	private static String url = "http://localhost:8080/pratiche";
	@Autowired
	private TestRestTemplate restTemplate;

	@SpyBean
	private Receiver receiver;

	@Test
	void itShouldCreateAPratica() throws Exception {
		Pratica response = createPratica("the name");
		assertNotNull(response);

		await().atMost(5, TimeUnit.SECONDS)
				.untilAsserted(() -> verify(receiver, times(1)).listen(any()));

	}

	@Test
	void itShouldGetAllPratiche() throws Exception {
		createPratica("the name");
		createPratica("the name");
		createPratica("the name");

		List<Pratica> list = this.restTemplate.getForObject(url, List.class);

		assertEquals(3, list.size());
	}

	@Test
	void itShouldGetAPratica() throws Exception {
		Pratica pratica = createPratica("the name");

		Pratica response = this.restTemplate.getForObject(url + "/" + pratica.getId().toString(), Pratica.class);

		assertEquals(pratica.getId(), response.getId());
	}

	@Test
	void itShouldUpdatePratiche() {
		Pratica pratica = createPratica("the name");
		pratica.setNome("new name");

		Pratica updated = this.restTemplate.postForObject(url, pratica, Pratica.class);

		assertNotNull(updated);
		assertEquals("new name", updated.getNome());

		await().atMost(5, TimeUnit.SECONDS)
				.untilAsserted(() -> verify(receiver, times(2)).listen(any()));
	}

	@Test
	void itShouldDeletePratiche() {
		Pratica pratica = createPratica("the name");

		this.restTemplate.delete(url + "/" + pratica.getId().toString());

		Pratica response = this.restTemplate.getForObject(url + "/" + pratica.getId().toString(), Pratica.class);

		assertNull(response);

		await().atMost(5, TimeUnit.SECONDS)
				.untilAsserted(() -> verify(receiver, times(2)).listen(any()));
	}

	private Pratica createPratica(String nome) {
		Pratica toSend = new Pratica(nome);

		return this.restTemplate.postForObject(url, toSend, Pratica.class);
	}

}
