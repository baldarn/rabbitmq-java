package com.coders51.rabbitmq;

import java.sql.SQLException;
import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.flywaydb.core.internal.jdbc.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Path("/publish")
public class PublishEndpoint {
    
    @Autowired
    JdbcTemplate jdbcTemplate;

    @POST
    @Consumes("application/json")
    public Response message(String msg) throws SQLException {

        jdbcTemplate.execute("INSERT INTO outbox(id, msg, created_at) VALUES (?,?,?)", 1, "ciao", new Date());

        return Response.status(200).entity("OK").build();
    }
    
}
