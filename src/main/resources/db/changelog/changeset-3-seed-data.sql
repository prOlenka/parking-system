--liquibase formatted sql

--changeset yourname:3
INSERT INTO parking_records (car_number, car_type, entry_time, exit_time)
VALUES ('A123BC', 'Sedan', '2025-05-01 08:00:00', '2025-05-01 10:00:00');
