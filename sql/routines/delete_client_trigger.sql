DROP FUNCTION IF EXISTS delete_client_trigger CASCADE;
DROP TRIGGER IF EXISTS delete_client ON clients CASCADE;

-- 2l)
/*
    Function:      delete_client_trigger
    Description:    An execution of the DELETE statement on the "clients" table allows
                    deactivating the client without erasing their data.
    Parameter(s):   -
    Return:         trigger
*/
CREATE OR REPLACE FUNCTION delete_client_trigger()
    RETURNS TRIGGER
    LANGUAGE plpgsql
AS
$delete_client_trigger$
BEGIN

    -- Update active state to false
    UPDATE clients
    SET active = false
    WHERE id = OLD.id;

    RETURN NULL;
END;
$delete_client_trigger$;

/*
    Trigger:        delete_client
    Description:    An execution of the DELETE statement on the "clients" table allows
                    deactivating the client without erasing their data.
*/
CREATE TRIGGER delete_client
    BEFORE DELETE
    ON clients
    FOR EACH ROW
EXECUTE FUNCTION delete_client_trigger();

/*
    Trigger:        delete_private_client
    Description:    An execution of the DELETE statement on the "private_clients" table allows
                    deactivating the client without erasing their data.
*/
CREATE TRIGGER delete_private_client
    BEFORE DELETE
    ON private_clients
    FOR EACH ROW
EXECUTE FUNCTION delete_client_trigger();

/*
    Trigger:        delete_institutional_client
    Description:    An execution of the DELETE statement on the "institutional_clients" table allows
                    deactivating the client without erasing their data.
*/
CREATE TRIGGER delete_institutional_client
    BEFORE DELETE
    ON institutional_clients
    FOR EACH ROW
EXECUTE FUNCTION delete_client_trigger();
