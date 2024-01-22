-- noinspection SqlNoDataSourceInspectionForFile

/*
 *****************************************************************
 *                                                               *
 *  DB script for CENSUS system. Script contains two sections:   *
 *   1. DDL - drop/create database and create necessary tables   *
 *   2. DML - add some initial data into database                *
 *                                                               *
 *  Script has been written for MySQL 5.                         *
 *                                                               *
 *  Created:  28.07.2015                                         *
 *  Modified: 11.08.2015                                         *
 *                                                               *
 *****************************************************************
*/

-- DROP USER 'memorandum'@'localhost';
-- DROP USER 'memorandum'@'%';
-- --- create 'memorandum' user ('%' - any host, 'localhost' - localhost - two different users!) ---
-- CREATE USER 'census'@'%' IDENTIFIED BY 'census';
-- CREATE USER 'census'@'localhost' IDENTIFIED BY 'census';

-- drop database, if exists (for clean set up)
DROP DATABASE IF EXISTS census;
-- create database
-- CREATE DATABASE IF NOT EXISTS census CHARACTER SET utf8 COLLATE utf8_general_ci;
CREATE DATABASE IF NOT EXISTS census;

-- grant privileges on 'census' database to 'census' user
-- GRANT ALL ON census.* TO 'census'@'%'         IDENTIFIED BY 'census';
-- GRANT ALL ON census.* TO 'census'@'localhost' IDENTIFIED BY 'census';
-- FLUSH PRIVILEGES;
-- select created database
USE census;

-- ============================ [PERSONNEL] ============================
-- Employees table.
create table employees (
  id            int auto_increment,
  name          varchar(250) not null,
  family        varchar(250),
  patronymic    varchar(250),
  nameEng       varchar(250) default null,
  familyEng     varchar(250) default null,
  patronymicEng varchar(250) default null,
  birthDate     datetime,
  sex           int not null default 0,
  hireDate      datetime default null,
  firDate       datetime default null,
  clockNumber   varchar(50)  default null,
  comment       varchar(200) default null,
  timestamp     timestamp    default now() on update now(),
  updateUser    int          not null default 0,
  deleted       int          default 0,
  primary key(id)
);
create        index employee_name_index        on employees(name);
create        index employee_family_index      on employees(family);
create        index employee_patronymic_index  on employees(patronymic);
-- unique combination for employee identification parameters
create unique index employee_name_unique_index on employees(family, name, patronymic, sex, birthDate);

-- Employees' positions. Position name should be unique.
create table positions (
 id         int auto_increment,
 name       varchar(250) not null,
 weight     int          not null,
 comment    varchar(200) default null,
 timestamp  timestamp    default now() on update now(),
 updateUser int          not null default 0,
 deleted    int          default 0,
 primary key(id)
);
create        index position_name_index        on positions(name);
create unique index position_name_unique_index on positions(name);

-- Departments list. Department name should be unique.
create table departments (
  id          int           auto_increment,
  name        varchar(250)  not null,
  code        varchar(50)   default null,
  description varchar (500) default null,
  leftValue   int           default 0,     -- for storing hierarchical data (departments tree)
  rightValue  int           default 0,     -- for storing hierarchical data (departments tree)
  chiefId     int           default 0,     -- department's chief (link to personnel table) - optional
  comment     varchar(200)  default null,
  timestamp   timestamp     default now() on update now(),
  updateUser  int           not null default 0,
  deleted     int           default 0,
  primary key(id),
  constraint foreign key(chiefId) references employees(id) on update cascade
);
create        index dept_name_index        on departments(name);
create unique index dept_name_unique_index on departments(name);
create        index dept_code_index        on departments(code);

