# Smart Road Assist API Design

## Architecture

Services:

* dispatcher (API Gateway + Auth + Authorization)
* user-service
* request-service
* mechanic-service
* payment-service
* notification-service

Dispatcher is the single entry point.

All requests go through dispatcher.

Dispatcher responsibilities:

* routing
* authentication
* authorization
* logging
* error handling
* token validation

Authorization data stored in MongoDB.

Roles:

ADMIN
USER
MECHANIC
DISPATCHER

---

## DISPATCHER

Routes

/users/** -> user-service
/requests/** -> request-service
/mechanics/** -> mechanic-service
/payments/** -> payment-service
/notifications/** -> notification-service

Auth endpoints

POST /login
POST /register
GET /me

Auth schema

{
id,
email,
password,
role,
token,
createdAt
}

Dispatcher checks role before forwarding request.

---

## USER SERVICE

Base path

/users

Endpoints

POST /users
GET /users
GET /users/{id}
PUT /users/{id}
DELETE /users/{id}

Schema

{
id,
authId,
username,
phone,
address,
createdAt
}

---

## REQUEST SERVICE

Base path

/requests

Endpoints

POST /requests
GET /requests
GET /requests/{id}
PUT /requests/{id}
DELETE /requests/{id}

PUT /requests/{id}/assign
PUT /requests/{id}/status

Schema

{
id,
userId,
type,
description,
location,
status,
mechanicId,
createdAt
}

Type

FLAT_TIRE
BATTERY
ENGINE
FUEL
LOCKED
TOW
OTHER

Status

CREATED
ASSIGNED
IN_PROGRESS
DONE
CANCELLED

---

## MECHANIC SERVICE

Base path

/mechanics

Endpoints

POST /mechanics
GET /mechanics
GET /mechanics/{id}
PUT /mechanics/{id}
DELETE /mechanics/{id}

PUT /mechanics/{id}/status
PUT /mechanics/{id}/location

Mongo schema

{
id,
name,
phone,
active,
createdAt
}

Redis status

{
status,
lat,
lng
}

Status

FREE
BUSY
OFFLINE

---

## PAYMENT SERVICE

Base path

/payments

Endpoints

POST /payments
GET /payments
GET /payments/{id}
PUT /payments/{id}
DELETE /payments/{id}

Schema

{
id,
userId,
requestId,
amount,
status,
createdAt
}

Status

PENDING
PAID
FAILED

---

## NOTIFICATION SERVICE

Base path

/notifications

Endpoints

POST /notifications
GET /notifications
GET /notifications/{id}
DELETE /notifications/{id}

Schema

{
id,
userId,
message,
type,
createdAt
}

Type

EMAIL
SMS
SYSTEM

---

## RMM Level 3

Resources

users
requests
mechanics
payments
notifications

Each resource supports

GET
POST
PUT
DELETE

Correct HTTP status codes used

200 OK
201 CREATED
400 BAD REQUEST
401 UNAUTHORIZED
403 FORBIDDEN
404 NOT FOUND
500 SERVER ERROR

Responses may contain links to related resources.
