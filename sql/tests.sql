-- This script contains the tests of the functionalities of items 2d) to 2m) (item 2n)
-- TODO - Run clearData.sql and insertData.sql before/after each test

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
    END;
$$;

-- 2f)
-- 2g)
-- 2h)
-- 2i)
-- 2j)
-- 2k)
-- 2l)
-- 2m)