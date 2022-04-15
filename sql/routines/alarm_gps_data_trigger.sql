DROP FUNCTION IF EXISTS valid_green_zone CASCADE;
DROP FUNCTION IF EXISTS alarm_gps_data_trigger CASCADE;
DROP TRIGGER IF EXISTS alarm_gps_data ON gps_data CASCADE;

-- 2g)
/*
    Function:       valid_green_zone
    Description:    Checks if a gps location is inside the green zone.
    Parameter(s):   @green_zone_center
                    @green_zone_radius
                    @gps_location
    Return:         true if the gps location is inside the green zone; false otherwise
*/
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
    RETURN ((green_zone_center <-> gps_location) <= green_zone_radius);
END;
$$;

-- TODO arranjar melhor nome
/*
    Function:       alarm_gps_data_trigger
    Description:    Allows you to analyze the processed gps data when it is created and to
                    generates the corresponding alarm if it is outside any of its green zones.
    Parameter(s):   -
    Return:         trigger
*/
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

/*
    Trigger:        alarm_gps_data
    Description:    Allows you to analyze the processed gps data when it is created and to
                    generates the corresponding alarm if it is outside any of its green zones.
*/
CREATE TRIGGER alarm_gps_data
    AFTER INSERT
    ON gps_data
    FOR EACH ROW
EXECUTE FUNCTION alarm_gps_data_trigger();
