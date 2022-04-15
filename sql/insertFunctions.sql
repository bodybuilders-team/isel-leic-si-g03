-- This script contains the functions, procedures, views and triggers from item 2e) to item 2m)

DROP FUNCTION IF EXISTS get_alarms_count CASCADE;
DROP PROCEDURE IF EXISTS process_gps_data CASCADE;
DROP FUNCTION IF EXISTS valid_green_zone CASCADE;
DROP FUNCTION IF EXISTS alarm_gps_data_trigger CASCADE;
DROP TRIGGER IF EXISTS alarm_gps_data ON gps_data CASCADE;
DROP PROCEDURE IF EXISTS create_vehicle CASCADE;
DROP VIEW IF EXISTS list_alarms CASCADE;
DROP FUNCTION IF EXISTS insert_into_list_alarms_trigger CASCADE;
DROP TRIGGER IF EXISTS insert_into_list_alarms ON list_alarms CASCADE;
DROP PROCEDURE IF EXISTS clear_invalid_gps_data CASCADE;
DROP FUNCTION IF EXISTS delete_client_trigger CASCADE;
DROP TRIGGER IF EXISTS delete_client ON clients CASCADE;
DROP FUNCTION IF EXISTS create_alarm_trigger CASCADE;
DROP TRIGGER IF EXISTS create_alarm ON alarms CASCADE;


-- 2e)
CREATE OR REPLACE FUNCTION get_alarms_count(
    year_a INTEGER,
    license_plate_a TEXT DEFAULT NULL
)
    RETURNS INTEGER
    LANGUAGE plpgsql AS
$$
BEGIN
    IF (license_plate_a IS NOT NULL) THEN
        RETURN (
            SELECT COUNT(*)
            FROM alarms
                     JOIN(
                SELECT gps_data.id
                FROM gps_data
                         JOIN (
                    SELECT gps_device_id AS id
                    FROM vehicles
                    WHERE vehicles.license_plate = license_plate_a
                ) AS gps_device ON gps_data.device_id = gps_device.id
                WHERE extract(YEAR from gps_data.timestamp) = year_a
            ) AS gps_data_j ON alarms.gps_data_id = gps_data_j.id
        );
    END IF;

    RETURN (
        SELECT COUNT(*)
        FROM alarms
                 JOIN(
            SELECT gps_data.id
            FROM gps_data
            WHERE extract(YEAR from gps_data.timestamp) = year_a
        ) AS gps_data_j ON alarms.gps_data_id = gps_data_j.id
    );
END;
$$;
-- SELECT get_alarms_count(2022);


-- 2f)
CREATE OR REPLACE PROCEDURE process_gps_data()
    LANGUAGE plpgsql
AS
$$
DECLARE
    temp_row RECORD;
BEGIN
    FOR temp_row IN SELECT * FROM unprocessed_gps_data
        LOOP
            BEGIN
                INSERT INTO gps_data(device_id, timestamp, location)
                VALUES (temp_row.device_id, temp_row.timestamp, temp_row.location);
            EXCEPTION
                WHEN OTHERS THEN
                    INSERT INTO invalid_gps_data(device_id, timestamp, location)
                    VALUES (temp_row.device_id, temp_row.timestamp, temp_row.location);
            END;

            DELETE
            FROM unprocessed_gps_data
            WHERE id = temp_row.id;

        END LOOP;
END;
$$;


-- 2g)
CREATE OR REPLACE FUNCTION valid_green_zone(
    green_zone_center POINT,
    green_zone_radius DOUBLE PRECISION,
    gps_location POINT
)
    RETURNS BOOLEAN
    LANGUAGE plpgsql
AS
$$
BEGIN
    RETURN (ST_DISTANCE(green_zone_center, gps_location) <= green_zone_radius); -- TODO isto tá sus Pascoas
END;
$$;

CREATE OR REPLACE FUNCTION alarm_gps_data_trigger()
    RETURNS TRIGGER
    LANGUAGE plpgsql
AS
$alarm_gps_data_trigger$
DECLARE
    green_zone       RECORD;
    device_status_id INTEGER;
    device_status    VARCHAR;
BEGIN
    FOR green_zone IN SELECT green_zones
                      FROM green_zones
                               JOIN(
                          SELECT id
                          FROM vehicles
                          WHERE gps_device_id = NEW.device_id
                      ) AS vehicles ON vehicles.id = green_zones.vehicle_id
        LOOP
            SELECT device_status
            FROM gps_devices
            WHERE id = NEW.device_id
            INTO device_status_id;

            SELECT status
            FROM gps_device_states
            WHERE id = device_status_id
            INTO device_status;

            IF NOT valid_green_zone(green_zone.center, green_zone.radius, NEW.location) AND
               device_status != 'AlarmPause'
            THEN
                INSERT INTO alarms(gps_data_id)
                VALUES (NEW.id);
                RETURN NEW;
            END IF;
        END LOOP;
    RETURN NEW;
