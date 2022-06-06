-- This script contains the procedures from item 2d)

DROP PROCEDURE IF EXISTS insert_private_client CASCADE;
DROP PROCEDURE IF EXISTS remove_private_client CASCADE;
DROP PROCEDURE IF EXISTS update_private_client CASCADE;

-- todo: check if locks are necessary for operations.

-- 2d)

/*
    Procedure:          insert_private_client
    Description:        Inserts a private client, given his information.
    Parameter(s):       @client_citizen_card_number - the client's citizen card number.
                        @client_name                - the client's name.
                        @client_phone_number        - the client's phone number.
                        @client_nif                 - the client's nif.
                        @client_address             - the client's address.
                        @client_referee             - the client's referee.
    Isolation Level:    READ UNCOMMITTED
    Return:             -
*/
CREATE OR REPLACE PROCEDURE insert_private_client(
    client_citizen_card_number VARCHAR(15),
    client_name VARCHAR(60),
    client_phone_number VARCHAR(20),
    client_nif CHAR(9),
    client_address VARCHAR(200),
    client_referee INTEGER DEFAULT NULL
)
    LANGUAGE plpgsql
AS
$$
DECLARE
    client_id INTEGER;
BEGIN
    INSERT INTO clients(name, dtype, phone_number, nif, address, active, referral)
    VALUES (client_name, 'PrivateClient', client_phone_number, client_nif, client_address, true, client_referee)
    RETURNING id INTO client_id;

    INSERT INTO private_clients (id, citizen_card_number)
    VALUES (client_id, client_citizen_card_number);
END;
$$;


/*
    Procedure:          remove_private_client
    Description:        Removes a private client, given his id.
    Parameter(s):       @client_id - the client's id.
    Isolation Level:    READ UNCOMMITTED
    Return:             -
*/
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


/*
    Procedure:          update_private_client
    Description:        Updates a private client, given his id and new information.
    Parameter(s):       @client_id - the client's id.
                        @client_citizen_card_number - the client's citizen card number.
                        @new_nif - the client's new nif.
                        @new_name - the client's new name.
                        @new_address - the client's new address.
                        @new_client_referee - the client's new referee.
    Isolation Level:    READ UNCOMMITTED
    Return:             -
*/
CREATE OR REPLACE PROCEDURE update_private_client(
    client_id INTEGER,
    new_citizen_card_number VARCHAR(15),
    new_nif CHAR(9),
    new_name VARCHAR(60),
    new_address VARCHAR(200),
    new_client_referee INTEGER DEFAULT NULL
)
    LANGUAGE plpgsql
AS
$$
BEGIN
    IF NOT EXISTS(SELECT id FROM clients WHERE id = client_id) THEN
        RAISE EXCEPTION 'Client with id % does not exist', client_id;
    END IF;

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
