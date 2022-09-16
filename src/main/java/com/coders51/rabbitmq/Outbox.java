package com.coders51.rabbitmq;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Outbox {
    
    private Long id;
    private String msg;
    private Date createdAt;

}
