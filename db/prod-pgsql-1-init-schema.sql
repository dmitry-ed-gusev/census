-- noinspection SqlNoDataSourceInspectionForFile

/*
    Census DB: PostgreSql schema initialization script.

    Created:  Dmitrii Gusev, 29.03.2020
    Modified: Dmitrii Gusev, 31.03.2020
*/

-- common DB objects

-- update modifiedAt field for specified table, used in triggers for tables
CREATE OR REPLACE FUNCTION update_timestamp() RETURNS TRIGGER AS
$$
BEGIN
    NEW.modifiedAt = now();
    RETURN NEW;
END;
$$ language 'plpgsql';

-- ------------------------------------------------------------------------------------------------

-- schemas for different services
create schema CENSUS_AUTH_SCHEMA;
create schema CENSUS_DOCS_SCHEMA;
create schema CENSUS_PERSONNEL_SCHEMA;

-- ------------------------------------------------------------------------------------------------

-- tables and other objects for CENSUS_AUTH_SCHEMA
-- create sequence CENSUS_AUTH_SCHEMA.auth_sequence start 10;

-- users table
create table if not exists CENSUS_AUTH_SCHEMA.auth_user (
    id          serial primary key,
    name        varchar(255) not null,
    description varchar(255),
    username    varchar(30)  not null unique,
    password    varchar(30)  not null,
    active      boolean               default true,  -- user is active by default
    createdAt   timestamp    not null default now(), -- default value on insert is current timestamp
    modifiedAt  timestamp
);
CREATE TRIGGER auth_user_timestamp_update BEFORE UPDATE
    ON CENSUS_AUTH_SCHEMA.auth_user FOR EACH ROW EXECUTE PROCEDURE update_timestamp();

-- roles table
create table if not exists CENSUS_AUTH_SCHEMA.auth_role (
    id          serial primary key,
    rolename    varchar(30) not null unique,
    description varchar(255),
    createdAt   timestamp   not null default now(),
    modifiedAt  timestamp
);
CREATE TRIGGER auth_role_timestamp_update BEFORE UPDATE
    ON CENSUS_AUTH_SCHEMA.auth_role FOR EACH ROW EXECUTE PROCEDURE update_timestamp();

-- map users to roles table
create table if not exists CENSUS_AUTH_SCHEMA.auth_user_role (
    id         serial primary key,
    user_id    int       not null,
    role_id    int       not null,
    createdAt  timestamp not null default now(),
    modifiedAt timestamp
);
CREATE TRIGGER auth_user_role_timestamp_update BEFORE UPDATE
    ON CENSUS_AUTH_SCHEMA.auth_user_role FOR EACH ROW EXECUTE PROCEDURE update_timestamp();

-- foreign keys
alter table CENSUS_AUTH_SCHEMA.auth_user_role add foreign key (user_id) references CENSUS_AUTH_SCHEMA.auth_user (id);
alter table CENSUS_AUTH_SCHEMA.auth_user_role add foreign key (role_id) references CENSUS_AUTH_SCHEMA.auth_role (id);

-- ------------------------------------------------------------------------------------------------

-- tables and other objects for CENSUS_DOCS_SCHEMA
-- todo: TBD

-- ------------------------------------------------------------------------------------------------

-- tables and other objects for CENSUS_PERSONNEL_SCHEMA
-- todo: TBD
