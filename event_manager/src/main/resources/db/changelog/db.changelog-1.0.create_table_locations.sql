--liquibase formatted sql

--changeset veta:1
CREATE TABLE IF NOT EXISTS locations(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    address VARCHAR(100) NOT NULL,
    capacity INT NOT NULL CHECK(capacity >= 10),
    description VARCHAR(1000)
);

