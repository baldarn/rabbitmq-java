package com.coders51.rabbitmq.infra.pratica;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.springframework.jdbc.core.RowMapper;

public class PraticaMapper implements RowMapper<Pratica> {
    @Override
    public Pratica mapRow(ResultSet rs, int rowNum) throws SQLException {
        Pratica pratica = new Pratica();

        pratica.setId(UUID.fromString(rs.getString("id")));
        pratica.setNome(rs.getString("nome"));
        pratica.setUpdatedAt(rs.getTimestamp("updated_at"));
        pratica.setCreatedAt(rs.getTimestamp("created_at"));

        return pratica;
    }
}
