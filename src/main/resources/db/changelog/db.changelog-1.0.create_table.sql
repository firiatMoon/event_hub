--liquibase formatted sql

--changeset veta:1
CREATE TABLE IF NOT EXISTS locations(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    address VARCHAR(100) NOT NULL,
    capacity INT NOT NULL CHECK(capacity >= 10),
    description VARCHAR(1000)
);

CREATE TABLE IF NOT EXISTS users(
    id BIGSERIAL PRIMARY KEY,
    login VARCHAR(64) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    age INT NOT NULL CHECK(age >= 18),
    role VARCHAR(10)
);