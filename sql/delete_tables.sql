-- Script to delete tables (item 2b)

CREATE OR REPLACE PROCEDURE delete_tables()
    LANGUAGE plpgsql
AS
$$
BEGIN
    DROP TABLE IF EXISTS invalid_gps_data CASCADE;
    DROP TABLE IF EXISTS unprocessed_gps_data CASCADE;
    DROP TABLE IF EXISTS alarms CASCADE;
    DROP TABLE IF EXISTS gps_data CASCADE;
    DROP TABLE IF EXISTS green_zones CASCADE;
    DROP TABLE IF EXISTS drivers CASCADE;
    DROP TABLE if exists vehicles CASCADE;
    DROP TABLE IF EXISTS institutional_clients CASCADE;
    DROP TABLE IF EXISTS private_clients CASCADE;
    DROP TABLE IF EXISTS clients CASCADE;
    DROP TABLE IF EXISTS gps_devices CASCADE;
    DROP TABLE IF EXISTS gps_device_states CASCADE;
END;
$$;

CALL delete_tables();