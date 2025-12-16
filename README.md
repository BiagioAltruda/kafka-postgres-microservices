# Document storage project

<!--toc:start-->

- [Document storage project](#document-storage-project)
  - [Services](#services)
  - [Endpoints](#endpoints)
    - [User Management](#user-management)
    - [Document Management](#document-management)
  - [Main Entities](#main-entities) - [Persisted Entities](#persisted-entities) - [DTOs](#dtos)
  <!--toc:end-->

## Services

1. AdminService: Authentication, user management, security. Main access point.
2. DocumentService: Document storage, retrieval, indexing.
3. LoggingService: Centralized logging, monitoring, alerting. Primarely called with Apache Kafka version 4.1.1
4. Kafka server: Reliable message broker for inter-service communication. Runs on port 9092 through docker.

## Endpoints

### User Management

1. <http://localhost:8080/auth/register> [POST]: User registration.
2. <http://localhost:8080/auth/login> [POST]: User login.

### Document Management

1. <http://localhost:8080/documentation/upload> [POST]: Upload document. Calls DocumentService via http.
2. <http://localhost:8082/documentation/upload> [POST]: Upload document.

## Main Entities

### Persisted Entities

1. BaseUser: id, username, password (BCrypt), roles.
2. Document: id, userId, documentType, content (to do), creationDate, expirationDate.
3. ChangeLog: id, eventType, modifiedEntity, timestamp, message.

### DTOs

1. DocumentationRequestDTO: userId, documents, operation, size.
