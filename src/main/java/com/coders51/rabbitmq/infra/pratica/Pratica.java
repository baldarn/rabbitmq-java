package com.coders51.rabbitmq.infra.pratica;

import java.sql.Timestamp;
import java.util.UUID;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pratica {
    @Id
    private UUID id;
    private String nome;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
