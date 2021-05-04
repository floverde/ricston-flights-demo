# Ricston Flights Demo - Flight Service API

This is a demonstration project commissioned by Ricston Ltd.

This project consists of a web service with a REST interface for flight management.

1. Requirements
2. Database schema
3. REST endpoints
4. Class Diagram

### 1. Requirements
Let's imagine that a company, Acme Travel, wants to launch its own mobile app that allows the following operations:

1. Retrieve a list of available flights on a given date
2. Retrieve a paginated list of destinations (country, city, airport name) for a given airline (10 results per page)
3. Simulate a ticket purchase by changing the seats available for a specified flight
4. Simulate new flight availability by adding a new flight for a specified route
5. Simulate change in flight price by increasing/decreasing the ticket price for a
specified flight.

This company already has an internal infrastructure that it uses to manage flights, so the application will have to use the same policies for accessing the common database.



### 2. Database schema
Before going into the details of the web service, it is good to provide an overview of the application domain and the entities involved. Since this is a REST API for managing **flights**, they are certainly one of the main entities. A flight is identified by its *code* and *departure date*. Each flight follows a predetermined **route**, decided in advance by air traffic controllers, and cannot deviate from it. A route may be flown by zero or more flights (*zero-to-many* relation). A flight can only exist if there is a predetermined route, and if this fails all flights on that route will be cancelled (*membership* relation). This UML diagram summarises the above:

