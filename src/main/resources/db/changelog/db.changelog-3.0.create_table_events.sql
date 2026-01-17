--liquibase formatted sql

--changeset veta:1
CREATE TABLE IF NOT EXISTS events(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(64) NOT NULL,
    owner_id BIGINT NOT NULL,
    occupied_places INT,
    max_place INT NOT NULL,
    date TIMESTAMP NOT NULL,
    cost DECIMAL(10, 2) DEFAULT 0.0 NOT NULL,
    duration INT NOT NULL CHECK(duration >= 30),
    location_id BIGINT NOT NULL,
    status VARCHAR(10)
);

