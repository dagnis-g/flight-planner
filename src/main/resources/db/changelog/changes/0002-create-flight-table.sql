--liquibase formatted sql

--changeset dagnis:2

CREATE TABLE flight
(
    flight_id      BIGINT PRIMARY KEY,
    airport_from   VARCHAR(255) NOT NULL,
    airport_to     VARCHAR(255) NOT NULL,
    carrier        VARCHAR(255) NOT NULL,
    departure_time timestamp    NOT NULL,
    arrival_time   timestamp    NOT NULL,
    constraint flight_from_id_fkey foreign key (airport_from) references airport (airport),
    constraint flight_to_id_fkey foreign key (airport_to) references airport (airport),
    constraint uniqueObject unique (airport_from, airport_to, carrier, departure_time, arrival_time)
);