![flight-route-relationship](https://github.com/floverde/ricston-flights-demo/blob/master/doc/flight-route-relationship.svg)

Each route is defined (and identified) by the *departure* and *destination* **airports** and by the **airline** that plotted it. Again, both between airports and routes and between airlines and routes there is *zero-to-many membership* relationship. In fact, since the existence of a route depends both on the existence of the airline that traced it and on the existence of the airports it connects, the cancellation of one of these implies the cancellation of the route itself.

![route-relationships](https://github.com/floverde/ricston-flights-demo/blob/master/doc/route-relationships.svg)

These concepts are modelled by the tables that make up the database in this way:

​											![acme-travel-db](https://github.com/floverde/ricston-flights-demo/blob/master/doc/acme-travel-db.svg)



At first glance, we realise that there are some issues arising from the database schema:

1. No foreign key constraints have been defined between the flight table and the route table (no reference to the `aircraft_type` table).
2. There is a problem of data redundancy, both between columns in the route table and duplication of information within the flight table. It would be possible to reference the route directly instead of duplicating information such as: *Airline_Name, Departure_Airport, Destination_Airport*.
3. Lack of a primary key in `aircraft_type` table.

These issues were therefore taken into account during the development of the web service.



### 3. REST endpoints

Let's focus more on the most important part of the web service, which is its REST interface.

For each we will provide a full table of specifications, an explanation of the design, and provide an example of usage.

- Find all available flights for a certain date
  - `HTTP GET /flights?date=yyyy-MM-dd`
- Find all destinations reached by a given airline (paginated results)
  - `HTTP GET /airlines/{id}/destinations?page=n`
- Create a new flight
  - `HTTP POST /flights`
- Purchase an airline ticket for a given flight
  - `HTTP POST /flights/{code}/ticket?departure-date=yyyy-MM-dd'T'HH:mm`
- Increasing/decreasing the price for a  specified flight
  - `HTTP PATCH /flights/{code}/price?departure-date=yyyy-MM-dd'T'HH:mm`

> **Note**: To test the service you can use any REST client, or alternatively, you can use the built-in automatically generated Swagger client. If the service is started in localhost, you can access the Swagger client at this address: http://localhost:8080/swagger-ui.html

### 3.1 "HTTP GET /flights"

**Description**: Find all available flights for a certain date.

| HTTP Verb | Path     |
| --------- | -------- |
| GET       | /flights |

Since a search is a request for resources (where the resources are flights), the use of **GET** and the path **/flights** has been chosen.

**Query parameters**:

| Name | Type                | Required |
| ---- | ------------------- | -------- |
| date | date (_yyyy-MM-dd_) | no       |

The only input parameter provided in query-string is to filter by date. We have chosen not to make it mandatory in order to allow searching without filters *(find-all)*. The format chosen to represent the date follows the ISO 8601 standard, i.e. four digits for the year, two for the month and two for the day separated by a hyphen.

We also chose to truncate the date to the day, in order to be able to search on all flights available on that day (regardless of the exact departure time). This choice seemed the most logical for the use case of this search.

**Possible HTTP response states**:

| HTTP Status     | Type    | Internal error | Comments         |
| --------------- | ------- | -------------- | ---------------- |
| 200 OK          | Success | --             | search results   |
| 400 Bad Request | Error   | --             | `date` malformed |

#### 3.1.1 Usage examples

Find all available flights departing on March 15, 2017

**HTTP Request**:

```http
GET /flights?date=2017-03-15 HTTP/1.1
```

**HTTP Response**:

```http
HTTP/1.1 200 OK
```
```json
[
	{
        "flight-code": "BA001",
        "departure-date": "2017-03-15T20:30:00",
        "airline-name": "British Airways",
        "departure-airport": "JFK",
        "destination-airport": "LCY",
        "aircraft-type": "Airbus A318",
        "seat-availability": 32,
        "price": 1400.00
    },
    {
        "flight-code": "BA105",
        "departure-date": "2017-03-15T06:30:00",
        "airline-name": "British Airways",
        "departure-airport": "LHR",
        "destination-airport": "JFK",
        "aircraft-type": "Boeing 747-400",
        "seat-availability": 463,
        "price": 452.18
    },
	//...    
]
```



### 3.2 "HTTP GET /airlines/{id}/destinations"

**Description**: Find all destinations reached by a given airline (paginated results).

| HTTP Verb  | Path                        |
| ---------- | --------------------------- |
| GET        | /airlines/{id}/destinations |

| Path Param | Type    | Description |
| ---------- | ------- | ----------- |
| id         | integer | Airline ID  |

Again, this is a request for resources, so the HTTP verb is always **GET**. In this case, the **destination** resource was modelled as a sub-resource of the **airline** to highlight the dependency link. More specifically, we are trying to access information belonging to a single airline, so the first part of the path is a **punctual search** for the airline by **ID**.

**Query parameters**:

| Name | Type                        | Required | Default |
| ---- | --------------------------- | -------- | ------- |
| page | integer (_starting from 1_) | no       | 1       |

Since the results of such a search may be manifold, it was decided to apply pagination. Specifically, it was decided to page the results in pages containing at most `10` results at a time. Therefore the parameter "page" provided in the query-string is the requested page index (starting with `1`). For simplicity's sake, we have chosen not to make it mandatory and to assign it `1` as the default value, if not specified by the request.

In order to make the pagination of the results configurable, two parameters have been inserted in the application configuration file (`application.properties`) that allow respectively to enable or disable pagination, and to vary the number of results per page.

```properties
com.ricston.flights.paging.enable = false
com.ricston.flights.paging.size = 10
```

**Possible HTTP response states**:

| HTTP Status     | Type    | Internal error | Comments                      |
| --------------- | ------- | -------------- | ----------------------------- |
| 200 OK          | Success | --             | search results                |
| 400 Bad Request | Error   | --             | invalid `id` or `page` values |
| 404 Not Found   | Error   | 5              | airline not found             |

**Output payload**:

The results will be provided in the form of tuples (country, city, airport name).

#### 3.2.1 Usage examples

Find all destinations reached by Ryanair (ID: 4296)

**HTTP Request**:

```http
GET /airlines/4296/destinations?page=1 HTTP/1.1
```

**HTTP Response**:

```http
HTTP/1.1 200 OK
```

```json
{
    "items": [
        {"country":"Austria","city":"Linz","airport":"Linz Hörsching Airport"},
        {"country":"Bulgaria","city":"Plovdiv","airport":"Plovdiv International Airport"},
        {"country":"Croatia","city":"Osijek","airport":"Osijek Airport"},
        {"country":"Belgium","city":"Brussels","airport":"Brussels Airport"},
        {"country":"Belgium","city":"Charleroi","airport":"Brussels South Charleroi Airport"},
        {"country":"Croatia","city":"Rijeka","airport":"Rijeka Airport"},
        {"country":"Austria","city":"Salzburg","airport":"Salzburg Airport"},
        {"country":"Croatia","city":"Pula","airport":"Pula Airport"},
        {"country":"Croatia","city":"Zadar","airport":"Zemunik Airport"},
        {"country":"Cyprus","city":"Paphos","airport":"Paphos International Airport"}
    ],
    "page":1,
    "size":10
}
```



### 3.3 "HTTP POST /flights"

**Description**: Create a new flight.

| HTTP Verb | Path     |
| --------- | -------- |
| POST      | /flights |

This time we are going to create a new **flight** resource, so we use the HTTP **POST** verb. This endpoint has no parameters in the URL or query-string, but all the information is contained in the JSON payload.

**Request payload**:

| Field               | Type           | Required | Comments                              |
| ------------------- | -------------- | -------- | ------------------------------------- |
| flight-code         | string         | yes      | _(airline IATA code + flight number)_ |
| departure-date      | date-time      | yes      | (_yyyy-MM-dd'T'HH:mm_)                |
| departure-airport   | string         | yes      | _(airport IATA code)_                 |
| destination-airport | string         | yes      | _(airport IATA code)_                 |
| aircraft-type       | string         | yes      |                                       |
| seat-availability   | integer        | yes      |                                       |
| price               | decimal number | yes      |                                       |

All values are mandatory in order to create the flight correctly. The _flight-code_ is a string consisting of the airline's IATA code and the flight number. For example, the code `FR3883` indicates _Ryanair_ flight (IATA code '`FR`') number `3883`.

Since, as mentioned above, a flight must necessarily follow a predetermined route, it is necessary that the flight creation parameters include all the information needed to establish the route that the flight will follow. From the flight code, the IATA code of the airline is extracted and through the triad «`airline_code`, `departure_airport`, `destination_airport`»  we retrieve the route to follow.

The other fields are the same as in the flight data structure. It's also important that the `aircraft-type` is a valid model (among those in the database table).

> **Note**: It is important to note that since there are no integrity constraints on the database that explicitly link the flight table with the route table, it is up to the web service to ensure that this constraint is respected.

**Possible HTTP response states**:

| HTTP Status     | Type    | Internal error | Comments                                   |
| --------------- | ------- | -------------- | ------------------------------------------ |
| 201 Created     | Success | --             | new flight successfully created            |
| 400 Bad Request | Error   | --             | invalid input parameters                   |
| 400 Bad Request | Error   | 7              | invalid `flight-code`                      |
| 400 Bad Request | Error   | 6              | invalid `aircraft-type`                    |
| 409 Conflict    | Error   | 3              | no `route` matching the criteria provided. |

#### 3.3.1 Usage examples

We create a Ryanair flight with code FR6524 departing from Budapest (Hungary) to Charleroi (Belgium) on 5 July 2021 at 10:45AM.

**HTTP Request**:

```http
POST /flights HTTP/1.1
Content-Type: application/json
```
```json
{
    "flight-code": "FR6524",
    "departure-date": "2021-07-05T10:45",
    "departure-airport": "BUD",
    "destination-airport": "CRL",
    "aircraft-type": "Boeing 737-800",
    "seat-availability": 150,
    "price": 65.25
}
```

**HTTP Response**:

```http
HTTP/1.1 201 CREATED
```

```json
{
    "flight-code": "FR6524",
    "departure-date": "2021-07-05T10:45:00",
    "airline-name": "Ryanair",
    "departure-airport": "BUD",
    "destination-airport": "CRL",
    "aircraft-type": "Boeing 737-800",
    "seat-availability": 150,
    "price": 65.25
}
```



### 3.4 "HTTP POST /flights/{code}/ticket"

**Description**: Purchase an airline ticket for a given flight.

| HTTP Verb | Path                   |
| --------- | ---------------------- |
| POST      | /flights/{code}/ticket |

| Path Param | Type   | Description                                       |
| ---------- | ------ | ------------------------------------------------- |
| code       | string | Flight code _(airline IATA code + flight number)_ |

| Query Param    | Type                            | Required |
| -------------- | ------------------------------- | -------- |
| departure_date | datetime (_yyyy-MM-dd'T'HH:mm_) | yes      |

The issuing of a ticket is modelled as the creation (verb **POST**) of a **ticket** sub-resource relating to a specific **flight**.

As in the case of the destination sub-resource examined in point 3.2, to access a sub-resource it is necessary to define a path in the request URL capable of uniquely identifying the parent resource. In this case, the flight code alone cannot uniquely identify a flight (the database allows for the possibility of two flights with the same code departing at different times). For this we need an additional mandatory parameter in query-string, namely: the _departure-date_. The «`flight-code`, `departure-date`»  pair is used to uniquely identify a flight.

To purchase a ticket you will also need to provide a list of _passengers_. It is provided via the request JSON payload.

**Request payload**:

| Field      | Type     | Required | Comments                           |
| ---------- | -------- | -------- | ---------------------------------- |
| passengers | string[] | yes      | names of passengers (_at least 1_) |

This list is only there for demonstration purposes, as it is not currently persisted in the database, but is output within the ticket data structure, together with a ticket code. Although it should uniquely identify the ticket, at the moment the ticket entity is not persisted, so this code is randomly generated for demonstration purposes only.

However, it is necessary to provide at least one name of the passenger, otherwise an error will be generated.

**Possible HTTP response states**:

| HTTP Status     | Type    | Internal error | Comments                          |
| --------------- | ------- | -------------- | --------------------------------- |
| 201 Created     | Success | --             | new ticket successfully purchased |
| 400 Bad Request | Error   | --             | invalid input parameters          |
| 404 Not Found   | Error   | 4              | `flight` not found.               |
| 409 Conflict    | Error   | 1              | not enough places available.      |

**Output format**:

| Field      | Type          | Comments                    |
| ---------- | ------------- | --------------------------- |
| code       | string        | ticket identification code. |
| passengers | string[]      | names of passengers         |
| flight     | flight struct | flight of the ticket        |

> **Note**: Please note that the ticket entity is not persistent and that the identification code is randomly generated for demonstration purposes.

#### 3.4.1 Usage examples

Let us now try to purchase a ticket for the previously created flight (with _John_ as the only passenger).

**HTTP Request**:

```http
POST /flights/FR6524/ticket?departure-date=2021-07-05T10:45 HTTP/1.1
Content-Type: application/json
```

```json
{
  "passengers": ["John"]
}
```

**HTTP Response**:

```http
HTTP/1.1 201 CREATED
```

```json
{
    "code": "CU8NZOODUH",
    "passengers": [
        "Mario"
    ],
    "flight": {
        "flight-code": "FR6524",
        "departure-date": "2021-07-05T10:45:00",
        "airline-name": "Ryanair",
        "departure-airport": "BUD",
        "destination-airport": "CRL",
        "aircraft-type": "Boeing 737-800",
        "seat-availability": 149,
        "price": 65.25
    }
}
```

Suppose the flight is in high demand and the available seats are sold out.

A further request like the one above will give a different result (not allowing the purchase of the ticket).

**HTTP Response**:

```http
HTTP/1.1 409 CONFLICT
```

```json
{
    "code": 1,
    "message": "There are not enough seats for flight \"XX0000\" (available: 0, requested 1)."
}
```



### 3.5 "HTTP PATCH /flights/{code}/price"

**Description**: Increasing/decreasing the price for a specified flight.

| HTTP Verb | Path                  |
| --------- | --------------------- |
| PATCH     | /flights/{code}/price |

| Path Param | Type   | Description                                       |
| ---------- | ------ | ------------------------------------------------- |
| code       | string | Flight code _(airline IATA code + flight number)_ |

| Query Param    | Type                            | Required |
| -------------- | ------------------------------- | -------- |
| departure-date | datetime (_yyyy-MM-dd'T'HH:mm_) | yes      |

This endpoint exposes a partial change operation of a resource (a certain flight), for which it uses **PATCH** as an HTTP verb.

Specifically, this operation alters the price of a ticket by increasing or decreasing it by a certain `delta` amount.

As in the previous case for the purchase of a ticket (point 3.4) the operation focuses on a specific flight, so we will need the same «`flight-code`, `departure-date`» pair to identify the flight to be modified.

Since this variation directly impacts one of the flight fields, it has been chosen to provide this value as a JSON request payload.

**Request payload**:

| Field | Type           | Required | Comments                                                     |
| ----- | -------------- | -------- | ------------------------------------------------------------ |
| delta | decimal number | yes      | positive or negative amount by which to adjust the price of the flight. |

The `delta` value therefore defines how much to increase (in case of positive value) or decrease (in case of negative value) the price of the flight. For obvious reasons the price of a flight cannot be negative. If you try to reduce the price of a flight too much (in an attempt to bring it to a negative figure), an error will be generated.

**Possible HTTP response states**:

| HTTP Status     | Type    | Internal error | Comments                   |
| --------------- | ------- | -------------- | -------------------------- |
| 200 OK          | Success | --             | price successfully updated |
| 400 Bad Request | Error   | --             | invalid input parameters   |
| 404 Not Found   | Error   | 4              | `flight` not found.        |
| 409 Conflict    | Error   | 9              | invalid price reduction    |

#### 3.5.1 Usage examples

Let's reduce the price of our flight by 20€ (the currency is a guess, but not important)

**HTTP Request**:

```http
PATCH /flights/FR6524/price?departure-date=2021-07-05T10:45 HTTP/1.1
Content-Type: application/json
```

```json
{
  "delta": -20.0
}
```

**HTTP Response**:

```http
HTTP/1.1 200 CREATED
```

```json
{
    "flight-code": "FR6524",
    "departure-date": "2021-07-05T10:45:00",
    "airline-name": "Ryanair",
    "departure-airport": "BUD",
    "destination-airport": "CRL",
    "aircraft-type": "Boeing 737-800",
    "seat-availability": 149,
    "price": 45.25
}
```

If by mistake we were to reduce the price of our flight too much (for example by applying a reduction of 100€), we would get an error message of this type:

**HTTP Response**:

```http
HTTP/1.1 409 CONFLICT
```

```json
{
    "code": 9,
    "message": "The price of flight \"FR6524\" cannot be reduced any further (current: 45.25, reduction: -100)."
}
```

------

### 4. Class Diagram

As a brief introduction to the structure of the application, we provide below a class diagram showing the relationship between the various classes of the application. 

For simplicity's sake we excluded from the schema all data classes (Entity JPA, DTO, etc...), and other utility classes (mapper, error, converter, etc...), keeping only the classes belonging to the three layers in which the application is structured (*REST controller layer*, *service layer*, *repository layer*).

![class-diagram](https://github.com/floverde/ricston-flights-demo/blob/master/doc/class-diagram.svg)

_Author: Fabrizio Lo Verde_