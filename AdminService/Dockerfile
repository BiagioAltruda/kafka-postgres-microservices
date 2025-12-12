FROM eclipse-temurin:17-jdk-alpine
WORKDIR /Work/Anagrafe/AdminService

COPY ./target/AdminService-0.0.1-SNAPSHOT.jar security.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","security.jar"]

