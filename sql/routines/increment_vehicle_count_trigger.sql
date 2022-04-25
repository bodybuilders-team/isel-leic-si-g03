DROP FUNCTION IF EXISTS increment_vehicle_count_trigger CASCADE;
DROP TRIGGER IF EXISTS create_alarm ON alarms CASCADE;

-- 2m)
/*
    Function:       increment_vehicle_count_trigger
    Description:    When an alarm is created, the number of vehicle alarms is updated.
    Parameter(s):   -
    Return:         trigger
*/
CREATE OR REPLACE FUNCTION increment_vehicle_count_trigger()
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

/*
    Trigger:        increment_vehicle_count
    Description:    When an alarm is created, the number of vehicle alarms is updated.
*/
CREATE TRIGGER increment_vehicle_count
    AFTER INSERT
    ON alarms
    FOR EACH ROW
EXECUTE FUNCTION increment_vehicle_count_trigger();
