FROM openjdk:17-jdk-slim

VOLUME /tmp

COPY target/github-status-tracker-1.0.jar github-status-tracker-1.0.jar

ENTRYPOINT ["java", "-jar", "/github-status-tracker-1.0.jar"]