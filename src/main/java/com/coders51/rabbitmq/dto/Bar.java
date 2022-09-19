package com.coders51.rabbitmq.dto;

import com.coders51.rabbitmq.infra.outbox.Outoboxable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bar implements Outoboxable {

    private String bar;

    @Override
    public String getRoutingKey() {
        return this.getClass().getSimpleName();
    }
    
}