-- Link table for employees and positions. One employee may have more than one position. One position may
-- belong to more than one employee. Relation between employees and positions is MANY-TO-MANY.
-- Pair employee-position should be unique.
create table link_employees_2_positions (
 id         int          not null auto_increment,
 employeeId int          not null,
 positionId int          not null,
 comment    varchar(200) default null,
 timestamp  timestamp    default now() on update now(),
 updateUser int          not null default 0,
 deleted    int          default 0,
 primary key (id),
 constraint foreign key(employeeId) references employees(id) on update cascade,
 constraint foreign key(positionId) references positions(id) on update cascade
);
create unique index emp_2_pos_unique_index on link_employees_2_positions(employeeId, positionId);

-- Link table for employees and departments. One employee may be part of one or more departments. One department
-- contains zero or more employees. Relation between employees and departments is MANY-TO-MANY.
-- Pair employee-department should be unique.
create table link_employees_2_departments (
 id           int          not null auto_increment,
 employeeId   int          not null,
 departmentId int          not null,
 comment      varchar(200) default null,
 timestamp    timestamp    default now() on update now(),
 updateUser   int          not null default 0,
 deleted      int          default 0,
 primary key (id),
 constraint foreign key(employeeId)   references employees(id)   on update cascade,
 constraint foreign key(departmentId) references departments(id) on update cascade
);
create unique index emp_2_dept_unique_index on link_employees_2_departments(employeeId, departmentId);

-- Contacts types. Any possible type is accepted.
create table contactsTypes(
  id            int not null auto_increment,
  contactType   varchar(250) not null, -- contact type value, mandatory, not unique!
  description   varchar(500) default null,
  comment       varchar(200) default null,
  timestamp     timestamp    default now() on update now(),
  updateUser    int          not null default 0,
  deleted       int          default 0,
  primary key (id)
);
create index contacts_types_index on contactsTypes(contactType);

-- Contacts for departments and for employees (stored in one table).
create table contacts(
  id            int not null auto_increment,
  contact       varchar(250) not null, -- contact value, mandatory, not unique!
  description   varchar(500) default null,
  contactTypeId int not null,
  comment       varchar(200) default null,
  timestamp     timestamp    default now() on update now(),
  updateUser    int          not null default 0,
  deleted       int          default 0,
  primary key (id),
  constraint foreign key(contactTypeId) references contactsTypes(id) on update cascade
);
create index contacts_index on contacts(contact);

-- Link table between employees and contacts(one employee-to-many contacts, ONE-TO-MANY). One employee may
-- have multiple contacts, but one contact may be assigned just for one employee. We used here extra table
-- for relation one-to-many, because we need assign contacts not only for employees, but for departments too
-- (maybe for other entities in future).
create table link_employees_2_contacts (
  id         int not null auto_increment,
  contactId  int not null,
  employeeId int not null,
  timestamp  timestamp default now() on update now(),
  updateUser int not null default 0,
  deleted    int default 0,
  constraint foreign key(contactId)  references contacts(id)  on update cascade,
  constraint foreign key(employeeId) references employees(id) on update cascade,
  primary key (id)
);
-- one contact may be assigned just for one employee
create unique index employee_2_contacts_unique_index on link_employees_2_contacts(contactId);

-- Link table between departments and contacts(one department-to-many contacts, ONE-TO-MANY). One department may
-- have multiple contacts, but one contact may be assigned just for one department. We used here extra table
-- for relation one-to-many, because we need assign contacts not only for departments, but for employees too
-- (maybe for other entities in future).
create table link_departments_2_contacts (
  id           int not null auto_increment,
  contactId    int not null,
  departmentId int not null,
  timestamp    timestamp default now() on update now(),
  updateUser   int not null default 0,
  deleted      int default 0,
  constraint foreign key(contactId)    references contacts(id)    on update cascade,
  constraint foreign key(departmentId) references departments(id) on update cascade,
  primary key (id)
);
-- one contact may be assigned just for one employee
create unique index departments_2_contacts_unique_index on link_departments_2_contacts(contactId);

