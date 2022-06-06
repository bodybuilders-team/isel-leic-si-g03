-- Script to insert initial data into tables (item 2c)

CREATE OR REPLACE PROCEDURE insert_data()
    LANGUAGE plpgsql
AS
$$
BEGIN
    INSERT INTO clients(name, dtype, phone_number, nif, address, active)
    VALUES ('André Páscoa', 'PrivateClient', '962249051', '123456789', 'Rua Padre Santos Vieira nº1, Lisboa, Portugal',
            true),
           ('BE Fit', 'InstitutionalClient', '1234', '299114480', 'Rua Padre Santos Vieira nº1, Lisboa, Portugal',
            true),
           ('André Jesus', 'InstitutionalClient', '1234', '243496199', 'Rua Padre Santos Vieira nº1, Lisboa, Portugal',
            true),
           ('Nyckollas Brandão', 'InstitutionalClient', '1234', '381099212',
            'Rua Padre Santos Vieira nº1, Lisboa, Portugal',
            true),
           ('Carlos', 'PrivateClient', '1234', '607425393', 'Rua Padre Santos Vieira nº1, Lisboa, Portugal',
            true),
           ('Cristiano Ronaldo', 'PrivateClient', '1234', '493391882', 'Rua Padre Santos Vieira nº1, Lisboa, Portugal',
            true),
           ('Bruno', 'PrivateClient', '962249051', '509869871', 'Rua Padre Santos Vieira nº1, Lisboa, Portugal', false);


    INSERT INTO private_clients (id, citizen_card_number)
    VALUES (1, '123456789123001'),
           (5, '638188683971442'),
           (6, '724920477174198'),
           (7, '725632908878066');


    INSERT INTO institutional_clients (id, contact_name)
    VALUES (2, '123456789123'),
           (3, '21434fgdfgsf'),
           (4, 'afdsdhkgdfad');


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


    INSERT INTO drivers (vehicle_id, name, phone_number)
    VALUES (1, 'Sonic', '962249051'),
           (2, 'Tails', '333444555'),
           (3, 'Eggman', '542341141');


    INSERT INTO green_zones (vehicle_id, lat, lon, radius)
    VALUES (1, 38.6556752, 9.0100229, 1000),
           (1, 38.511, 9.0100229, 1000),
           (2, 28.6556752, 9.0100229, 10),
           (3, 18.6556752, 9.0100229, 15);


    INSERT INTO unprocessed_gps_data (device_id, timestamp, lat, lon, version)
    VALUES (1, '2022-04-12 04:05:06 UTC+1', 18, 9, 0),
           (1, '2020-04-12 04:05:06 PST', 18, 9, 0),
           (3, '2020-04-12 04:05:06 UTC+0', 18, 9, 0),
           (7, '2022-05-06 04:05:06 PST', 18, 9, 0);


    INSERT INTO gps_data (device_id, timestamp, lat, lon)
    VALUES (1, '2022-04-12 04:05:06 UTC+3', 18, 10),
           (2, '2019-04-12 04:05:06 UTC+3', 0, 10),
           (2, '2020-04-12 04:05:06 UTC+3', 18, 0),
           (3, '2022-09-05 04:05:06 UTC+3', 18, 95),
           (3, '2018-04-12 04:05:06 UTC+3', 166, 25);

END;
$$;


CALL insert_data();
