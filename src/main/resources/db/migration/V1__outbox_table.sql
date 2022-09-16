CREATE TABLE outbox (
    id UUID PRIMARY KEY,
    msg VARCHAR NOT NULL,
    published boolean DEFAULT false,
    created_at TIMESTAMP NOT NULL
);