-- ============================ [AUTHENTICATION/AUTHORIZATION] ============================
-- Logic users table (logic user -> pair login/password). Login name should be unique.
create table logicUsers (
  id         int not null auto_increment,
  fullName   varchar (200) not null, -- full user name (as description)
  login      varchar(50)   not null, -- user login, unique
  password   varchar(200)  not null,
  email      varchar(200)  default null, -- optional, we can take email from employee owner
  locked     int not null default 0,     -- 0 -> user active, !=0 -> locked (inactive)
  comment    varchar(200)  default null,
  timestamp  timestamp     default now() on update now(),
  updateUser int           not null default 0,
  deleted    int           default 0,
  primary key (id)
);
create        index logic_users_name_index        on logicUsers(login);
create unique index logic_users_name_unique_index on logicUsers(login);

-- Link table between employees and logic users (one employee-to-many logic users, ONE-TO-MANY). One employee may
-- be assigned with many logic users, but one logic user may be assigned just for one employee. If there is no link
-- for employee or logic user - its ok (this made for flexibility). Separated table made for flexibility - we may
-- use AUTH part without PERSONNEL and vice versa.
create table link_employees_2_logicUsers (
  id          int not null auto_increment,
  logicUserId int not null,
  employeeId  int not null,
  timestamp   timestamp default now() on update now(),
  updateUser  int not null default 0,
  deleted     int default 0,
  constraint foreign key(logicUserId) references logicUsers(id) on update cascade,
  constraint foreign key(employeeId)  references employees(id)  on update cascade,
  primary key (id)
);
-- one logic user may be assigned just for one employee
create unique index emp_2_lu_unique_index on link_employees_2_logicUsers(logicUserId);

-- Logic groups table. Logic groups consists of logic users. Logic group name should be unique.
create table logicGroups (
  id          int not null auto_increment,
  name        varchar(100) not null,
  description varchar(500) default null,
  comment     varchar(200) default null,
  timestamp   timestamp    default now() on update now(),
  updateUser  int          not null default 0,
  deleted     int          default 0,
  primary key (id)
);
create        index logic_groups_name_index        on logicGroups(name);
create unique index logic_groups_name_unique_index on logicGroups(name);

-- Link table for tables logicUsers and logicGroups. Pair userId-groupId should be unique.
create table link_logicUsers_2_logicGroups (
 id           int not null auto_increment,
 logicUserId  int not null,
 logicGroupId int not null,
 comment      varchar(200) default null,
 timestamp    timestamp    default now() on update now(),
 updateUser   int          not null default 0,
 deleted      int          default 0,
 primary key (id),
 constraint foreign key(logicUserId)  references logicUsers(id)  on update cascade,
 constraint foreign key(logicGroupId) references logicGroups(id) on update cascade
);
create unique index logicUsers_2_logicGroups_unique_index on link_logicUsers_2_logicGroups(logicUserId, logicGroupId);

-- ============================ [DOCUMENTATION] ============================
-- Documents types table. Field 'name' should be unique.
create table documentsTypes (
  id                int not null auto_increment,
  name              varchar(100) not null, -- should be unique
  comment           varchar(200) default null,
  timestamp         timestamp    default now() not null,
  updateUser        int          not null default 0,
  deleted           int          default 0,
  primary key (id)
);
create        index docs_types_name_index        on documentsTypes(name);
create unique index docs_types_name_unique_index on documentsTypes(name);

-- Documents table.
create table documents (
  id                  int not null auto_increment,
  parentId            int not null default 0,
  docTypeId           int          not null,
  createDate          datetime     default now(),
  number              varchar(100) default null, -- internal document number (any chars)
  subject             varchar(250) not null,
  sendDate            datetime     default null, -- document send date/time
  executionDate       datetime     default null, -- document execution date/time
  comment             varchar(200) default null,
  timestamp           timestamp    default now() on update now(),
  updateUser          int          not null default 0,
  deleted             int          default 0,
  primary key (id),
  constraint foreign key(docTypeId) references documentsTypes(id) on update cascade
);
create index docs_subject_index on documents(subject);

