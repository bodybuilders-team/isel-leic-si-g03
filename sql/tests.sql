-- This script contains the tests of the functionalities of items 2d) to 2m) (item 2n)
-- TODO - Run clear_data.sql and insert_data.sql before/after each test

-- 2d)

DO
$$
    DECLARE
        client RECORD;
    BEGIN
        CALL insert_private_client('123456789', 'André Jesus', '912912912', '123123123', 'Rua das Ruas');

        SELECT * FROM private_clients WHERE citizen_card_number = '123456789' INTO client;
        IF (client IS NOT NULL) THEN
            RAISE NOTICE 'Test 1: Insert private client - Scenario 1: OK';
        ELSE
            RAISE NOTICE 'Test 1: Insert private client - Scenario 1: NOK';
        END IF;

        ROLLBACK;
    END;
$$;

DO
$$
    DECLARE
        client RECORD;
    BEGIN
        CALL remove_private_client(1);

        SELECT * FROM private_clients WHERE id = 1 INTO client;
        IF (client IS NULL) THEN
            RAISE NOTICE 'Test 2: Remove private client - Scenario 1: OK';
        ELSE
            RAISE NOTICE 'Test 2: Remove private client - Scenario 1: NOK';
        END IF;

        ROLLBACK;
    END;
$$;

DO
$$
    DECLARE
        client RECORD;
    BEGIN
        CALL update_private_client(1, '4', '69', 'Big Jesus', 'nova rua');

        SELECT * FROM private_clients WHERE id = 1 INTO client;
        IF (client IS NOT NULL AND client.citizen_card_number = '4') THEN
            RAISE NOTICE 'Test 3: Update private client - Scenario 1: OK';
        ELSE
            RAISE NOTICE 'Test 3: Update private client - Scenario 1: NOK';
        END IF;

        ROLLBACK;
    END;
$$;

-- 2e)
DO
$$
    DECLARE
        alarms_count INTEGER;
    BEGIN
        SELECT get_alarms_count(2022)
        INTO alarms_count;

        IF (alarms_count = 1) THEN
            RAISE NOTICE 'Test 1: Get alarms count without license plate - Scenario 1: OK';
        ELSE
            RAISE NOTICE 'Test 1: Get alarms count without license plate - Scenario 1: NOK';
        END IF;

        ROLLBACK;
    END;
$$;

DO
$$
    DECLARE
        alarms_count INTEGER;
    BEGIN
        SELECT get_alarms_count(2020)
        INTO alarms_count;

        IF (alarms_count = 0) THEN
            RAISE NOTICE 'Test 2: Get alarms count without license plate - Scenario 2: OK';
        ELSE
            RAISE NOTICE 'Test 2: Get alarms count without license plate - Scenario 2: NOK';
        END IF;

        ROLLBACK;
    END;
$$;

DO
$$
    DECLARE
        alarms_count INTEGER;
    BEGIN
        SELECT get_alarms_count(2022, '99-DH-66')
        INTO alarms_count;

        IF (alarms_count = 1) THEN
            RAISE NOTICE 'Test 3: Get alarms count with license plate - Scenario 1: OK';
        ELSE
            RAISE NOTICE 'Test 3: Get alarms count with license plate - Scenario 1: NOK';
        END IF;

        ROLLBACK;
    END;
$$;

DO
$$
    DECLARE
        alarms_count INTEGER;
    BEGIN
        SELECT get_alarms_count(2022, '33-44-55')
        INTO alarms_count;

        IF (alarms_count = 0) THEN
            RAISE NOTICE 'Test 4: Get alarms count with license plate - Scenario 2: OK';
        ELSE
            RAISE NOTICE 'Test 4: Get alarms count with license plate - Scenario 2: NOK';
        END IF;

        ROLLBACK;
    END;
$$;

-- 2f)

