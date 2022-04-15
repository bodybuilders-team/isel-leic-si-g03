DROP FUNCTION IF EXISTS create_alarm_trigger CASCADE;
DROP TRIGGER IF EXISTS create_alarm ON alarms CASCADE;

-- 2m)
/*
    Function:       create_alarm_trigger
    Description:    When an alarm is created, the number of vehicle alarms is updated.
    Parameter(s):   -
    Return:         trigger
*/
CREATE OR REPLACE FUNCTION create_alarm_trigger()
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
    Trigger:        create_alarm
    Description:    When an alarm is created, the number of vehicle alarms is updated.
*/
CREATE TRIGGER create_alarm
    AFTER INSERT
    ON alarms
    FOR EACH ROW
EXECUTE FUNCTION create_alarm_trigger();