-- Documents texts. This entity will be stored in different table for performance reasons (text may be very big and
-- record size will grow - it slow down select queries).
create table documentsTexts (
  id         int not null auto_increment,
  documentId int not null,
  text       varchar(3000) not null,
  comment    varchar(200) default null,
  timestamp  timestamp    default now() on update now(),
  updateUser int          not null default 0,
  deleted    int          default 0,
  primary key (id),
  constraint foreign key(documentId) references documents(id) on update cascade
);

-- Link table between employees and documents (many employees-to-many documents, MANY-TO-MANY).
-- One document may be assigned to many employees and vice versa.
create table link_documents_2_employees (
  id          int not null auto_increment,
  documentId  int not null,
  employeeId  int not null,
  timestamp   timestamp default now() on update now(),
  updateUser  int not null default 0,
  deleted     int default 0,
  constraint foreign key(documentId) references documents(id) on update cascade,
  constraint foreign key(employeeId) references employees(id) on update cascade,
  primary key (id)
);

-- Link table between departments and documents (many departemnts-to-many documents, MANY-TO-MANY).
-- One document may be assigned to many departemnts and vice versa.
create table link_documents_2_departments (
  id           int not null auto_increment,
  documentId   int not null,
  departmentId int not null,
  timestamp    timestamp default now() on update now(),
  updateUser   int not null default 0,
  deleted      int default 0,
  constraint foreign key(documentId)   references documents(id)   on update cascade,
  constraint foreign key(departmentId) references departments(id) on update cascade,
  primary key (id)
);

-- Uploaded files table.
create table files (
  id         int not null auto_increment,
  storedName varchar(100) not null,  -- should be unique
  realName   varchar(100) not null,
  comment    varchar(200) default null,
  timestamp  timestamp    default now() on update now(),
  updateUser int          not null default 0,
  deleted    int          default 0,
  primary key (id)
);
create        index files_stored_names_index        on files(storedName);
create unique index files_stored_names_unique_index on files(storedName);
create        index files_real_names_index          on files(realName);

-- Link table between documents and files (one document-to-many files, ONE-TO-MANY). One document may
-- be assigned with many files, but one file may be assigned just for one document.
create table link_documents_2_files (
  id          int not null auto_increment,
  documentId  int not null,
  fileId      int not null,
  timestamp   timestamp default now() on update now(),
  updateUser  int not null default 0,
  deleted     int default 0,
  constraint foreign key(documentId) references documents(id) on update cascade,
  constraint foreign key(fileId)  references files(id)  on update cascade,
  primary key (id)
);
-- one file may be assigned just for one document
create unique index doc_2_files_unique_index on link_documents_2_files(fileId);

-- Initial data for [EMPLOYEES]
insert into employees (id, family, name, patronymic, sex) values(1,  'Кошкин',   'Игорь',   'Игоревич',   0);
insert into employees (id, family, name, patronymic, sex) values(2,  'Мышкин',   'Денис',   'Денисович',  0);
insert into employees (id, family, name, patronymic, sex) values(3,  'Иванов',   'Сергей',  'Сергеевич',  0);
insert into employees (id, family, name, patronymic, sex) values(4,  'Петрова',  'Мария',   'Ивановна',   1);
insert into employees (id, family, name, patronymic, sex) values(5,  'Петров',   'Иван',    'Иванович',   0);
insert into employees (id, family, name, patronymic, sex) values(6,  'Сидоров',  'Петр',    'Петрович',   0);
insert into employees (id, family, name, patronymic, sex) values(7,  'Козлов',   'Борис',   'Борисович',  0);
insert into employees (id, family, name, patronymic, sex) values(8,  'Гусев',    'Дмитрий', 'Эдуардович', 0);
insert into employees (id, family, name, patronymic, sex) values(9,  'Сергеева', 'Мария',   'Михайловна', 1);
insert into employees (id, family, name, patronymic, sex) values(10, 'Денисов',  'Виктор',  'Викторович', 0);
insert into employees (id, family, name, patronymic, sex) values(11, 'Зверев',   'Алексей', 'Алексеевич', 0);

