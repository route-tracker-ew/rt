CREATE TABLE IF NOT EXISTS account
(
    id             SERIAL PRIMARY KEY,
    first_name     VARCHAR(30),
    last_name      VARCHAR(30),
    phone_number   VARCHAR(30) NOT NULL UNIQUE,
    password       VARCHAR(233),
    is_registered  BOOLEAN DEFAULT FALSE,
    is_gps_tracker BOOLEAN DEFAULT FALSE
);


CREATE TABLE IF NOT EXISTS route
(
    id                                SERIAL PRIMARY KEY,
    source_country                    varchar(30) NOT NULL,
    source_city                       varchar(30) NOT NULL,
    destination_country               varchar(30) NOT NULL,
    destination_city                  varchar(30) NOT NULL,
    day_of_departure_from_source      INT         NOT NULL,
    day_of_departure_from_destination INT         NOT NULL
);

CREATE TABLE IF NOT EXISTS route_owners
(
    id         SERIAL PRIMARY KEY,
    account_id BIGINT NOT NULL,
    route_id   BIGINT NOT NULL,
    CONSTRAINT FK_route_owners_account_id
        FOREIGN KEY (account_id)
            REFERENCES account (id)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT FK_route_owners_route_id
        FOREIGN KEY (route_id)
            REFERENCES route (id)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION

);

CREATE TABLE IF NOT EXISTS car
(
    id              SERIAL PRIMARY KEY,
    brand           VARCHAR(30) NOT NULL,
    model           VARCHAR(30) NOT NULL,
    color           VARCHAR(30),
    number          VARCHAR(30) NOT NULL UNIQUE,
    engine_capacity DECIMAL,
    gps_tracker_id  BIGINT,
    CONSTRAINT FK_car_gps_tracker
        FOREIGN KEY (gps_tracker_id)
            REFERENCES account (id)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
);

CREATE TABLE IF NOT EXISTS route_cars
(
    id       SERIAL PRIMARY KEY,
    car_id   BIGINT NOT NULL,
    route_id BIGINT NOT NULL,
    CONSTRAINT FK_route_cars_car_id
        FOREIGN KEY (car_id)
            REFERENCES car (id)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT FK_route_cars_route_id
        FOREIGN KEY (route_id)
            REFERENCES route (id)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION

);

CREATE TABLE IF NOT EXISTS route_workers
(
    id         SERIAL PRIMARY KEY,
    account_id BIGINT NOT NULL,
    route_id   BIGINT NOT NULL,
    CONSTRAINT FK_route_workers_account_id
        FOREIGN KEY (account_id)
            REFERENCES account (id)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT FK_route_workers_route_id
        FOREIGN KEY (route_id)
            REFERENCES route (id)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION

);
CREATE TABLE IF NOT EXISTS parcel
(
    id                       SERIAL PRIMARY KEY,
    sender_id                BIGINT,
    source_country           VARCHAR(30),
    source_city              VARCHAR(30),
    source_street            VARCHAR(30),
    source_house_number      VARCHAR(30),
    source_flat_number       INT,
    receiver_id              BIGINT      NOT NULL,
    destination_country      VARCHAR(30) NOT NULL,
    destination_city         VARCHAR(30) NOT NULL,
    destination_street       VARCHAR(30),
    destination_house_number VARCHAR(30),
    destination_flat_number  INT,
    estimated_pickup_date    DATE,
    amount                   INT         NOT NULL,
    delivery_service         VARCHAR(30),
    destination_post_number  INT,
    price                    DECIMAL,
    parcel_status            VARCHAR(30) NOT NULL,
    route_id                 BIGINT,
    car_id                   BIGINT,
    request                  boolean default false,
    accept                   BOOLEAN,
    CONSTRAINT FK_parcel_route
        FOREIGN KEY (route_id)
            REFERENCES route (id)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT FK_parcel_sender
        FOREIGN KEY (sender_id)
            REFERENCES account (id)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT FK_parcel_receiver
        FOREIGN KEY (receiver_id)
            REFERENCES account (id)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT FK_parcel_car
        FOREIGN KEY (car_id)
            REFERENCES car (id)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
);

CREATE TABLE IF NOT EXISTS telegram_account
(
    id               SERIAL PRIMARY KEY,
    telegram_chat_id BIGINT NOT NULL unique,
    account_id       BIGINT NOT NULL,
    CONSTRAINT FK_telegram_account_account_id
        FOREIGN KEY (account_id)
            REFERENCES account (id)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION

);