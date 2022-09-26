package com.coders51.rabbitmq.infra.pratica;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

@Repository
public interface IPraticaRepository {

    List<Pratica> findAll();

    Optional<Pratica> findById(UUID id);

    Pratica save(Pratica p);

    Pratica update(Pratica p);

    void deleteById(UUID id);

}