-- Initial data for [POSITIONS]
insert into positions(id, name, weight, comment, updateUser) values(1,  'Директор',                            800, 'Самая большая должность компании.', 1);
insert into positions(id, name, weight, comment, updateUser) values(2,  'Заместитель генерального директора.', 700, 'Вторая должность компании.', 1);
insert into positions(id, name, weight, comment, updateUser) values(3,  'Начальник административной службы.',  600, 'Должность компании.', 1);
insert into positions(id, name, weight, comment, updateUser) values(4,  'Начальник службы IT.',                600, 'Должность компании.', 1);
insert into positions(id, name, weight, comment, updateUser) values(5,  'Начальник отдела АХО.',               500, 'Должность компании.', 1);
insert into positions(id, name, weight, comment, updateUser) values(6,  'Начальник отдела IT.',                500, 'Должность компании.', 1);
insert into positions(id, name, weight, comment, updateUser) values(7,  'Начальник отдела.',                   500, 'Должность компании.', 1);
insert into positions(id, name, weight, comment, updateUser) values(8,  'Главный инженер.',                    400, 'Должность компании.', 1);
insert into positions(id, name, weight, comment, updateUser) values(9,  'Ведущий инженер.',                    300, 'Должность компании.', 1);
insert into positions(id, name, weight, comment, updateUser) values(10, 'Инженер.',                            200, 'Должность компании.', 1);
insert into positions(id, name, weight, comment, updateUser) values(11, 'Делопроизводитель.',                  100, 'Должность компании.', 1);
insert into positions(id, name, weight, comment, updateUser) values(12, 'Секретарь.',                          100, 'Должность компании.', 1);
insert into positions(id, name, weight, comment, updateUser) values(13, 'Системный администратор',             10,  'Сисадмин в конторе.', 1);
insert into positions(id, name, weight, comment, updateUser) values(14, 'Специалист',                          350, '', 1);
insert into positions(id, name, weight, comment, updateUser) values(15, 'Программист',                         250, '', 1);
insert into positions(id, name, weight, comment, updateUser) values(16, 'Менеджер по продажам',                300, '', 1);

-- Initial data for [DEPARTMENTS]
insert into departments(id, name, code, chiefId) values(1, 'Топ-менеджмент', 1, 1);              -- chief: Директор (Кошкин)
insert into departments(id, name, code, chiefId) values(2, 'Отдел документооборота', 2, 3);      -- chief: Нач. отдела (Иванов)
insert into departments(id, name, code, chiefId) values(3, 'Отдел технической поддержки', 3, 5); -- chief: Нач. отдела (Петров)
insert into departments(id, name, code, chiefId) values(4, 'Отдел продаж', 4, 9);                -- chief: Нач. отдела (Сергеева)

-- Initial data for [EMPLOYEES_2_POSITIONS]
insert into link_employees_2_positions(id, employeeId, positionId) values(1,  1,  1);
insert into link_employees_2_positions(id, employeeId, positionId) values(2,  2,  2);
insert into link_employees_2_positions(id, employeeId, positionId) values(3,  3,  7);
insert into link_employees_2_positions(id, employeeId, positionId) values(4,  4,  11);
insert into link_employees_2_positions(id, employeeId, positionId) values(5,  5,  7);
insert into link_employees_2_positions(id, employeeId, positionId) values(6,  6,  14);
insert into link_employees_2_positions(id, employeeId, positionId) values(7,  7,  15);
-- two positions for one employee (Гусев Дмитрий)
insert into link_employees_2_positions(id, employeeId, positionId) values(8,  8,  13);
insert into link_employees_2_positions(id, employeeId, positionId) values(9,  8,  4);
insert into link_employees_2_positions(id, employeeId, positionId) values(10,  9,  7);
insert into link_employees_2_positions(id, employeeId, positionId) values(11, 10, 16);
insert into link_employees_2_positions(id, employeeId, positionId) values(12, 11, 16);

