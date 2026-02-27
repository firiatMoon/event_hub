--liquibase formatted sql

--changeset veta:1
CREATE TABLE IF NOT EXISTS notifications(
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT,
    created_at TIMESTAMP NOT NULL,
    is_read BOOLEAN DEFAULT false,
    event_id BIGINT NOT NULL,
    owner_id BIGINT,
    old_name VARCHAR(100),
    new_name VARCHAR(100),
    old_max_place INT,
    new_max_place INT,
    old_date TIMESTAMP,
    new_date TIMESTAMP,
    old_cost DECIMAL(10, 2),
    new_cost DECIMAL(10, 2),
    old_duration INT,
    new_duration INT,
    old_location_id BIGINT,
    new_location_id BIGINT,
    old_status VARCHAR(10),
    new_status VARCHAR(10)
);



