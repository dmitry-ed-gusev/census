-- noinspection SqlNoDataSourceInspectionForFile

/*
    DB schema initialization for Census Auth service.

    This file is default schema init file and will be used (by default)
    just for in-memory databases (like H2) for development.

    DB should not contain any business-logic, except timestamps updated.

    Created:  Dmitrii Gusev, 22.03.2020
    Modified: Dmitrii Gusev, 31.03.2020
*/

-- Sequence for unique id's. Start from 10 - we have several pre-inserted users and roles.
--create sequence if not exists auth_sequence start with 10;

-- users table
create table if not exists auth_user (
    id          identity not null primary key,
    name        varchar(255) not null,
    description varchar(255),
    username    varchar(30) not null unique,
    password    varchar(30) not null,
    active      bool default true,
    createdAt   timestamp not null default CURRENT_TIMESTAMP(),
    modifiedAt  timestamp on update CURRENT_TIMESTAMP()
);

-- roles table
create table if not exists auth_role (
    id          identity not null primary key,
    rolename    varchar(30) not null unique,
    description varchar(255),
    createdAt   timestamp not null default CURRENT_TIMESTAMP(),
    modifiedAt  timestamp on update CURRENT_TIMESTAMP()
);

-- map users to roles table
create table if not exists auth_user_role (
    id         identity not null primary key,
    user_id    int not null,
    role_id    int not null,
    createdAt  timestamp not null default CURRENT_TIMESTAMP(),
    modifiedAt timestamp on update CURRENT_TIMESTAMP()
);

-- foreign keys
alter table auth_user_role add foreign key (user_id) references auth_user(id);
alter table auth_user_role add foreign key (role_id) references auth_role(id);
