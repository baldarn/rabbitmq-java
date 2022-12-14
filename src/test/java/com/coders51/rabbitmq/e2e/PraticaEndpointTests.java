package com.coders51.rabbitmq.e2e;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.web.client.RestTemplate;

import com.coders51.rabbitmq.BaseTest;
import com.coders51.rabbitmq.consumer.Receiver;
import com.coders51.rabbitmq.infra.pratica.Pratica;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
class PraticaEndpointTests extends BaseTest {

	private static String url = "http://localhost:8080/pratiche";

	@Autowired
	private TestRestTemplate restTemplate;

	@SpyBean
	private Receiver receiver;

	@MockBean
	private RestTemplate mockRestTemplate;

	@Test
	void itShouldCreateAPratica() throws Exception {
		Pratica pratica = createPratica("the name");
		assertNotNull(pratica);

		await().atMost(3, TimeUnit.SECONDS)
				.untilAsserted(() -> verify(receiver, times(1))
						.listen(argThat(m -> Pratica.deserialize(m.getBody()).getId().equals(pratica.getId()))));

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

		Pratica updated = this.restTemplate.postForObject(url + "/" + pratica.getId().toString(), pratica, Pratica.class);

		assertNotNull(updated);
		assertEquals("new name", updated.getNome());

		await().atMost(3, TimeUnit.SECONDS)
				.untilAsserted(() -> verify(receiver, times(2))
						.listen(argThat(m -> Pratica.deserialize(m.getBody()).getId().equals(pratica.getId()))));
	}

	@Test
	void itShouldDeletePratiche() {
		Pratica pratica = createPratica("the name");

		when(mockRestTemplate.getForObject("http://localhost:8081/auth.json", String.class))
			.thenReturn("true");
		
		this.restTemplate.delete(url + "/" + pratica.getId().toString());

		Pratica response = this.restTemplate.getForObject(url + "/" + pratica.getId().toString(), Pratica.class);

		assertNull(response);

		await().atMost(3, TimeUnit.SECONDS)
				.untilAsserted(() -> verify(receiver, times(2))
						.listen(argThat(m -> Pratica.deserialize(m.getBody()).getId().equals(pratica.getId()))));
	}

	@Test
	void itShouldntDeletePratiche() {
		Pratica pratica = createPratica("the name");

		when(mockRestTemplate.getForObject("http://localhost:8081/auth.json", String.class))
			.thenReturn("false");
		
		this.restTemplate.delete(url + "/" + pratica.getId().toString());

		Pratica response = this.restTemplate.getForObject(url + "/" + pratica.getId().toString(), Pratica.class);

		assertNotNull(response);
	}

	private Pratica createPratica(String nome) {
		Pratica toSend = new Pratica(nome);

		return this.restTemplate.postForObject(url, toSend, Pratica.class);
	}

}
