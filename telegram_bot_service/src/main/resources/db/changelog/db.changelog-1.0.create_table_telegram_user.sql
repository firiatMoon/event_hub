--liquibase formatted sql

--changeset veta:1
CREATE TABLE IF NOT EXISTS telegram_user(
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT,
    chat_id BIGINT UNIQUE NOT NULL,
    created_at TIMESTAMP NOT NULL
);



