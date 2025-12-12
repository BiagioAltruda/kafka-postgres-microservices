FROM eclipse-temurin:17-jdk-alpine
WORKDIR /Work/Anagrafe/LogService

COPY ./target/LogService-0.0.1-SNAPSHOT.jar log_service.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","log_service.jar"]

