-- This script contains the tests of the functionalities of items 2d) to 2m) (item 2n)

CREATE OR REPLACE PROCEDURE clear_and_insert_data()
    LANGUAGE plpgsql
AS
$$
BEGIN
    CALL clear_data();
    CALL insert_data();
END;
$$;


-- 2d)

BEGIN;
SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;
DO
$$
    DECLARE
        client RECORD;
    BEGIN
        CALL clear_and_insert_data();

        CALL insert_private_client('123456789', 'André Jesus', '912912912', '123123123', 'Rua das Ruas');

        SELECT * FROM private_clients WHERE citizen_card_number = '123456789' INTO client;
        IF (client IS NOT NULL) THEN
            RAISE NOTICE '2d) Test 1: Insert private client - Scenario 1: OK';
        ELSE
            RAISE EXCEPTION '2d) Test 1: Insert private client - Scenario 1: NOK';
        END IF;

        CALL clear_and_insert_data();
    END;
$$;
COMMIT;

BEGIN;
SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;
DO
$$
    DECLARE
        client RECORD;
    BEGIN
        CALL clear_and_insert_data();

        CALL remove_private_client(1);

        SELECT * FROM private_clients WHERE id = 1 INTO client;
        IF (client IS NULL) THEN
            RAISE NOTICE '2d) Test 2: Remove private client - Scenario 1: OK';
        ELSE
            RAISE EXCEPTION '2d) Test 2: Remove private client - Scenario 1: NOK';
        END IF;

        CALL clear_and_insert_data();
    END;
$$;
COMMIT;

BEGIN;
SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;
DO
$$
    DECLARE
        client RECORD;
    BEGIN
        CALL clear_and_insert_data();

        CALL update_private_client(1, '4', '69', 'Big Jesus', 'nova rua');

        SELECT * FROM private_clients WHERE id = 1 INTO client;
        IF (client IS NOT NULL AND client.citizen_card_number = '4') THEN
            RAISE NOTICE '2d) Test 3: Update private client - Scenario 1: OK';
        ELSE
            RAISE EXCEPTION '2d) Test 3: Update private client - Scenario 1: NOK';
        END IF;

        CALL clear_and_insert_data();
    END;
$$;
COMMIT;

-- 2e)
BEGIN;
SET TRANSACTION ISOLATION LEVEL READ COMMITTED;
DO
$$
    DECLARE
        alarms_count INTEGER;
    BEGIN
        CALL clear_and_insert_data();

        SELECT get_alarms_count(2022)
        INTO alarms_count;

        IF (alarms_count = 2) THEN
            RAISE NOTICE '2e) Test 1: Get alarms count without license plate - Scenario 1: OK';
        ELSE
            RAISE EXCEPTION '2e) Test 1: Get alarms count without license plate - Scenario 1: NOK';
        END IF;

        CALL clear_and_insert_data();
    END;
$$;
COMMIT;

BEGIN;
SET TRANSACTION ISOLATION LEVEL READ COMMITTED;
DO
$$
    DECLARE
        alarms_count INTEGER;
    BEGIN
        CALL clear_and_insert_data();

        SELECT get_alarms_count(1000)
        INTO alarms_count;

        IF (alarms_count = 0) THEN
            RAISE NOTICE '2e) Test 2: Get alarms count without license plate - Scenario 2: OK';
        ELSE
            RAISE EXCEPTION '2e) Test 2: Get alarms count without license plate - Scenario 2: NOK';
        END IF;

        CALL clear_and_insert_data();
    END;
$$;
COMMIT;

BEGIN;
SET TRANSACTION ISOLATION LEVEL READ COMMITTED;
DO
$$
    DECLARE
        alarms_count INTEGER;
    BEGIN
        CALL clear_and_insert_data();

        SELECT get_alarms_count(2022, '99-DH-66')
        INTO alarms_count;

        IF (alarms_count = 1) THEN
            RAISE NOTICE '2e) Test 3: Get alarms count with license plate - Scenario 1: OK';
        ELSE
            RAISE EXCEPTION '2e) Test 3: Get alarms count with license plate - Scenario 1: NOK';
        END IF;

        CALL clear_and_insert_data();
    END;
