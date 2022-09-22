package com.coders51.rabbitmq.infra.pratica;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PraticaRepository implements IPraticaRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public List<Pratica> findAll() {
        String sql = "SELECT * from pratica";
        Stream<Pratica> p = jdbcTemplate.queryForStream(sql, new PraticaMapper());
        return p.collect(Collectors.toList());
    }

    @Override
    public Optional<Pratica> findById(UUID id) {
        String sql = "SELECT * from pratica where id = ?";
        Pratica p = jdbcTemplate.queryForObject(sql, new PraticaMapper(), id);
        return Optional.of(p);
    }

    @Override
    public Pratica save(Pratica p) {
        jdbcTemplate.update("INSERT INTO pratica(id, nome, updated_at, created_at) VALUES (?, ?, ?, ?)",
                UUID.randomUUID(),
                p.getNome(),
                new Date(),
                new Date());

        sendMessage("created", p.getId().toString());

        return p;
    }

    @Override
    public void deleteById(UUID id) {
        jdbcTemplate.update("DELETE from pratica where id = ?", id);
        sendMessage("deleted", id.toString());
    }

    private void sendMessage(String routingKey, String message) {
        jdbcTemplate.update("INSERT INTO outbox(id, routing_key, type, msg, created_at) VALUES (?, ?, ?, ?, ?)",
                UUID.randomUUID(),
                routingKey,
                Pratica.class,
                message,
                new Date());
    }

}
