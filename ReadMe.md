#  Music Metadata Service

** Music Metadata Service**  is a scalable microservice designed 
to manage artist and track metadata, exposing RESTful APIs, 
integrated with PostgreSQL for persistence and Redis for caching. 
It’s Dockerized, cloud-ready, and supports monitoring through 
Prometheus and Spring Boot Actuator.

## Architecture Overview

* The application is dockerized, and also cloud ready.
* There is a Nginx sitting in front of the application as load balancer.
* There are two instances of application running(app1,app2).
* PostgreSQL is the primary database for storing music metadata.
* Application exposes Swagger UI for API exploration.
* Integrated Prometheus via Spring Boot Actuator for observability.
* Supports both Testing (H2) and production (PostgreSQL) profiles.

## Architecture Overview
```
                          +---------------------+
                          |     NGINX (LB)      |
                          +----------+----------+
                                     |
           +-------------------------+-------------------------+
           |                                                   |
   +-------v--------+                                   +------v--------+
   |   App Instance |                                   |  App Instance |
   |     (app1)     |                                   |     (app2)    |
   +-------+--------+                                   +------+--------+
           |                                                   |
           +-------------------------+-------------------------+
                                     |
                       +-------------v--------------+
                       |        Redis Cache         |
                       +-------------+--------------+
                                     |
                          +----------v-----------+
                          |     PostgreSQL DB     |
                          +-----------------------+

```

## REST Endpoints

* Update Artist name PUT /api/v1/artists/{identifier}
```
curl -X 'PUT' \
  'http://localhost:8080/api/v1/artists/bon-jovi' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '"son john"'
```
* The response will be
  ```
  {
  "name": "\"son john\"",
  "createdAt": "2025-06-08T18:44:12.848251",
  "tracks": []
  }
   ``` 
  
* To get tracks by artist GET to /api/v1/artists/{identifier}/tracks
  ```
  curl -X 'GET' \
  'http://localhost:8080/api/v1/artists/sonu-nigam/tracks' \
  -H 'accept: */*'
  ```
* The response will be
  ```
  [
  {
    "title": "trere bin",
    "genre": "Hip-Hop",
    "lengthInSeconds": 267
  }
  ]
   ```
* To add tracks for artist POST /api/v1/artists/{identifier}/tracks
 ```
  curl -X 'POST' \
  'http://localhost:8080/api/v1/artists/sonu-nigam/tracks' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "title": "teri chera",
  "genre": "modern",
  "lengthSeconds": 300
}'
  ```
* The response will be
  ```
  {
  "title": "teri chera",
  "genre": "modern",
  "lengthInSeconds": 300
  }
   ```
* The artist of the day GET /api/v1/artists/artist-of-the-day
  
  ``` 
  curl -X 'GET' \
  'http://localhost:8080/api/v1/artists/artist-of-the-day' \
  -H 'accept: */*'
    ```
* The response will be
      ```
      {
        "name": "\"son john\"",
        "createdAt": "2025-06-08T18:44:12.848251",
        "tracks": []
      }
       ```

* Swagger document can be found in

``` 
  http://localhost:8080/swagger-ui/index.html
``` 

* Actuator Prometheus endpoint is exposed via the following url

``` 
  http://localhost:8080/actuator/prometheus
  
  (http_server_requests_seconds_count{uri="/api/v1/artists/**"})
``` 

* Also we have included a docker image of promethus configured with the actuator api.

``` 
  http://localhost:9090/
``` 

### Stack

* Java 18
* Spring Boot 3
* JUnit
* Mockito
* PostgreSQL
* H2 (for testing)
* maven
* Docker
* Redis (caching)
* Nginx
* Springdoc OpenAPI (Swagger)
* Prometheus and Actuator for monitoring

### Setup and Run

* Build and install
  ```
    mvn clean install
  ```
* Deploy and run the application
  ```
    docker-compose up -d 
  ```
### Some Class and their purpose
  ```
- ArtistController - Controller class providing apis to get and set the artist meta data and their related tracks. Handles all the non error flows
- TrackRequestDto, TrackResponseDto - Used as dto to transfer data, also provides type safety and control.
- ArtistService, TrackService - Business logic layer(maintains SOLID principle), maintains the logic for Tracks and artist.
- ArtistSelectionStrategy- Strategy interface for selecting an Artist from a given list, based on a specific time-based criteria.
- ArtistStrategyFactory - Factory class for retrieving the appropriate ArtistSelectionStrategy 
                based on a given time period. This enables flexible and decoupled logic for selecting an artist
                by delegating the responsibility to different strategy implementations.
- DailyArtistSelectionStrategy - Strategy implementation for selecting the "Artist of the Day".
- ArtistRepository, TrackRepository - Only deals with data storage and retrival.(From SOLID - SRS)
- Artist, Track -Actual Entity object that holds the data.
- ErrorHandlingControllerAdvice - handles all the error flows.
- ErrorResponse - gives the business object for errors.
- ArtistControllerIntegrationTest - Integration test for testing end to end functionality.
- ArtistServiceTest, TrackServiceTest - Unit test for service layers in a granular way.
  ```



