package com.coders51.rabbitmq.infra.outbox;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface Outoboxable {
    
    @JsonIgnore
    String getRoutingKey();
}
