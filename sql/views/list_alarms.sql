DROP VIEW IF EXISTS list_alarms CASCADE;

-- 2i)
/*
    View:           list_alarms
    Description:    List all alarms.
*/
CREATE OR REPLACE VIEW list_alarms AS
SELECT gps_data_id, license_plate, driver_name, location, timestamp
FROM alarms
         JOIN (
    SELECT license_plate, driver_name, location, timestamp, gps_data.id AS id
    FROM gps_data
             JOIN (
        SELECT gps_device_id, license_plate, drivers.name AS driver_name
        FROM vehicles
                 JOIN drivers ON vehicles.id = drivers.vehicle_id
    ) AS vehicles_and_drivers ON gps_data.device_id = vehicles_and_drivers.gps_device_id
) AS alarm_data ON alarms.gps_data_id = alarm_data.id;