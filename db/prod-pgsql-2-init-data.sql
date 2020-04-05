-- noinspection SqlNoDataSourceInspectionForFile
-- noinspection SqlResolveForFile

/*
    SQL script for adding some initial data into Postgres DBMS.

    Created:  Dmitrii Gusev, 29.03.2020
    Modified: Dmitrii Gusev, 04.04.2020
*/

-- adding some users
insert into CENSUS_AUTH_SCHEMA.auth_user(id, name, description, username, password)
    overriding system value values (1, 'Test user', 'Test system user', 'test', 'test');
insert into CENSUS_AUTH_SCHEMA.auth_user(id, name, description, username, password)
    overriding system value values (2, 'Dmitrii Gusev', 'Fisrt system user', 'dgusev', '123456');

-- adding some roles
insert into CENSUS_AUTH_SCHEMA.auth_role(id, rolename, description)
    overriding system value values (1, 'USER', 'System user without special privileges.');
insert into CENSUS_AUTH_SCHEMA.auth_role(id, rolename, description)
    overriding system value values (2, 'ADMIN', 'System admin. Privileged user.');

-- adding some mappings users <-> roles
insert into CENSUS_AUTH_SCHEMA.auth_user_role(user_id, role_id) values(1, 1); -- Test user -> user
insert into CENSUS_AUTH_SCHEMA.auth_user_role(user_id, role_id) values(2, 1); -- Dmitrii -> user
insert into CENSUS_AUTH_SCHEMA.auth_user_role(user_id, role_id) values(2, 2); -- Dmitrii -> admin