-- Initial data for [EMPLOYEES_2_DEPARTMENTS]
insert into link_employees_2_departments(id, employeeId, departmentId) values (1,  1,  1);
insert into link_employees_2_departments(id, employeeId, departmentId) values (2,  2,  1);
insert into link_employees_2_departments(id, employeeId, departmentId) values (3,  3,  2);
insert into link_employees_2_departments(id, employeeId, departmentId) values (4,  4,  2);
insert into link_employees_2_departments(id, employeeId, departmentId) values (5,  5,  3);
insert into link_employees_2_departments(id, employeeId, departmentId) values (6,  6,  3);
insert into link_employees_2_departments(id, employeeId, departmentId) values (7,  7,  3);
insert into link_employees_2_departments(id, employeeId, departmentId) values (8,  8,  3);
insert into link_employees_2_departments(id, employeeId, departmentId) values (9,  9,  4);
insert into link_employees_2_departments(id, employeeId, departmentId) values (10, 10, 4);
insert into link_employees_2_departments(id, employeeId, departmentId) values (11, 11, 4);

-- Initial data for [CONTACTSTYPES]
insert into contactsTypes(id, contactType, description) values (1,  'Phone',         'Телефон.');
insert into contactsTypes(id, contactType, description) values (2,  'Email',         'Адрес электронной почты.');
insert into contactsTypes(id, contactType, description) values (3,  'Skype',         'Логин Skype.');
insert into contactsTypes(id, contactType, description) values (4,  'QIP',           'Логин QIP.');
insert into contactsTypes(id, contactType, description) values (5,  'Jabber',        'Логин Jabber.');
insert into contactsTypes(id, contactType, description) values (6,  'ICQ',           'Логин ICQ.');
insert into contactsTypes(id, contactType, description) values (7,  'Facebook',      'Адрес страницы Facebook.');
insert into contactsTypes(id, contactType, description) values (8,  'LinkedIn',      'Адрес страницы LinkedIn.');
insert into contactsTypes(id, contactType, description) values (9,  'ВКонтакте',     'Адрес страницы ВКонтакте.');
insert into contactsTypes(id, contactType, description) values (10, 'Одноклассники', 'Адрес страницы Одноклассники.');
insert into contactsTypes(id, contactType, description) values (11, 'Другое',        'Другой тип контакта.');

-- Initial data for [CONTACTS]
-- phones
insert into contacts(id, contact, contactTypeId) values (1,  '(812) 333-44-55', 1);
insert into contacts(id, contact, contactTypeId) values (2,  '+7 921 777 99 88', 1);
insert into contacts(id, contact, contactTypeId) values (3,  '75 (12) - 3218', 1);
-- emails
insert into contacts(id, contact, contactTypeId) values (4,  'employee@mydomain.com', 2);
insert into contacts(id, contact, contactTypeId) values (5,  'it@zz.com', 2);
insert into contacts(id, contact, contactTypeId) values (6,  'free.email@freedomain.org', 2);
insert into contacts(id, contact, contactTypeId) values (7,  'support@my.ru', 2);
insert into contacts(id, contact, contactTypeId) values (8,  'elvis12@spb.ru', 2);
-- skype logins
insert into contacts(id, contact, contactTypeId) values (9,  'zzz.sky', 3);
insert into contacts(id, contact, contactTypeId) values (10, 'support_guy', 3);
insert into contacts(id, contact, contactTypeId) values (11, 'documents.dept', 3);

-- Initial data for [EMPLOYEES_2_CONTACTS]
insert into link_employees_2_contacts(id, contactId, employeeId) values (1, 2, 8); -- Гусев Дмитрий (IT)
insert into link_employees_2_contacts(id, contactId, employeeId) values (2, 5, 8); -- Гусев Дмитрий (IT)
insert into link_employees_2_contacts(id, contactId, employeeId) values (3, 1, 1); -- Кошкин Игорь (CEO)