$$;
COMMIT;

BEGIN;
SET TRANSACTION ISOLATION LEVEL READ COMMITTED;
DO
$$
    DECLARE
        alarms_count INTEGER;
    BEGIN
        CALL clear_and_insert_data();

        SELECT get_alarms_count(2022, '33-44-55')
        INTO alarms_count;

        IF (alarms_count = 1) THEN
            RAISE NOTICE '2e) Test 4: Get alarms count with license plate - Scenario 2: OK';
        ELSE
            RAISE EXCEPTION '2e) Test 4: Get alarms count with license plate - Scenario 2: NOK';
        END IF;

        CALL clear_and_insert_data();
    END;
$$;
COMMIT;

-- 2f)

BEGIN;
SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;
DO
$$
    DECLARE
        processed_gps_data_var RECORD;
    BEGIN
        CALL clear_and_insert_data();

        INSERT INTO unprocessed_gps_data(device_id, timestamp, lat, lon, version)
        VALUES (1, '2022-04-12 14:05:06 UTC+1', 30, 15, 0);

        CALL process_gps_data();

        SELECT *
        FROM gps_data
        WHERE timestamp = '2022-04-12 14:05:06 UTC+1'
        INTO processed_gps_data_var;

        IF (processed_gps_data_var IS NOT NULL) THEN
            RAISE NOTICE '2f) Test 1: Process a gps data - Scenario 1: OK';
        ELSE
            RAISE EXCEPTION '2f) Test 1: Process a gps data - Scenario 1: NOK';
        END IF;

        CALL clear_and_insert_data();
    END;
$$;
COMMIT;

BEGIN;
SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;
DO
$$
    DECLARE
        processed_gps_data_var RECORD;
    BEGIN
        CALL clear_and_insert_data();

        INSERT INTO unprocessed_gps_data(device_id, timestamp, lat, lon, version)
        VALUES (10, '2022-04-12 14:05:06 UTC+1', 30, 15, 0);

        CALL process_gps_data();

        SELECT *
        FROM gps_data
        WHERE timestamp = '2022-04-12 14:05:06 UTC+1'
        INTO processed_gps_data_var;

        IF (processed_gps_data_var IS NULL) THEN
            RAISE NOTICE '2f) Test 2: Process a gps data with invalid device id - Scenario 1: OK';
        ELSE
            RAISE EXCEPTION '2f) Test 2: Process a gps data with invalid device id - Scenario 1: NOK';
        END IF;

        CALL clear_and_insert_data();
    END;
$$;
COMMIT;

-- 2g)

BEGIN;
DO
$$
    BEGIN
        CALL clear_and_insert_data();

        IF NOT (location_inside_green_zone(point(10, 10), 5, point(20, 20))) THEN
            RAISE NOTICE '2g) Test 1: Location is not inside green zone - Scenario 1: OK';
        ELSE
            RAISE EXCEPTION '2g) Test 1: Location is not inside green zone - Scenario 1: NOK';
        END IF;

        CALL clear_and_insert_data();
    END;
$$;
COMMIT;

BEGIN;
DO
$$
    BEGIN
        CALL clear_and_insert_data();

        IF (location_inside_green_zone(point(10, 10), 20, point(20, 20))) THEN
            RAISE NOTICE '2g) Test 2: Location is inside green zone - Scenario 1: OK';
        ELSE
            RAISE EXCEPTION '2g) Test 2: Location is inside green zone - Scenario 1: NOK';
        END IF;

        CALL clear_and_insert_data();
    END;
$$;
COMMIT;

