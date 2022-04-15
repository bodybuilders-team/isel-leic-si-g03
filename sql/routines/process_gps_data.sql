DROP PROCEDURE IF EXISTS process_gps_data CASCADE;

-- 2f)
/*
    Procedure:      process_gps_data
    Description:    Called periodically and which processes all not processed gps data.
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