-- Initial data for [DEPARTMENTS_2_CONTACTS]
insert into link_departments_2_contacts(id, contactId, departmentId) values (4, 7, 3); -- IT dept
insert into link_departments_2_contacts(id, contactId, departmentId) values (5, 9, 2); -- DOCS dept
insert into link_departments_2_contacts(id, contactId, departmentId) values (6, 8, 1); -- TOP management

-- Initial data for [LOGICUSERS]
insert into logicUsers (id, fullName, login, password) values (1,  'sys admin',   'admin',     'admin');
insert into logicUsers (id, fullName, login, password) values (2,  'big boss',    'ceo',       'ceo');
insert into logicUsers (id, fullName, login, password) values (3,  'tech boss',   'cto',       'cto');
insert into logicUsers (id, fullName, login, password) values (4,  'papers boss', 'docchief',  'docchief');
insert into logicUsers (id, fullName, login, password) values (5,  'papers worm', 'docs',      'docs');
insert into logicUsers (id, fullName, login, password) values (6,  'it crowd',    'itchief',   'itchief');
insert into logicUsers (id, fullName, login, password) values (7,  'it-1',        'it1',       'it1');
insert into logicUsers (id, fullName, login, password) values (8,  'it-2',        'it2',       'it2');
insert into logicUsers (id, fullName, login, password) values (9,  'sales',       'salechief', 'salechief');
insert into logicUsers (id, fullName, login, password) values (10, 'salesman1',   'sales1',    'sales1');
insert into logicUsers (id, fullName, login, password) values (11, 'salesman2',   'sales2',    'sales2');

-- Initial data for [EMPLOYEES_2_LOGICUSERS]
insert into link_employees_2_logicUsers (logicUserId, employeeId) values (1, 8);
insert into link_employees_2_logicUsers (logicUserId, employeeId) values (2, 1);
insert into link_employees_2_logicUsers (logicUserId, employeeId) values (3, 2);
insert into link_employees_2_logicUsers (logicUserId, employeeId) values (4, 3);
insert into link_employees_2_logicUsers (logicUserId, employeeId) values (5, 4);
insert into link_employees_2_logicUsers (logicUserId, employeeId) values (6, 5);
insert into link_employees_2_logicUsers (logicUserId, employeeId) values (7, 6);
insert into link_employees_2_logicUsers (logicUserId, employeeId) values (8, 7);
insert into link_employees_2_logicUsers (logicUserId, employeeId) values (9, 9);
insert into link_employees_2_logicUsers (logicUserId, employeeId) values (10, 10);
insert into link_employees_2_logicUsers (logicUserId, employeeId) values (11, 11);

-- Initial data for [LOGICGROUPS]
insert into logicGroups (id, name, description) values (1, 'memo_user',        'Роль пользователя служебок - доступ с правами "просмотр".');
insert into logicGroups (id, name, description) values (2, 'memo_editor',      'Роль (группа) "РЕДАКТОР" - доступ для редактирования.');
insert into logicGroups (id, name, description) values (3, 'memo_sender',      'Роль (группа) "ОТПРАВИТЕЛЬ" - доступ для отправления.');
insert into logicGroups (id, name, description) values (4, 'memo_chief',       'Роль (группа) "НАЧАЛЬНИК" - доступ уровня начальника подразделения.');
insert into logicGroups (id, name, description) values (5, 'memo_system',      'Администраторы задачи - системная (служебная) роль.');
insert into logicGroups (id, name, description) values (6, 'memo_appointment', 'Роль (группа) "НАЧАЛЬНИК", входящие в эту группу могут поручать.');
insert into logicGroups (id, name, description) values (7, 'memo_superuser',   'Для пользователей с такой ролью, появляется ряд доп. возможностей :)');

