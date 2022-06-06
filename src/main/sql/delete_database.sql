-- Script to delete tables (item 2b), procedures, functions, views, and triggers

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

CREATE OR REPLACE PROCEDURE delete_functions()
    LANGUAGE plpgsql
AS
$$
BEGIN
    DROP PROCEDURE IF EXISTS clear_invalid_gps_data();
    DROP FUNCTION IF EXISTS create_vehicle(v_gps_device_id integer, v_client_id integer, v_license_plate varchar,
                                           v_num_alarms integer, gz_center_location_lat double precision,
                                           gz_center_location_lon double precision, gz_radius double precision);
    DROP FUNCTION IF EXISTS delete_client_trigger();
    DROP FUNCTION IF EXISTS generate_alarm_trigger();
    DROP FUNCTION IF EXISTS get_alarms_count(year_a integer, license_plate_a varchar);
    DROP FUNCTION IF EXISTS increment_vehicle_count_trigger();
    DROP FUNCTION IF EXISTS insert_into_list_alarms_trigger();
    DROP PROCEDURE IF EXISTS insert_private_client(client_citizen_card_number varchar, client_name varchar,
                                                   client_phone_number varchar, client_nif char, client_address varchar,
                                                   client_referee integer);
    DROP FUNCTION IF EXISTS location_inside_green_zone(green_zone_center point, green_zone_radius double precision,
                                                       gps_location point);
    DROP PROCEDURE IF EXISTS process_gps_data();
    DROP PROCEDURE IF EXISTS remove_private_client(client_id integer);
    DROP PROCEDURE IF EXISTS update_private_client(client_id integer, new_citizen_card_number varchar, new_nif char,
                                                   new_name varchar, new_address varchar, new_client_referee integer);
END;
$$;


CALL delete_tables();
CALL delete_functions();