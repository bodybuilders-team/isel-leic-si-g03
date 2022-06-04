DROP PROCEDURE IF EXISTS process_gps_data CASCADE;

-- 2f)
/*
    Procedure:      process_gps_data
    Description:    Called periodically. Processes all unprocessed gps data.
    Parameter(s):   -
    Return:         -
*/
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
                INSERT INTO gps_data(device_id, timestamp, lat, lon)
                VALUES (temp_row.device_id, temp_row.timestamp, temp_row.lat, temp_row.lon);
            EXCEPTION
                WHEN OTHERS THEN
                    INSERT INTO invalid_gps_data(device_id, timestamp, lat, lon)
                    VALUES (temp_row.device_id, temp_row.timestamp, temp_row.lat, temp_row.lon);
            END;

            DELETE
            FROM unprocessed_gps_data
            WHERE id = temp_row.id;

        END LOOP;
END;
$$;