-- Initial data for [LOGICUSERS_2_LOGICGROUPS]
-- rights for 'admin' user
insert into link_logicUsers_2_logicGroups (logicUserId, logicGroupId, comment) values(1, 1, 'memo_user for admin'); -- for 'admin'
insert into link_logicUsers_2_logicGroups (logicUserId, logicGroupId, comment) values(1, 2, 'memo_editor for admin');
insert into link_logicUsers_2_logicGroups (logicUserId, logicGroupId, comment) values(1, 3, 'memo_sender for admin');
insert into link_logicUsers_2_logicGroups (logicUserId, logicGroupId, comment) values(1, 4, 'memo_chief for admin');
insert into link_logicUsers_2_logicGroups (logicUserId, logicGroupId, comment) values(1, 5, 'memo_system for admin');
insert into link_logicUsers_2_logicGroups (logicUserId, logicGroupId, comment) values(1, 6, 'memo_appointment for admin');
insert into link_logicUsers_2_logicGroups (logicUserId, logicGroupId, comment) values(1, 7, 'memo_superuser for admin');
-- rights for 'ceo' user
insert into link_logicUsers_2_logicGroups (logicUserId, logicGroupId, comment) values(2, 1, 'memo_user for ceo'); -- for 'ceo'
insert into link_logicUsers_2_logicGroups (logicUserId, logicGroupId, comment) values(2, 2, 'memo_editor for ceo');
insert into link_logicUsers_2_logicGroups (logicUserId, logicGroupId, comment) values(2, 3, 'memo_sender for ceo');
insert into link_logicUsers_2_logicGroups (logicUserId, logicGroupId, comment) values(2, 4, 'memo_chief for ceo');
insert into link_logicUsers_2_logicGroups (logicUserId, logicGroupId, comment) values(2, 6, 'memo_appointment for ceo');
-- rights for 'docchief' user
insert into link_logicUsers_2_logicGroups (logicUserId, logicGroupId, comment) values(4, 1, 'memo_user for docchief'); -- for 'docchief'
insert into link_logicUsers_2_logicGroups (logicUserId, logicGroupId, comment) values(4, 2, 'memo_editor for docchief');
insert into link_logicUsers_2_logicGroups (logicUserId, logicGroupId, comment) values(4, 3, 'memo_sender for docchief');
insert into link_logicUsers_2_logicGroups (logicUserId, logicGroupId, comment) values(4, 4, 'memo_chief for docchief');
insert into link_logicUsers_2_logicGroups (logicUserId, logicGroupId, comment) values(4, 6, 'memo_appointment for docchief');
-- rights for 'docs' user
insert into link_logicUsers_2_logicGroups (logicUserId, logicGroupId, comment) values(5, 1, 'memo_user for docs'); -- for 'docs'
insert into link_logicUsers_2_logicGroups (logicUserId, logicGroupId, comment) values(5, 2, 'memo_editor for docs');

-- Initial data for [DOCUMENTSTYPES]
insert into documentsTypes(id, name, comment) values(1, 'Входящий документ',  'Внешний входящий документ.');
insert into documentsTypes(id, name, comment) values(2, 'Исходящий документ', 'Внешний исходящий документ.');
insert into documentsTypes(id, name, comment) values(3, 'Служебная записка',  'Внутренний документ.');
insert into documentsTypes(id, name, comment) values(4, 'Поручение',          'Внутренний документ.');
insert into documentsTypes(id, name, comment) values(5, 'Ответ на поручение', 'Внутренний документ.');

-- Initial data for [DOCUMENTS]
insert into documents(id, parentId, docTypeId, number, subject, comment)
 values (1, 0, 3, 'Company-СЗ-1/1', 'Первая служебка в системе.', 'нужна для проверки/теста');

-- Initial data for [DOCUMENTSTEXTS]
insert into documentsTexts(id, documentId, text, comment)
 values (1, 1, '', 'text of first document');

-- Initial data for [FILES]
-- Initial data for [DOCUMENTS_2_EMPLOYEES]
-- Initial data for [DOCUMENTS_2_DEPARTMENTS]