CREATE TABLE outbox (
    id UUID PRIMARY KEY,
    routing_key VARCHAR NOT NULL,
    type VARCHAR NOT NULL,
    msg VARCHAR NOT NULL,
    published boolean DEFAULT false,
    confirmed boolean,
    confirmation_cause VARCHAR,
    return_cause VARCHAR,
    attempts int default 1,
    created_at TIMESTAMP NOT NULL
);
