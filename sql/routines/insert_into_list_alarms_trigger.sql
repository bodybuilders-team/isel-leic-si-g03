DROP FUNCTION IF EXISTS insert_into_list_alarms_trigger CASCADE;
DROP TRIGGER IF EXISTS insert_into_list_alarms ON list_alarms CASCADE;


-- 2j)
/*
    Function:       insert_into_list_alarms_trigger
    Description:    Allows adding an alarm and its record to the "list_alarms" view.
    Parameter(s):   -
    Return:         trigger
*/
CREATE OR REPLACE FUNCTION insert_into_list_alarms_trigger()
    RETURNS TRIGGER
    LANGUAGE plpgsql
AS
$insert_into_list_alarms_trigger$
DECLARE
    vehicle RECORD;
BEGIN

    -- Get vehicle and if there's a driver, the driver's name --
    SELECT *, name as driver_name
    FROM vehicles
             LEFT JOIN drivers ON vehicles.id = drivers.vehicle_id
    WHERE vehicles.license_plate = NEW.license_plate
    INTO vehicle;

    -- Insert new gps data
    INSERT INTO gps_data(id, device_id, timestamp, location)
    VALUES (NEW.gps_data_id, vehicle.gps_device_id, NEW.timestamp, NEW.location);

    RETURN NEW;
END;
$insert_into_list_alarms_trigger$;

/*
    Trigger:        insert_into_list_alarms_trigger
    Description:    Allows adding an alarm and its record to the "list_alarms" view.
*/
CREATE TRIGGER insert_into_list_alarms_trigger
    INSTEAD OF INSERT
    ON list_alarms
    FOR EACH ROW
EXECUTE FUNCTION insert_into_list_alarms_trigger();