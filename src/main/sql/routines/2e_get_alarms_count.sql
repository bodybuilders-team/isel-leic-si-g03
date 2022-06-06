DROP FUNCTION IF EXISTS get_alarms_count CASCADE;

-- 2e)
/*
    Function:           get_alarms_count
    Description:        Returns the total number of alarms for a given year and vehicle.
                        If the license plate is empty, returns the total number of alarms
                        for all vehicles of the given year.
    Parameter(s):       @year_a             - The year of the alarms.
                        @license_plate_a    - The license plate of the vehicle.
    Isolation Level:    READ COMMITTED
    Return:             total number of alarms
*/
CREATE OR REPLACE FUNCTION get_alarms_count(
    year_a INTEGER,
    license_plate_a VARCHAR(10) DEFAULT NULL
)
    RETURNS INTEGER
    LANGUAGE plpgsql AS
$$
BEGIN
    -- If license plate is specified, return the number of alarms for the given year and license plate.
    IF (license_plate_a IS NOT NULL) THEN
        RETURN (SELECT COUNT(*)
                FROM alarms
                         JOIN(SELECT gps_data.id
                              FROM gps_data
                                       JOIN (SELECT gps_device_id AS id
                                             FROM vehicles
                                             WHERE vehicles.license_plate = license_plate_a) AS gps_device_ids
                                            ON gps_data.device_id = gps_device_ids.id
                              WHERE extract(YEAR from gps_data.timestamp) = year_a) AS gps_data_ids
                             ON alarms.gps_data_id = gps_data_ids.id);
    END IF;

    -- If license plate is not specified, return the number of alarms for the given year.
    RETURN (SELECT COUNT(*)
            FROM alarms
                     JOIN(SELECT gps_data.id
                          FROM gps_data
                          WHERE extract(YEAR from gps_data.timestamp) = year_a) AS gps_data_ids
                         ON alarms.gps_data_id = gps_data_ids.id);
END;
$$;
