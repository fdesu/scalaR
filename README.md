## Car advert service

Service consists of two parts:
* Incoming request processor
* Data persistence layer with JPA

### Build & Run

To build and run the application it should be enough to just run 
```bash
sbt run
``` 
in case of success `[info] p.c.s.AkkaHttpServer - Listening for HTTP on /0:0:0:0:0:0:0:0:9000` will appear.

### Endpoints

URL-to-endpoint mapping:
```
HTTP VERB       URL                 DESCRIPTION
---------------------------------------------------------------
GET             /v1/car/all         list all adverts
GET             /v1/car/:id         fetch single advert by id
POST            /v1/car/new         add new advert
PUT             /v1/car/change      update existing advert
DELETE          /v1/car/:id         delete advert by id
```
The most up-to-date mapping could be found at `conf/routes`


### Testing

#### Automated

Automated testing is done using Play's `FakeApplication` facility with embedded `H2` database. As JPA being used Database could be easily changed by changing `DataSource`.

Tests could be divided into 3 different categories:
* Unit (simple mocking/verification)
* Integration (mostly persistence layer)
* Acceptance (REST Api testing)

##### Sanity

In order to verify application sanity, one need to start the it (e.g. `sbt run`) and query appropriate endpoints. Below examples with `curl` could be found:

###### persist new entity example
```bash
$ curl -i -H "Content-Type: application/json" -X POST -d '{"id":333,"title":"A","fuel":"GASOLINE","price":123,"new":true,"mileage":0,"registrationDate":"2019-01-25"}' http://localhost:9000/v1/car/new
  % Total    % Received % Xferd  Average Speed   Time    Time     Time  Current
                                 Dload  Upload   Total   Spent    Left  Speed
100   115  100     8  100   107     28    380 --:--:-- --:--:-- --:--:--   409 HTTP/1.1 200 OK
Vary: Origin
Referrer-Policy: origin-when-cross-origin, strict-origin-when-cross-origin
X-Frame-Options: DENY
X-XSS-Protection: 1; mode=block
X-Content-Type-Options: nosniff
Content-Security-Policy: default-src 'self'
X-Permitted-Cross-Domain-Policies: master-only
Date: Fri, 25 Jan 2019 09:38:21 GMT
Content-Type: application/json
Content-Length: 8

{"id":1}
```

###### fetch by id example
```bash
$ curl -i -X GET http://localhost:9000/v1/car/1
  % Total    % Received % Xferd  Average Speed   Time    Time     Time  Current
                                 Dload  Upload   Total   Spent    Left  Speed
100   105  100   105    0     0   3281      0 --:--:-- --:--:-- --:--:--  3281HTTP/1.1 200 OK
Vary: Origin
Referrer-Policy: origin-when-cross-origin, strict-origin-when-cross-origin
X-Frame-Options: DENY
X-XSS-Protection: 1; mode=block
X-Content-Type-Options: nosniff
Content-Security-Policy: default-src 'self'
X-Permitted-Cross-Domain-Policies: master-only
Date: Fri, 25 Jan 2019 09:38:29 GMT
Content-Type: application/json
Content-Length: 105

{"id":1,"title":"A","fuel":"GASOLINE","price":123,"new":true,"mileage":0,"registrationDate":"2019-01-25"}
```

###### an example of endpoint declining request when `Content-Type` != `[applciation|text]/json`
```bash
$ curl -i -X POST -d '{"field":"whatever"}' http://localhost:9000/v1/car/new
  % Total    % Received % Xferd  Average Speed   Time    Time     Time  Current
                                 Dload  Upload   Total   Spent    Left  Speed
100    59  100    39  100    20   2437   1250 --:--:-- --:--:-- --:--:--  3687HTTP/1.1 400 Bad Request
Vary: Origin
Referrer-Policy: origin-when-cross-origin, strict-origin-when-cross-origin
X-Frame-Options: DENY
X-XSS-Protection: 1; mode=block
X-Content-Type-Options: nosniff
Content-Security-Policy: default-src 'self'
X-Permitted-Cross-Domain-Policies: master-only
Date: Fri, 25 Jan 2019 09:41:49 GMT
Content-Type: text/plain; charset=UTF-8
Content-Length: 39

Expecting application/json request body
```