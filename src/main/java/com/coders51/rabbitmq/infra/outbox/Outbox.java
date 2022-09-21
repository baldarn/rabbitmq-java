package com.coders51.rabbitmq.infra.outbox;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Outbox {
    
    private String id;
    private String routingKey;
    private String type;
    private String msg;

}
