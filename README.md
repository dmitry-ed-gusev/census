# Census simple ERP system, (C) 2010-2020 #

## How to build project ##
TBD

## Project modules ##
* **[census-auth]** Simple authorization microservice. Works via REST over http/https protocol.
* **[census-service1]**
* **[census-service2]**
* **[census-service3]**

## Detailed modules descriptions ##

### [census-auth] ###
Simple authorization microservice.
Base service URI: **host:port/census/auth/xxx**

#### REST interface description ####
| HTTP method |  URI           | Description |
| ----------- | -------------- | ----------- |
| GET         |   /users       | get list of all users |
| POST        |   /users       | save new user |
| GET         |   /users/{id}  | find a user where id = {:id} |
| PUT         |   /users/{id}  | update a user where id = {:id}, or save it |
| PATCH       |   /users/{id}  | update a single field where id = {:id} |
| DELETE      |   /users/{id}  | delete a user where id = {:id}, idempotent |
| GET         |   /roles       | get list of all roles |
| POST        |   /roles       | save new role |
| GET         |   /roles/{id}  | find a role where id = {:id} |
| PUT         |   /roles/{id}  | update a role where id = {:id}, or save it |
| PATCH       |   /roles/{id}  | update a single field where id = {:id} |
| DELETE      |   /roles/{id}  | delete a role where id = {:id}, idempotent |

### [census-service1] ###
TBD

### [census-service2] ###
TBD

### [census-service3] ###
TBD