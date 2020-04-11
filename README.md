# Census simple ERP system, (C) 2010-2020 #

## How to build project ##
TBD

## Project modules ##
* **[census-auth]** Simple authorization microservice. Works via REST over http/https protocol.
* **[census-auth-ui]** UI for authorization microservice for managing permissions. Works as a client for [census-auth] microservice.
* **[census-service1]** TBD
* **[census-service2]** TBD
* **[census-service3]** TBD

## Detailed modules descriptions ##

### [census-auth] ###
Simple authorization microservice.
Base service URI: **host:port/census/api/auth/xxx**

#### REST interface description ####
| HTTP method |  URI          | Return                | Description    |
| ----------- | --------------| --------------------- |--------------- |
| GET         |  /users       | 200 (OK)              | get list of all users |
| POST        |  /users       | 201 (OK) / 500 (fail) | save new user |
| GET         |  /users/{id}  | 200 (OK) / 404 (fail) | find a user where id = {:id} |
| PUT         |  /users/{id}  | 200 (OK) / 500 (fail) | update a user where id = {:id}, or save it |
| PATCH       |  /users/{id}  | 200 (OK) / 404 (fail) | update a single field where id = {:id} |
| DELETE      |  /users/{id}  | 204 (OK / fail)       | delete a user where id = {:id}, idempotent |
| ----------- | --------------| --------------------- | ----------- |
| GET         |  /roles       | 200 (OK)              | get list of all roles |
| POST        |  /roles       | 201 (OK) / 500 (fail) | save new role |
| GET         |  /roles/{id}  | 200 (OK) / 404 (fail) | find a role where id = {:id} |
| PUT         |  /roles/{id}  | 200 (OK) / 500 (fail) | update a role where id = {:id}, or save it |
| PATCH       |  /roles/{id}  | 200 (OK) / 404 (fail) | update a single field where id = {:id} |
| DELETE      |  /roles/{id}  | 204 (OK / fail)       | delete a role where id = {:id}, idempotent |


### [census-auth-ui] ###
UI for authorization microservice for managing permissions.
Base service URI: **host:port/census/auth-ui/**

### [census-service1] ###
TBD

### [census-service2] ###
TBD

### [census-service3] ###
TBD