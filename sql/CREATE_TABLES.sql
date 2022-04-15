DROP TABLE IF EXISTS invalid_gps_data;
DROP TABLE IF EXISTS unprocessed_gps_data;
DROP TABLE IF EXISTS alarms;
DROP TABLE IF EXISTS gps_data;
DROP TABLE IF EXISTS green_zones;
DROP TABLE IF EXISTS drivers;
DROP TABLE if exists vehicles;
DROP TABLE IF EXISTS institutional_clients;
DROP TABLE IF EXISTS private_clients;
DROP TABLE IF EXISTS clients;
DROP TABLE IF EXISTS gps_devices;
DROP TABLE IF EXISTS gps_device_states;

CREATE TABLE clients
(
    id           SERIAL PRIMARY KEY,
    referral     INT         NULL REFERENCES clients (id),
    name         VARCHAR(60)  NOT NULL,
    phone_number VARCHAR(20)  NOT NULL,
    nif          CHAR(9)      NOT NULL,
    address      VARCHAR(200) NOT NULL,
    active       BOOLEAN      NOT NULL
);

CREATE TABLE private_clients
(
    id                  INT PRIMARY KEY REFERENCES clients (id),
    citizen_card_number VARCHAR(15) NOT NULL
);

CREATE TABLE institutional_clients
(
    id           INT PRIMARY KEY REFERENCES clients (id),
    contact_name VARCHAR(60) NOT NULL
);

CREATE TABLE gps_device_states
(
    id     SERIAL PRIMARY KEY,
    status VARCHAR(20)
);

CREATE TABLE gps_devices
(
    id            SERIAL PRIMARY KEY,
    device_status INT NOT NULL REFERENCES gps_device_states (id)
);

CREATE TABLE gps_data
(
    id        SERIAL PRIMARY KEY,
    device_id INT         NOT NULL REFERENCES gps_devices (id),
    timestamp TIMESTAMPTZ NOT NULL,
    location  POINT       NOT NULL
);

CREATE TABLE vehicles
(
    id            SERIAL PRIMARY KEY,
    gps_device_id INT         NOT NULL UNIQUE REFERENCES gps_devices (id),
    client_id     INT         NOT NULL REFERENCES clients (id),
    license_plate VARCHAR(10) NOT NULL,
    num_alarms    INT         NOT NULL CHECK (num_alarms >= 0)
);

CREATE TABLE green_zones
(
    id              SERIAL PRIMARY KEY,
    vehicle_id      INT              NOT NULL REFERENCES vehicles (id),
    center_location POINT            NOT NULL,
    radius          DOUBLE PRECISION NOT NULL
);

CREATE TABLE drivers
(
    vehicle_id   INT PRIMARY KEY REFERENCES vehicles (id),
    name         VARCHAR(60) NOT NULL,
    phone_number VARCHAR(20) NOT NULL
);

CREATE TABLE alarms
(
    gps_data_id INT PRIMARY KEY REFERENCES gps_data (id)
);

CREATE TABLE unprocessed_gps_data
(
    id        SERIAL PRIMARY KEY,
    timestamp TIMESTAMPTZ,
    location  POINT,
    device_id INT
);

CREATE TABLE invalid_gps_data
(
    id        SERIAL PRIMARY KEY,
    timestamp TIMESTAMPTZ,
    location  POINT,
    device_id INT
)
