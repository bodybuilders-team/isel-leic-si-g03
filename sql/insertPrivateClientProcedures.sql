-- This script contains the procedures from item 2d)

DROP PROCEDURE IF EXISTS insert_private_client CASCADE;
DROP PROCEDURE IF EXISTS remove_private_client CASCADE;
DROP PROCEDURE IF EXISTS update_private_client CASCADE;

CREATE OR REPLACE PROCEDURE insert_private_client(
    client_citizen_card_number TEXT,
    client_name TEXT,
    client_phone_number TEXT,
    client_nif TEXT,
    client_address TEXT,
    client_referee INTEGER DEFAULT NULL
)
    LANGUAGE plpgsql
AS
$$
DECLARE
    client_id INTEGER;
BEGIN
    INSERT INTO clients(name, phone_number, nif, address, active, referral)
    VALUES (client_name, client_phone_number, client_nif, client_address, true, client_referee)
    RETURNING id INTO client_id;

    INSERT INTO private_clients (id, citizen_card_number)
    VALUES (client_id, client_citizen_card_number);
END;
$$;

-- CALL insert_private_client('123343', 'andre', '12', '123', 'rua das ruas', 1);

CREATE OR REPLACE PROCEDURE remove_private_client(client_id INTEGER)
    LANGUAGE plpgsql
AS
$$
BEGIN
    DELETE
    FROM private_clients
    WHERE id = client_id;

    DELETE
    FROM clients
    WHERE id = client_id;
END;
$$;

-- CALL remove_private_client(3)

CREATE OR REPLACE PROCEDURE update_private_client(
    client_id INTEGER,
    new_citizen_card_number TEXT,
    new_nif TEXT,
    new_name TEXT,
    new_address TEXT,
    new_client_referee INTEGER DEFAULT NULL
)
    LANGUAGE plpgsql
AS
$$
BEGIN
    UPDATE private_clients
    SET citizen_card_number = new_citizen_card_number
    WHERE id = client_id;

    UPDATE clients
    SET nif      = new_nif,
        name     = new_name,
        address  = new_address,
        referral = new_client_referee
    WHERE id = client_id;
END;
$$;

-- CALL update_private_client(4,'4', '69', 'big jesus', 'nova rua', 2);
