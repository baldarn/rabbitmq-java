package com.coders51.rabbitmq.infra.pratica;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.UUID;

import org.springframework.data.annotation.Id;

import com.coders51.rabbitmq.infra.outbox.Outoboxable;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pratica implements Outoboxable {
    @Id
    private UUID id;
    private String nome;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Pratica(String nome) {
        this.nome = nome;
    }

    @Override
    public String getRoutingKey() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String toString() {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonMsg = null;

        try {
            jsonMsg = objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonMsg;
    }

    public static Pratica deserialize(byte[] bytes) {
        Pratica p = null;
		try {
			p = (new ObjectMapper()).readValue(bytes, Pratica.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return p;
    }
}