BEGIN;
SET TRANSACTION ISOLATION LEVEL REPEATABLE READ;
DO
$$
    DECLARE
        alarm RECORD;
    BEGIN
        CALL clear_and_insert_data();

        INSERT INTO gps_data(device_id, timestamp, lat, lon)
        VALUES (1, '2022-04-12 14:05:06 UTC+1', 38, -9);

        SELECT *
        FROM alarms
                 JOIN gps_data on alarms.gps_data_id = gps_data.id
        WHERE timestamp = '2022-04-12 14:05:06 UTC+1'
        INTO alarm;

        IF (alarm IS NULL) THEN
            RAISE NOTICE '2g) Test 3: New GPS data does not trigger alarm - Scenario 1: OK';
        ELSE
            RAISE EXCEPTION '2g) Test 3: New GPS data does not trigger alarm - Scenario 1: NOK';
        END IF;

        CALL clear_and_insert_data();
    END;
$$;
COMMIT;

BEGIN;
SET TRANSACTION ISOLATION LEVEL REPEATABLE READ;
DO
$$
    DECLARE
        alarm RECORD;
    BEGIN
        CALL clear_and_insert_data();

        INSERT INTO gps_data(device_id, timestamp, lat, lon)
        VALUES (1, '2022-04-12 14:05:06 UTC+1', -50, -50);

        SELECT *
        FROM alarms
                 JOIN gps_data on alarms.gps_data_id = gps_data.id
        WHERE timestamp = '2022-04-12 14:05:06 UTC+1'
        INTO alarm;

        IF (alarm IS NOT NULL) THEN
            RAISE NOTICE '2g) Test 4: New GPS data triggers alarm - Scenario 1: OK';
        ELSE
            RAISE EXCEPTION '2g) Test 4: New GPS data triggers alarm - Scenario 1: NOK';
        END IF;

        CALL clear_and_insert_data();
    END;
$$;
COMMIT;

-- 2h)

BEGIN;
SET TRANSACTION ISOLATION LEVEL SERIALIZABLE;
DO
$$
    DECLARE
        vehicle RECORD;
    BEGIN
        CALL clear_and_insert_data();

        INSERT INTO gps_devices(device_status) VALUES (1);

        CALL create_vehicle(4, 1, '11-HH-22', 0);

        SELECT *
        FROM vehicles
        WHERE license_plate = '11-HH-22'
        INTO vehicle;

        IF (vehicle IS NOT NULL) THEN
            RAISE NOTICE '2h) Test 1: Vehicle is created - Scenario 1: OK';
        ELSE
            RAISE EXCEPTION '2h) Test 1: Vehicle is created - Scenario 1: NOK';
        END IF;

        CALL clear_and_insert_data();
    END;
$$;
COMMIT;

BEGIN;
SET TRANSACTION ISOLATION LEVEL SERIALIZABLE;
DO
$$
    DECLARE
        vehicle RECORD;
    BEGIN
        CALL clear_and_insert_data();

        INSERT INTO gps_devices(device_status) VALUES (1), (1);
        INSERT INTO vehicles(gps_device_id, client_id, license_plate, num_alarms) VALUES (4, 1, '11-HH-22', 0);

        CALL create_vehicle(5, 1, '18-H1-77', 0);

        SELECT *
        FROM vehicles
        WHERE license_plate = '18-H1-77'
        INTO vehicle;

        IF (vehicle IS NULL) THEN
            RAISE NOTICE '2h) Test 2: Vehicle is not created because client already has 3 or more cars - Scenario 1: OK';
        ELSE
            RAISE EXCEPTION '2h) Test 2: Vehicle is not created because client already has 3 or more cars - Scenario 1: NOK';
        END IF;

        CALL clear_and_insert_data();
    END;
$$;
COMMIT;

