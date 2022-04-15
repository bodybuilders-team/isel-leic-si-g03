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
    new_device_id  INTEGER;
    new_vehicle_id INTEGER;
BEGIN
    -- Insert new device
    INSERT INTO gps_devices(device_status)
    VALUES (1)
    INTO new_device_id;

    -- Insert new gps data
    INSERT INTO gps_data(id, device_id, timestamp, location)
    VALUES (NEW.gps_data_id, new_device_id, NEW.timestamp, NEW.location);

    -- Insert new alarm
    INSERT INTO alarms(gps_data_id)
    VALUES (NEW.gps_data_id);

    INSERT INTO vehicles(gps_device_id, client_id, license_plate, num_alarms)
    VALUES (new_device_id, 1, NEW.license_plate, 1) -- TODO - what about client_id???
    INTO new_vehicle_id;

    INSERT INTO drivers(vehicle_id, name)
    VALUES (new_vehicle_id, NEW.driver_name);

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