DROP PROCEDURE IF EXISTS process_gps_data CASCADE;

-- 2f)
/*
    Procedure:          process_gps_data
    Description:        Called periodically. Processes all unprocessed gps data.
    Parameter(s):       -
    Isolation Level:    REPEATABLE READ
    Return:             -
*/
CREATE OR REPLACE PROCEDURE process_gps_data()
    LANGUAGE plpgsql
AS
$$
DECLARE
    temp_row RECORD;
BEGIN
    -- Iterate through all unprocessed gps data
    FOR temp_row IN SELECT * FROM unprocessed_gps_data
        LOOP
            BEGIN
                -- If the gps data is valid, insert it into the gps_data table
                INSERT INTO gps_data(device_id, timestamp, lat, lon)
                VALUES (temp_row.device_id, temp_row.timestamp, temp_row.lat, temp_row.lon);
            EXCEPTION
                WHEN OTHERS THEN
                    -- If the gps data is invalid, insert it into the invalid_gps_data table
                    INSERT INTO invalid_gps_data(device_id, timestamp, lat, lon)
                    VALUES (temp_row.device_id, temp_row.timestamp, temp_row.lat, temp_row.lon);
            END;

            -- Remove the gps data from the unprocessed_gps_data table
            DELETE
            FROM unprocessed_gps_data
            WHERE id = temp_row.id;

        END LOOP;
END;
$$;
