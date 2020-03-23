-- noinspection SqlNoDataSourceInspectionForFile

/*
    DB schema initialization for Census Auth service.

    This file is default schema init file and will be used (by default)
    just for in-memory databases (like H2).

    Created:  Dmitrii Gusev, 22.03.2020
    Modified: Dmitrii Gusev, 23.03.2020
*/

-- users table
create table if not exists auth_user (
    id          int not null primary key auto_increment,
    name        varchar(255) not null,
    description varchar(255),
    username    varchar(30) not null,
    password    varchar(30) not null,
    active      bool default true,                -- user active by default
    createdAt   timestamp not null default now(), -- default create timestamp -> now
    modifiedAt  timestamp on update now()         -- on row update set timestamp -> now
);

-- roles table
create table if not exists auth_role (
    id          identity,
    rolename    varchar(30) not null,
    description varchar(255),
    createdAt   timestamp not null default now(), -- default create timestamp -> now
    modifiedAt  timestamp on update now()         -- on row update set timestamp -> now
);

-- map users to roles table
create table if not exists auth_user_role (
    id         identity,
    user_id    int not null,
    role_id    int not null,
    createdAt  timestamp not null default now(), -- default create timestamp -> now
    modifiedAt timestamp on update now()         -- on row update set timestamp -> now
);

-- foreign keys
alter table auth_user_role add foreign key (user_id) references auth_user(id);
alter table auth_user_role add foreign key (role_id) references auth_role(id);
