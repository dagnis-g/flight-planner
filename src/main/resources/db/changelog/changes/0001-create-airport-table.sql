--liquibase formatted sql

--changeset dagnis:1

CREATE TABLE airport
(
    airport VARCHAR(255) PRIMARY KEY,
    country VARCHAR(255) NOT NULL,
    city    VARCHAR(255) NOT NULL
);

