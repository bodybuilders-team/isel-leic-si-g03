DROP PROCEDURE IF EXISTS insert_private_client;
DROP FUNCTION IF EXISTS get_num_total_alarms_by_year;

CREATE PROCEDURE insert_private_client(
    citizen_card_number TEXT, name TEXT, phone_number TEXT,
    nif TEXT, address TEXT, client_referee INTEGER DEFAULT NULL
) as
$$
DECLARE
    new_id INTEGER;
BEGIN
    INSERT INTO clients(name, phone_number, nif, address, active)
    VALUES (name, phone_number, nif, address, true)
    RETURNING id INTO new_id;

    INSERT INTO private_clients (id, citizen_card_number)
    VALUES (new_id, citizen_card_number);
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_num_total_alarms_by_year(
    year_a INTEGER, license_plate_a TEXT DEFAULT NULL) RETURNS INTEGER as
$$
BEGIN

    IF (license_plate_a IS NOT NULL) THEN
        RETURN (
            SELECT COUNT(*)
            FROM alarms
                     JOIN(
                SELECT gps_data.id
                FROM gps_data
                         JOIN (SELECT gps_device_id as id
                               FROM vehicles
                               WHERE vehicles.license_plate = license_plate_a) as gps_device
                              ON gps_data.device_id = gps_device.id
                WHERE extract(YEAR from gps_data.timestamp) = year_a
            ) AS gps_data_j
                         ON alarms.gps_data_id = gps_data_j.id
        );
    END IF;

    RETURN (
        SELECT COUNT(*)
        FROM alarms
                 RIGHT JOIN(
            SELECT gps_data.id
            FROM gps_data

            WHERE extract(YEAR from gps_data.timestamp) = year_a
        ) AS gps_data_j
                           ON alarms.gps_data_id = gps_data_j.id
    );

END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE PROCEDURE PROCESS_GPS_DATA()
AS
$$
DECLARE
    temp_row RECORD;
BEGIN
    FOR temp_row IN SELECT * FROM unprocessed_gps_data
        LOOP

            DECLARE
            BEGIN
                INSERT INTO gps_data(device_id, timestamp, location)
                VALUES (temp_row.device_id, temp_row.timestamp, temp_row.location);
            EXCEPTION
                WHEN OTHERS THEN
                    INSERT INTO invalid_gps_data(device_id, timestamp, location)
                    VALUES (temp_row.device_id, temp_row.timestamp, temp_row.location);
            END;

            DELETE FROM unprocessed_gps_data WHERE id = temp_row.id;
        END LOOP;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION valid_green_zone(
    green_zone_center POINT, green_zone_radius DOUBLE PRECISION,
    gps_location POINT) RETURNS BOOLEAN
AS
$$
BEGIN
    RETURN (
        (ST_DISTANCE(green_zone_center, gps_location) <= green_zone_radius)
        );
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION alarm_gps_data_trigger() RETURNS TRIGGER AS
$alarm_gps_data_trigger$
DECLARE
    green_zone       RECORD;
    device_status_id INTEGER;
    device_status    VARCHAR;
BEGIN
    FOR green_zone IN SELECT green_zones
                      FROM green_zones
                               JOIN
                               (SELECT id FROM vehicles WHERE gps_device_id = NEW.device_id)
                               AS vehicles ON vehicles.id = green_zones.vehicle_id
        LOOP


            SELECT device_status FROM gps_devices WHERE id = NEW.device_id INTO device_status_id;

            SELECT status FROM gps_device_states WHERE id = device_status_id INTO device_status;

            IF NOT valid_green_zone(
                    green_zone.center,
                    green_zone.radius,
                    NEW.location) AND
               device_status != 'AlarmPause'
            THEN
                INSERT INTO alarms(gps_data_id)
                VALUES (NEW.id);
                RETURN NEW;
            END IF;

        END LOOP;
    RETURN NEW;
END;

$alarm_gps_data_trigger$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS alarm_gps_data_trigger ON gps_data;

CREATE TRIGGER alarm_gps_data_trigger
    AFTER INSERT
    ON gps_data
    FOR EACH ROW
EXECUTE FUNCTION alarm_gps_data_trigger();