BEGIN;
SET TRANSACTION ISOLATION LEVEL SERIALIZABLE;
DO
$$
    DECLARE
        vehicle RECORD;
    BEGIN
        CALL clear_and_insert_data();

        INSERT INTO gps_devices(device_status) VALUES (1), (1), (1);
        INSERT INTO vehicles(gps_device_id, client_id, license_plate, num_alarms) VALUES (4, 2, '11-HH-22', 0);
        INSERT INTO vehicles(gps_device_id, client_id, license_plate, num_alarms) VALUES (5, 2, '19-T2-08', 0);

        CALL create_vehicle(6, 2, '18-H1-77', 0);

        SELECT *
        FROM vehicles
        WHERE license_plate = '18-H1-77'
        INTO vehicle;

        IF (vehicle IS NOT NULL) THEN
            RAISE NOTICE '2h) Test 3: Vehicle is created because client is institutional although having 3 or more cars - Scenario 1: OK';
        ELSE
            RAISE EXCEPTION '2h) Test 3: Vehicle is created because client is institutional although having 3 or more cars - Scenario 1: NOK';
        END IF;

        CALL clear_and_insert_data();
    END;
$$;
COMMIT;

BEGIN;
SET TRANSACTION ISOLATION LEVEL SERIALIZABLE;
DO
$$
    DECLARE
        green_zone RECORD;
    BEGIN
        CALL clear_and_insert_data();

        INSERT INTO gps_devices(device_status) VALUES (1), (1), (1);
        INSERT INTO vehicles(gps_device_id, client_id, license_plate, num_alarms) VALUES (4, 2, '11-HH-22', 0);
        INSERT INTO vehicles(gps_device_id, client_id, license_plate, num_alarms) VALUES (5, 2, '19-T2-08', 0);

        CALL create_vehicle(6, 2, '18-H1-77', 0, point(10, 512), 5);

        SELECT *
        FROM green_zones
        WHERE lat == 10
          AND lon = 512
        INTO green_zone;

        IF (green_zone IS NOT NULL) THEN
            RAISE NOTICE '2h) Test 4: Green zone is created if passed as argument - Scenario 1: OK';
        ELSE
            RAISE EXCEPTION '2h) Test 4: Green zone is created if passed as argument - Scenario 1: NOK';
        END IF;

        CALL clear_and_insert_data();
    END;
$$;
COMMIT;

-- 2i)

BEGIN;
SET TRANSACTION ISOLATION LEVEL REPEATABLE READ;
DO
$$
    DECLARE
        alarm RECORD;
    BEGIN
        CALL clear_and_insert_data();

        SELECT *
        FROM list_alarms
        WHERE license_plate = '99-DH-66'
        INTO alarm;

        IF (alarm IS NOT NULL) THEN
            RAISE NOTICE '2i) Test 1: list_alarms lists alarms correctly - Scenario 1: OK';
        ELSE
            RAISE EXCEPTION '2i) Test 1: list_alarms lists alarms correctly - Scenario 1: NOK';
        END IF;

        CALL clear_and_insert_data();
    END;
$$;
COMMIT;

-- 2j)

BEGIN;
SET TRANSACTION ISOLATION LEVEL REPEATABLE READ;
DO
$$
    DECLARE
        alarm RECORD;
    BEGIN
        CALL clear_and_insert_data();

        INSERT INTO list_alarms(gps_data_id, license_plate, timestamp, lat, lon)
        VALUES (420, '88-66-33', '2022-02-12 17:05:06.000000 +00:00', 11, 11);

        SELECT *
        FROM list_alarms
        WHERE license_plate = '88-66-33'
        INTO alarm;

        IF (alarm IS NOT NULL) THEN
            RAISE NOTICE '2j) Test 1: insert_into_list_alarms trigger works correctly - Scenario 1: OK';
        ELSE
            RAISE EXCEPTION '2j) Test 1: insert_into_list_alarms trigger works correctly - Scenario 1: NOK';
        END IF;

        CALL clear_and_insert_data();
    END;
$$;
COMMIT;

-- 2k)

BEGIN;
SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;
DO
$$
    DECLARE
        gps_data_var   RECORD;
        test_timestamp TIMESTAMP = '2022-04-01 17:05:06.000000 +00:00';
    BEGIN
        CALL clear_and_insert_data();

        INSERT INTO invalid_gps_data(device_id, timestamp, lat, lon)
        VALUES (1, test_timestamp, 11, 11);

        CALL clear_invalid_gps_data();

        SELECT *
        FROM invalid_gps_data
        WHERE timestamp = test_timestamp
        INTO gps_data_var;

        IF (gps_data_var IS NULL) THEN
            RAISE NOTICE '2k) Test 1: clear_invalid_gps_data clears correctly - Scenario 1: OK';
        ELSE
            RAISE EXCEPTION '2k) Test 1: clear_invalid_gps_data clears correctly - Scenario 1: NOK';
        END IF;

        CALL clear_and_insert_data();
    END;
