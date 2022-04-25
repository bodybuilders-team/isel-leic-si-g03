-- Script to clear all data from tables

CREATE OR REPLACE PROCEDURE clear_data()
    LANGUAGE plpgsql
AS
$$
BEGIN
    TRUNCATE alarms CASCADE;
    TRUNCATE institutional_clients CASCADE;
    TRUNCATE private_clients CASCADE;
    TRUNCATE drivers CASCADE;

    TRUNCATE invalid_gps_data CASCADE;
    ALTER SEQUENCE invalid_gps_data_id_seq RESTART WITH 1;

    TRUNCATE unprocessed_gps_data CASCADE;
    ALTER SEQUENCE unprocessed_gps_data_id_seq RESTART WITH 1;

    TRUNCATE gps_data CASCADE;
    ALTER SEQUENCE gps_data_id_seq RESTART WITH 1;

    TRUNCATE green_zones CASCADE;
    ALTER SEQUENCE green_zones_id_seq RESTART WITH 1;

    TRUNCATE vehicles CASCADE;
    ALTER SEQUENCE vehicles_id_seq RESTART WITH 1;

    TRUNCATE clients CASCADE;
    ALTER SEQUENCE clients_id_seq RESTART WITH 1;

    TRUNCATE gps_devices CASCADE;
    ALTER SEQUENCE gps_devices_id_seq RESTART WITH 1;

    TRUNCATE gps_device_states CASCADE;
    ALTER SEQUENCE gps_device_states_id_seq RESTART WITH 1;

END;
$$;

CALL clear_data();