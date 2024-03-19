# WCC Coding Challenge

This assignment is created for WCC to build a RESTful API (also know as REST (-y)) to calculate distance between 2 postcode in UK.

## Startup 
### Requirements:
1. Java jdk 1.17
2. Docker started and running

### Steps to start:
1. Run startWCCPostgresDocker.cmd to create a Postgresql database container. Container name will be wcc-postgres.
2. Wait for wcc-postgres to start.
3. Run command below to start the application.
````cmd
.\mvnm spring-boot:run
````
4. The application startup for the first time will take longer to complete. This is because liquibase will create the table and populate the table with uk postcode.


## REST endpoint
1. /distance
* HTTP Method: POST
* calculate distance between 2 postcode. 
* Accepts the following json
````json
{
  "postcode1": "<postcode 1>",
  "postcode2": "<postcode 2>"
}
````

2. /postcode/{postcode id}
* HTTP Method: GET
* get details of postcode based on id

3. /postcode/{postcode id}
* HTTP Method: PUT
* Update postcode latitude and longitude.
* Accepts the following json
````json
{
    "postcode": "<postcode>",
    "latitude": "<latitude>",
    "longitude": "<longitude>"
}
````


## Not covered
### Code:
1. Input validation on update postcode. Only search distance covered due to time constrain.
2. Add new username.
3. Segregation of read only user and user that can perform update.

### API Not covered:
1. List postcodes.
2. New postcode.
3. Search postcode based on postcode.
4. Delete postcode.

## Additional Information
1. jacoco report is available in target/site/jacoco for test coverage report.