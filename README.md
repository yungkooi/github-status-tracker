## Steps to run ##

Jar file is provided for this example. To rebuild this project, go to root folder and run the following command (requires Maven):
- mvn clean install

The project can be run via docker container. Go to project root folder and run the following commands:
- docker build -t github-status-tracker-1.0 .
- docker run -p 8080:8080 github-status-tracker-1.0

Alternatively, we can run it if Java 17 is installed locally. Go to project root folder and run the following command:
- java -jar target/github-status-tracker-1.0.jar

To download the csv file, visit the following url:
- http://localhost:8080/api/csv/download

To access H2 database:
- Go to http://localhost:8080/h2-console/login
- Key in the JDBC url, username and password found in src/main/resources/application.properties

## Assumptions ##
- New data will override the old one after each schedule
- API is set to trigger every 10 seconds. This can be controlled via `request.interval` field found in application.properties
- Timestamp is of the time where API is called, not the time provided in "updated_at" field of API response
- H2 database (embedded) is used in this project for simplicity. To have persistent data, can consider other alternatives such as MongoDB