DO
$$
    DECLARE
        processed_gps_data_var RECORD;
    BEGIN

        INSERT INTO unprocessed_gps_data(device_id, timestamp, location)
        VALUES (1, '2022-04-12 14:05:06 UTC+1', point(30, 15));

        CALL process_gps_data();

        SELECT *
        FROM gps_data
        WHERE timestamp = '2022-04-12 14:05:06 UTC+1'
        INTO processed_gps_data_var;

        IF (processed_gps_data_var IS NOT NULL) THEN
            RAISE NOTICE 'Test 1: Process a gps data - Scenario 1: OK';
        ELSE
            RAISE NOTICE 'Test 1: Process a gps data - Scenario 1: NOK';
        END IF;

        ROLLBACK;
    END;
$$;

DO
$$
    DECLARE
        processed_gps_data_var RECORD;
    BEGIN

        INSERT INTO unprocessed_gps_data(device_id, timestamp, location)
        VALUES (10, '2022-04-12 14:05:06 UTC+1', point(30, 15));

        CALL process_gps_data();

        SELECT *
        FROM gps_data
        WHERE timestamp = '2022-04-12 14:05:06 UTC+1'
        INTO processed_gps_data_var;

        IF (processed_gps_data_var IS NULL) THEN
            RAISE NOTICE 'Test 2: Process a gps data with invalid device id - Scenario 1: OK';
        ELSE
            RAISE NOTICE 'Test 2: Process a gps data with invalid device id - Scenario 1: NOK';
        END IF;

        ROLLBACK;
    END;
$$;

-- 2g)

DO
$$
    BEGIN
        IF NOT (location_inside_green_zone(point(10, 10), 5, point(20, 20))) THEN
            RAISE NOTICE 'Test 1: Location is not inside green zone - Scenario 1: OK';
        ELSE
            RAISE NOTICE 'Test 1: Location is not inside green zone - Scenario 1: NOK';
        END IF;

        ROLLBACK;
    END;
$$;

DO
$$
    BEGIN
        IF (location_inside_green_zone(point(10, 10), 20, point(20, 20))) THEN
            RAISE NOTICE 'Test 2: Location is inside green zone - Scenario 1: OK';
        ELSE
            RAISE NOTICE 'Test 2: Location is inside green zone - Scenario 1: NOK';
        END IF;

        ROLLBACK;
    END;
$$;

DO
$$
    DECLARE
        alarm RECORD;
    BEGIN

        INSERT INTO gps_data(device_id, timestamp, location)
        VALUES (1, '2022-04-12 14:05:06 UTC+1', point(38, -9));

        SELECT *
        FROM alarms
                 JOIN gps_data on alarms.gps_data_id = gps_data.id
        WHERE timestamp = '2022-04-12 14:05:06 UTC+1'
        INTO alarm;

        IF (alarm IS NULL) THEN
            RAISE NOTICE 'Test 3: New GPS data does not trigger alarm - Scenario 1: OK';
        ELSE
            RAISE NOTICE 'Test 3: New GPS data does not trigger alarm - Scenario 1: NOK';
        END IF;

        ROLLBACK;
    END;
$$;

DO
$$
    DECLARE
        alarm RECORD;
    BEGIN

        INSERT INTO gps_data(device_id, timestamp, location)
        VALUES (1, '2022-04-12 14:05:06 UTC+1', point(-50, -50));

        SELECT *
        FROM alarms
                 JOIN gps_data on alarms.gps_data_id = gps_data.id
        WHERE timestamp = '2022-04-12 14:05:06 UTC+1'
        INTO alarm;

        IF (alarm IS NOT NULL) THEN
            RAISE NOTICE 'Test 4: New GPS data triggers alarm - Scenario 1: OK';
        ELSE
            RAISE NOTICE 'Test 4: New GPS data triggers alarm - Scenario 1: NOK';
        END IF;

        ROLLBACK;
    END;
$$;

-- 2h)
-- 2i)
-- 2j)
-- 2k)
-- 2l)
-- 2m)