package com.coders51.rabbitmq.infra.pratica;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class PraticaRepository implements IPraticaRepository {

    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public List<Pratica> findAll() {
        String sql = "SELECT * from pratica";
        Stream<Pratica> p = jdbcTemplate.queryForStream(sql, new MapSqlParameterSource(), new PraticaMapper());
        return p.collect(Collectors.toList());
    }

    @Override
    public Optional<Pratica> findById(UUID id) {
        Pratica p = null;
        try {
            p = jdbcTemplate.queryForObject(
                    "SELECT * from pratica where id = :id",
                    new MapSqlParameterSource().addValue("id", id),
                    new PraticaMapper());
        } catch (DataAccessException e) {
        }
        return Optional.ofNullable(p);
    }

    @Override
    @Transactional
    public Pratica save(Pratica p) {

        jdbcTemplate.update(
                "INSERT INTO pratica(id, nome, updated_at, created_at) VALUES (:id, :nome, :updated_at, :created_at) ON CONFLICT (id) DO "
                        +
                        "UPDATE SET nome = :nome, updated_at = :updated_at",
                new MapSqlParameterSource()
                        .addValue("id", p.getId())
                        .addValue("nome", p.getNome())
                        .addValue("updated_at", p.getUpdatedAt())
                        .addValue("created_at", p.getCreatedAt()));

        sendMessage("created", p);

        return p;
    }

    @Override
    @Transactional
    public Pratica update(Pratica p) {
        jdbcTemplate.update(
                "UPDATE pratica SET nome = :nome, updated_at = :updated_at where id = :id",
                new MapSqlParameterSource()
                        .addValue("id", p.getId())
                        .addValue("nome", p.getNome())
                        .addValue("updated_at", p.getUpdatedAt()));

        sendMessage("updated", p);

        return p;
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        jdbcTemplate.update("DELETE from pratica where id = :id", new MapSqlParameterSource().addValue("id", id));
        Pratica p = new Pratica();
        p.setId(id);

        sendMessage("deleted", p);
    }

    private void sendMessage(String routingKey, Pratica pratica) {
        System.out.println(pratica.toString());
        jdbcTemplate.update("INSERT INTO outbox(id, routing_key, type, msg, created_at) VALUES (:id, :routing_key, :type, :message, :created_at)",
                new MapSqlParameterSource()
                        .addValue("id", UUID.randomUUID())
                        .addValue("routing_key", routingKey)
                        .addValue("type", Pratica.class.getName())
                        .addValue("message", pratica.toString())
                        .addValue("created_at", new Date()));
    }

}
