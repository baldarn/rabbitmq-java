package com.coders51.rabbitmq.dto;

import com.coders51.rabbitmq.infra.outbox.Outoboxable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Foo implements Outoboxable {

    private String foo;
    private int fooNo;

    @Override
    public String getRoutingKey() {
        return this.getClass().getSimpleName();
    }

}
