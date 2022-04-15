DROP FUNCTION IF EXISTS get_alarms_count CASCADE;

-- 2e)
/*
    Function:       get_alarms_count
    Description:    Returns the total number of alarms for a given year and vehicle.
    Parameter(s):   @year_a
                    @license_plate_a
    Return:         total number of alarms
*/
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
