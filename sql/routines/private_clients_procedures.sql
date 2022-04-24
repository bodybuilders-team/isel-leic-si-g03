-- This script contains the procedures from item 2d)

DROP PROCEDURE IF EXISTS insert_private_client CASCADE;
DROP PROCEDURE IF EXISTS remove_private_client CASCADE;
DROP PROCEDURE IF EXISTS update_private_client CASCADE;

-- 2d)

/*
    Procedure:      insert_private_client
    Description:    Inserts a private client, given his information.
    Parameter(s):   @client_citizen_card_number
                    @client_name
                    @client_phone_number
                    @client_nif
                    @client_address
                    @client_referee
    Return:         -
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
    INSERT INTO clients(name, phone_number, nif, address, active, referral)
    VALUES (client_name, client_phone_number, client_nif, client_address, true, client_referee)
    RETURNING id INTO client_id;

    INSERT INTO private_clients (id, citizen_card_number)
    VALUES (client_id, client_citizen_card_number);
END;
$$;


/*
    Procedure:      remove_private_client
    Description:    Removes a private client, given his id.
    Parameter(s):   @client_id
    Return:         -
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
    Procedure:      update_private_client
    Description:    Updates a private client, given his id and new information.
    Parameter(s):   @client_id
                    @client_citizen_card_number
                    @new_nif
                    @new_name
                    @new_address
                    @new_client_referee
    Return:         -
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
