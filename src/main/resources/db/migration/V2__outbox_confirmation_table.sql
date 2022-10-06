CREATE TABLE outbox_confirmation (
    id UUID PRIMARY KEY,
    confirmed boolean,
    confirmation_cause VARCHAR,
    created_at TIMESTAMP NOT NULL
);
