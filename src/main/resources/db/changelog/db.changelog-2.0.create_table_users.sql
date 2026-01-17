--liquibase formatted sql

--changeset veta:1

CREATE TABLE IF NOT EXISTS users(
    id BIGSERIAL PRIMARY KEY,
    login VARCHAR(64) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    age INT NOT NULL CHECK(age >= 18),
    role VARCHAR(10)
);
