-- Script to insert initial data into tables (item 2c)

CREATE OR REPLACE PROCEDURE insert_data()
    LANGUAGE plpgsql
AS
$$
BEGIN
INSERT INTO clients(name, phone_number, nif, address, active)
VALUES ('André Páscoa', '962249051', '123456789', 'Rua Padre Santos Vieira nº1, Lisboa, Portugal', true),
       ('BE Fit', '1234', '123456789', 'Rua Padre Santos Vieira nº1, Lisboa, Portugal', true);


INSERT INTO private_clients (id, citizen_card_number)
VALUES (1, '123456789123');


INSERT INTO institutional_clients (id, contact_name)
VALUES (2, '123456789123');


INSERT INTO gps_device_states (status)
VALUES ('Active'),
       ('AlarmPause'),
       ('Inactive');


INSERT INTO gps_devices (device_status)
VALUES (1),
       (1),
       (1);


INSERT INTO vehicles (gps_device_id, client_id, license_plate, num_alarms)
VALUES (1, 1, '99-DH-66', 0),
       (2, 1, '88-66-33', 0),
       (3, 2, '33-44-55', 0);


INSERT INTO drivers
VALUES (1, 'Sonic', '962249051'),
       (2, 'Tails', '333444555'),
       (3, 'Eggman', '542341141');


INSERT INTO green_zones (vehicle_id, center_location, radius)
VALUES (1, point(38.6556752, -9.0100229), 5),
       (1, point(39.511, -9.0100229), 5),
       (2, point(28.6556752, -9.0100229), 10),
       (3, point(18.6556752, -9.0100229), 15);


INSERT INTO unprocessed_gps_data (device_id, timestamp, location)
VALUES (1, '2022-04-12 04:05:06 UTC+1', point(18, 9)),
       (1, '2020-04-12 04:05:06 PST', point(18, 9)),
       (3, '2020-04-12 04:05:06 UTC+0', point(18, 9));


INSERT INTO gps_data (device_id, timestamp, location)
VALUES (1, '2022-04-12 04:05:06 UTC+3', point(18, 10));

END;
$$;

CALL insert_data();