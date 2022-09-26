CREATE TABLE outbox (
    id UUID PRIMARY KEY,
    routing_key VARCHAR NOT NULL,
    type VARCHAR NOT NULL,
    msg VARCHAR NOT NULL,
    published boolean DEFAULT false,
    return_cause VARCHAR,
    attempts int default 1,
    created_at TIMESTAMP NOT NULL
);
