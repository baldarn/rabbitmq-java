package com.coders51.rabbitmq.infra.outbox;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class OutboxMapper implements RowMapper<Outbox> {
    @Override
    public Outbox mapRow(ResultSet rs, int rowNum) throws SQLException {
        Outbox outbox = new Outbox();

        outbox.setId(rs.getString("ID"));
        outbox.setType(rs.getString("TYPE"));
        outbox.setMsg(rs.getString("MSG"));
        outbox.setRoutingKey(rs.getString("routing_key"));

        return outbox;
    }
}
