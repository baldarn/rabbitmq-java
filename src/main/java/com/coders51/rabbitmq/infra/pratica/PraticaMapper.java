package com.coders51.rabbitmq.infra.pratica;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.springframework.jdbc.core.RowMapper;

public class PraticaMapper implements RowMapper<Pratica> {
    @Override
    public Pratica mapRow(ResultSet rs, int rowNum) throws SQLException {
        Pratica outbox = new Pratica();

        outbox.setId(UUID.fromString(rs.getString("id")));
        outbox.setNome(rs.getString("nome"));
        outbox.setUpdatedAt(rs.getTimestamp("updated_at"));
        outbox.setCreatedAt(rs.getTimestamp("created_at"));

        return outbox;
    }
}
