-- noinspection SqlResolveForFile
-- noinspection SqlNoDataSourceInspectionForFile

/*
    DB data initialization for Census Auth service.

    This file is default file for initialize data for in-memory
    DBs (like H2).

    Created:  Dmitrii Gusev, 22.03.2020
    Modified: Dmitrii Gusev, 31.03.2020
*/

-- adding some users
insert into auth_user(id, name, description, username, password) values (1, 'Test user', 'Test system user', 'test', 'test');
insert into auth_user(id, name, description, username, password) values (2, 'Dmitrii Gusev', 'Fisrt system user', 'dgusev', '123456');

-- adding some roles
insert into auth_role(id, rolename, description) values (1, 'USER', 'System user without special privileges.');
insert into auth_role(id, rolename, description) values (2, 'ADMIN', 'System admin. Privileged user.');

-- adding some mappings users <-> roles
insert into auth_user_role(id, user_id, role_id) values(1, 1, 1); -- Test user -> user
insert into auth_user_role(id, user_id, role_id) values(2, 2, 1); -- Dmitrii -> user
insert into auth_user_role(id, user_id, role_id) values(3, 2, 2); -- Dmitrii -> admin