$$;
COMMIT;

BEGIN;
SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;
DO
$$
    DECLARE
        gps_data_var   RECORD;
        test_timestamp TIMESTAMP = current_date;
    BEGIN
        CALL clear_and_insert_data();

        INSERT INTO invalid_gps_data(device_id, timestamp, lat, lon)
        VALUES (1, test_timestamp, 11, 11);

        CALL clear_invalid_gps_data();

        SELECT *
        FROM invalid_gps_data
        WHERE timestamp = test_timestamp
        INTO gps_data_var;

        IF (gps_data_var IS NOT NULL) THEN
            RAISE NOTICE '2k) Test 2: clear_invalid_gps_data does not clear if 15 days have not passed yet - Scenario 1: OK';
        ELSE
            RAISE EXCEPTION '2k) Test 2: clear_invalid_gps_data does not clear if 15 days have not passed yet - Scenario 1: NOK';
        END IF;

        CALL clear_and_insert_data();
    END;
$$;
COMMIT;


-- 2l)

BEGIN;
SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;
DO
$$
    DECLARE
    BEGIN
        CALL clear_and_insert_data();

        DELETE
        FROM clients
        WHERE id = 1;

        IF EXISTS(SELECT * FROM clients WHERE id = 1) THEN
            RAISE NOTICE '2l) Test 1: delete_client_trigger does not delete client - Scenario 1: OK';
        ELSE
            RAISE EXCEPTION '2l) Test 1: delete_client_trigger does not delete client - Scenario 1: NOK';
        END IF;

        CALL clear_and_insert_data();
    END;
$$;
COMMIT;

BEGIN;
SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;
DO
$$
    DECLARE
        prev_active BOOLEAN;
        cur_active  BOOLEAN;
    BEGIN
        CALL clear_and_insert_data();

        SELECT active
        FROM clients
        WHERE id = 1
        INTO prev_active;

        DELETE
        FROM clients
        WHERE id = 1;

        SELECT active
        FROM clients
        WHERE id = 1
        INTO cur_active;

        IF (prev_active = true AND cur_active = false) THEN
            RAISE NOTICE '2l) Test 2: delete_client_trigger sets active to false - Scenario 1: OK';
        ELSE
            RAISE EXCEPTION '2l) Test 2: delete_client_trigger sets active to false - Scenario 1: NOK';
        END IF;

        CALL clear_and_insert_data();
    END;
$$;
COMMIT;


-- 2m)

BEGIN;
SET TRANSACTION ISOLATION LEVEL REPEATABLE READ;
DO
$$
    DECLARE
        prev_alarm_count INTEGER;
        cur_alarm_count  INTEGER;
    BEGIN
        CALL clear_and_insert_data();

        SELECT num_alarms
        FROM vehicles
        WHERE id = 2
        INTO prev_alarm_count;

        -- gps data triggers alarm
        INSERT INTO gps_data(device_id, timestamp, lat, lon)
        VALUES (2, '2022-03-12 07:05:06.000000 +00:00', 0, 0);

        SELECT num_alarms
        FROM vehicles
        WHERE id = 2
        INTO cur_alarm_count;

        IF (cur_alarm_count = prev_alarm_count + 1) THEN
            RAISE NOTICE '2m) Test 1: create_alarm_trigger increments vehicle alarm count - Scenario 1: OK';
        ELSE
            RAISE EXCEPTION '2m) Test 1: create_alarm_trigger increments vehicle alarm count - Scenario 1: NOK';
        END IF;

        CALL clear_and_insert_data();
    END;
$$;
COMMIT;