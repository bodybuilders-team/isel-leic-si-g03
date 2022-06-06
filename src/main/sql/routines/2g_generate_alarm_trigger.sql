DROP FUNCTION IF EXISTS location_inside_green_zone CASCADE;
DROP FUNCTION IF EXISTS generate_alarm_trigger CASCADE;
DROP TRIGGER IF EXISTS generate_alarm ON gps_data CASCADE;

-- 2g)
/*
    Function:       location_inside_green_zone
    Description:    Checks if a gps location is inside the green zone.
    Parameter(s):   @green_zone_center  - the center of the green zone
                    @green_zone_radius  - the radius of the green zone
                    @gps_location       - the gps location to check
    Return:         true if the gps location is inside the green zone; false otherwise
*/
CREATE OR REPLACE FUNCTION location_inside_green_zone(
    green_zone_center POINT,
    gps_location POINT,
    green_zone_radius DOUBLE PRECISION
)
    RETURNS BOOLEAN
    LANGUAGE plpgsql
AS
$$
DECLARE
    a DOUBLE PRECISION;
BEGIN
    a := 0;

    -- Calculate distance between the gps location and the green zone center (in kilometers)
    a := a + power(sin(radians(gps_location[0] - green_zone_center[0]) / 2), 2);
    a := a + cos(radians(green_zone_center[0])) * cos(radians(gps_location[0])) *
             power(sin(radians(gps_location[1] - green_zone_center[1]) / 2), 2);

    -- Return true if the gps location is inside the green zone
    RETURN 6371 * 2 * atan2(sqrt(a), sqrt(1 - a)) <= green_zone_radius;
END;
$$;

/*
    Function:           generate_alarm_trigger
    Description:        Allows you to analyze the processed gps data when it is created,
                        generating the corresponding alarm if it is outside any of its green zones.
    Parameter(s):       -
    Isolation Level:    REPEATABLE READ
    Return:             trigger
*/
CREATE OR REPLACE FUNCTION generate_alarm_trigger()
    RETURNS TRIGGER
    LANGUAGE plpgsql
AS
$alarm_gps_data_trigger$
DECLARE
    green_zone       RECORD;
    device_status_id INTEGER;
    v_device_status  VARCHAR(20);
    v_driver_name    VARCHAR(60);
BEGIN
    -- Iterate through the green zones of the associated device
    FOR green_zone IN SELECT *
                      FROM green_zones
                               JOIN(SELECT id
                                    FROM vehicles
                                    WHERE gps_device_id = NEW.device_id) AS vehicles
                                   ON vehicles.id = green_zones.vehicle_id
        LOOP
            SELECT device_status
            FROM gps_devices
            WHERE id = NEW.device_id
            INTO device_status_id;

            SELECT status
            FROM gps_device_states
            WHERE id = device_status_id
            INTO v_device_status;

            SELECT name
            FROM vehicles
                     JOIN drivers ON vehicles.id = drivers.vehicle_id
            INTO v_driver_name;

            -- If the gps location is outside the green zone, generate an alarm
            IF location_inside_green_zone(
                       point(green_zone.lat, green_zone.lon),
                       point(NEW.lat, NEW.lon),
                       green_zone.radius
                   ) IS FALSE
                AND v_device_status != 'AlarmPause'
            THEN
                INSERT INTO alarms(gps_data_id, driver_name)
                VALUES (NEW.id, v_driver_name);
                RETURN NEW;
            END IF;
        END LOOP;

    RETURN NEW;
END;
$alarm_gps_data_trigger$;

/*
    Trigger:        generate_alarm
    Description:    Allows you to analyze the processed gps data when it is created,
                    generating the corresponding alarm if it is outside any of its green zones.
*/
CREATE TRIGGER generate_alarm
    AFTER INSERT
    ON gps_data
    FOR EACH ROW
EXECUTE FUNCTION generate_alarm_trigger();
