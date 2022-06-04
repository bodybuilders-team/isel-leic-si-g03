DROP PROCEDURE IF EXISTS create_vehicle CASCADE;

-- 2h)
/*
    Procedure:      create_vehicle
    Description:    Creates a vehicle with the respective associated equipment information,
                    and associates it with a customer.
                    If data is passed to the creation of a green zone, creates it and
                    associates the vehicle with that zone.
    Parameter(s):   @v_gps_device_id
                    @v_client_id
                    @v_license_plate
                    @v_num_alarms
                    @gz_center_location
                    @gz_radius
    Return:         -
*/
CREATE OR REPLACE PROCEDURE create_vehicle(
    v_gps_device_id INTEGER,
    v_client_id INTEGER,
    v_license_plate VARCHAR(10),
    v_num_alarms INTEGER,
    gz_center_location_lat FLOAT DEFAULT NULL,
    gz_center_location_lon FLOAT DEFAULT NULL,
    gz_radius DOUBLE PRECISION DEFAULT NULL
)
    LANGUAGE plpgsql
AS
$$
DECLARE
    v_id                INTEGER;
    v_client_cars_count INTEGER;
    green_zone_id       INTEGER;
BEGIN
    -- Get number of client cars
    SELECT COUNT(*)
    FROM vehicles
    WHERE client_id = v_client_id
    INTO v_client_cars_count;

    -- If the client is institutional or has less than 3 cars
    IF (SELECT EXISTS(SELECT 1 FROM institutional_clients WHERE id = v_client_id) OR v_client_cars_count < 3) THEN
        INSERT INTO vehicles(gps_device_id, client_id, license_plate, num_alarms)
        VALUES (v_gps_device_id, v_client_id, v_license_plate, v_num_alarms)
        RETURNING id INTO v_id;

        IF (gz_center_location_lat IS NOT NULL AND gz_center_location_lon IS NOT NULL AND gz_radius IS NOT NULL) THEN
            INSERT INTO green_zones(vehicle_id, lat, lon, radius)
            VALUES (v_id, gz_center_location_lat, gz_center_location_lon, gz_radius)
            RETURNING id INTO green_zone_id;
        END IF;
    END IF;
END;
$$;