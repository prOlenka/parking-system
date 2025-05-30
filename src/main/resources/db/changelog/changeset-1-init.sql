--liquibase formatted sql

--changeset yourname:1

CREATE TABLE parking_records (

                                 id SERIAL PRIMARY KEY,
                                 car_number VARCHAR(255) NOT NULL,
                                 car_type VARCHAR(255) NOT NULL,
                                 entry_time TIMESTAMP,
                                 exit_time TIMESTAMP
);

