--liquibase formatted sql

--changeset veta:1
CREATE TABLE IF NOT EXISTS registration_events(
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    event_id INT REFERENCES events (id) ON DELETE CASCADE
);
