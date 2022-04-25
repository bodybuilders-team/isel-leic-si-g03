DROP VIEW IF EXISTS list_alarms CASCADE;

-- 2i)
/*
    View:           list_alarms
    Description:    List all alarms, along with information on the driver's name, the license plate,
                    location and timestamp.
*/
CREATE OR REPLACE VIEW list_alarms AS
SELECT gps_data_id, license_plate, driver_name, location, timestamp
FROM alarms
         JOIN (SELECT license_plate, location, timestamp, gps_data.id AS id
               FROM gps_data
                        JOIN vehicles ON gps_data.device_id = vehicles.gps_device_id) AS gps_data_and_vehicle
              ON alarms.gps_data_id = gps_data_and_vehicle.id;