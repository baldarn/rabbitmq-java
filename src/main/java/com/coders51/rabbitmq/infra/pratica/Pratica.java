package com.coders51.rabbitmq.infra.pratica;

import java.sql.Timestamp;
import java.util.UUID;

import org.springframework.data.annotation.Id;

import com.coders51.rabbitmq.infra.outbox.Outoboxable;

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
}
