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

CREATE TABLE IF NOT EXISTS events(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(64) NOT NULL,
    owner_id BIGINT NOT NULL,
    occupied_places INT NOT NULL,
    max_place INT NOT NULL,
    date TIMESTAMP NOT NULL,
    cost DECIMAL(10, 2) DEFAULT 0.0 NOT NULL,
    duration INT NOT NULL CHECK(duration >= 30),
    location_id BIGINT NOT NULL,
    status VARCHAR(10)
);

-- CREATE TABLE IF NOT EXISTS registration_events(
--     id BIGSERIAL PRIMARY KEY,
--     user_id BIGINT NOT NULL,
--
--     name VARCHAR(64) NOT NULL,
--     owner_id BIGINT NOT NULL,
--     occupied_places INT NOT NULL,
--     max_place INT NOT NULL,
--     date TIMESTAMP NOT NULL,
--     cost DECIMAL(10, 2) DEFAULT 0.0 NOT NULL,
--     duration INT NOT NULL CHECK(duration >= 30),
--     location_id BIGINT NOT NULL,
--     status VARCHAR(10)
--     );