END;
$alarm_gps_data_trigger$;

CREATE TRIGGER alarm_gps_data
    AFTER INSERT
    ON gps_data
    FOR EACH ROW
EXECUTE FUNCTION alarm_gps_data_trigger();


-- 2h)
CREATE OR REPLACE PROCEDURE create_vehicle(
    v_gps_device_id INTEGER,
    v_client_id INTEGER,
    v_license_plate TEXT,
    v_num_alarms INTEGER,
    gz_center_location POINT DEFAULT NULL,
    gz_radius DOUBLE PRECISION DEFAULT NULL
)
    LANGUAGE plpgsql
AS
$$
DECLARE
    v_id                INTEGER;
    v_client_cars_count INTEGER;
    green_zone_id       INTEGER;
BEGIN
    -- Get number of client cars
    SELECT COUNT(*)
    FROM vehicles
    WHERE client_id = v_client_id
    INTO v_client_cars_count;

    -- If the client is institutional or has less than 3 cars
    IF (SELECT EXISTS(SELECT 1 FROM institutional_clients WHERE id = v_client_id) OR v_client_cars_count < 3) THEN
        INSERT INTO vehicles(gps_device_id, client_id, license_plate, num_alarms)
        VALUES (v_gps_device_id, v_client_id, v_license_plate, v_num_alarms)
        RETURNING id INTO v_id;

        IF (gz_center_location IS NOT NULL AND gz_radius IS NOT NULL) THEN
            INSERT INTO green_zones(vehicle_id, center_location, radius)
            VALUES (v_id, gz_center_location, gz_radius)
            RETURNING id INTO green_zone_id;
        END IF;
    END IF;
END;
$$;


-- 2i)
CREATE OR REPLACE VIEW list_alarms AS
SELECT gps_data_id, license_plate, driver_name, location, timestamp
FROM alarms
         JOIN (
    SELECT license_plate, driver_name, location, timestamp, gps_data.id AS id
    FROM gps_data
             JOIN (
        SELECT gps_device_id, license_plate, drivers.name AS driver_name
        FROM vehicles
                 JOIN drivers ON vehicles.id = drivers.vehicle_id
    ) AS vehicles_and_drivers ON gps_data.device_id = vehicles_and_drivers.gps_device_id
) AS alarm_data ON alarms.gps_data_id = alarm_data.id;


-- 2j)
CREATE OR REPLACE FUNCTION insert_into_list_alarms_trigger()
    RETURNS TRIGGER
    LANGUAGE plpgsql
AS
$insert_into_list_alarms_trigger$
BEGIN

    INSERT INTO alarms(gps_data_id)
    VALUES (NEW.gps_data_id);
    -- TODO NÃO SEI FAZER, N PERCEBO O ENUNCIADO

    RETURN NEW;
END;
$insert_into_list_alarms_trigger$;

CREATE TRIGGER insert_into_list_alarms_trigger
    INSTEAD OF INSERT
    ON list_alarms
    FOR EACH ROW
EXECUTE FUNCTION insert_into_list_alarms_trigger();


-- 2k)
CREATE OR REPLACE PROCEDURE clear_invalid_gps_data()
    LANGUAGE plpgsql
AS
$$
BEGIN
    DELETE
    FROM invalid_gps_data
    WHERE extract(DAY from invalid_gps_data.timestamp) > 15;
END;
$$;


-- 2l)
CREATE OR REPLACE FUNCTION delete_client_trigger()
    RETURNS TRIGGER
    LANGUAGE plpgsql
AS
$delete_client_trigger$
BEGIN

    -- Insert deleted user
    INSERT INTO clients
    VALUES (OLD.*);

    -- Update active state to false
    UPDATE clients
    SET active = false
    WHERE id = OLD.id;

    RETURN NEW;
END;
$delete_client_trigger$;

CREATE TRIGGER delete_client
    AFTER DELETE
    ON clients
    FOR EACH ROW
EXECUTE FUNCTION delete_client_trigger();

-- 2m)
CREATE OR REPLACE FUNCTION create_alarm_trigger()
    RETURNS TRIGGER
    LANGUAGE plpgsql
AS
$create_alarm_trigger$
DECLARE
    vehicle_id INTEGER;
BEGIN

    -- Get alarm vehicle id
    SELECT vehicles.id
    FROM gps_data
             JOIN vehicles ON gps_data.device_id = vehicles.gps_device_id
    WHERE gps_data.id = NEW.gps_data_id
    INTO vehicle_id;

    -- Update vehicle alarm count
    UPDATE vehicles
    SET num_alarms = num_alarms + 1
    WHERE id = vehicle_id;

    RETURN NEW;
END;
$create_alarm_trigger$;

CREATE TRIGGER create_alarm
    AFTER INSERT
    ON alarms
    FOR EACH ROW
EXECUTE FUNCTION create_alarm_trigger();
