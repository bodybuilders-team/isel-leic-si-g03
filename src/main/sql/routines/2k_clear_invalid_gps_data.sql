DROP PROCEDURE IF EXISTS clear_invalid_gps_data CASCADE;

-- 2k)
/*
    Procedure:          clear_invalid_gps_data
    Description:        Called daily. Erases existing invalid gps data lasting longer than 15 days.
    Parameter(s):       -
    Isolation Level:    READ UNCOMMITTED
    Return:             -
*/
CREATE OR REPLACE PROCEDURE clear_invalid_gps_data()
    LANGUAGE plpgsql
AS
$$
BEGIN
    DELETE
    FROM invalid_gps_data
    WHERE extract(DAY from (current_date - invalid_gps_data.timestamp)) > 15;
END;
$$;