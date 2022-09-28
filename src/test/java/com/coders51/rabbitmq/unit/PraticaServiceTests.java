package com.coders51.rabbitmq.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import com.coders51.rabbitmq.BaseTest;
import com.coders51.rabbitmq.infra.pratica.Pratica;
import com.coders51.rabbitmq.infra.pratica.PraticaRepository;
import com.coders51.rabbitmq.infra.pratica.PraticaService;

@SpringBootTest
class PraticaServiceTests extends BaseTest {

	@SpyBean
	PraticaService praticaService;

	@MockBean
	PraticaRepository repo;

	@Test
	void itShouldCreateAPratica() {
		praticaService.create(new Pratica("1"));
		verify(repo, times(1)).save(any());
	}

	@Test
	void itShouldRetrievePratiche() {
		when(repo.findAll()).thenReturn(Arrays.asList(new Pratica(), new Pratica()));

		List<Pratica> list = praticaService.getAll();
		verify(repo, times(1)).findAll();

		assertEquals(2, list.size());
	}

	@Test
	void itShouldRetrieveEmptyArrayWithoutPratiche() {
		when(repo.findAll()).thenReturn(new ArrayList<>());

		List<Pratica> list = praticaService.getAll();

		assertEquals(0, list.size());
	}

	@Test
	void itShouldUpdatePratiche() {
		Pratica pratica = new Pratica("nome");
		UUID id = UUID.randomUUID();
		pratica.setId(id);

		when(repo.findById(id)).thenReturn(Optional.ofNullable(pratica));

		Pratica toUpdate = new Pratica();
		toUpdate.setId(id);
		toUpdate.setNome("other name");

		praticaService.update(toUpdate);

		verify(repo, times(1)).findById(id);
		verify(repo, times(1)).update(
				argThat(p -> p.getNome().equals("other name")));
	}

	@Test
	void itShouldDeletePratiche() {
		UUID id = UUID.randomUUID();
		praticaService.delete(id);

		verify(repo, times(1)).deleteById(id);
	}

}
