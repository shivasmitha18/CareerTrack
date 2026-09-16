# CareerTrack

CareerTrack is a full-stack Job Application and Career Management Platform built to help users track job applications, interviews, application status, and career progress.

## Features

- User registration and login
- JWT-based authentication
- BCrypt password encryption
- Secure REST APIs
- Job application CRUD operations
- Application status tracking
- Search and filtering
- Sorting
- Pagination
- Interview date tracking
- Upcoming interview tracking
- Application statistics
- Analytics dashboard
- Input validation
- Global exception handling
- MySQL database integration
- Swagger/OpenAPI documentation
- Unit and controller testing
- Docker support

## Application Status

CareerTrack supports the following application stages:

- Applied
- Assessment
- Interview
- Offer
- Rejected

## Tech Stack

### Backend

- Java 21
- Spring Boot
- Spring Web MVC
- Spring Data JPA
- Hibernate
- Spring Security
- JWT
- Maven

### Database

- MySQL

### Frontend

- HTML
- CSS
- JavaScript
- Chart.js

### Tools

- IntelliJ IDEA
- Postman
- Swagger/OpenAPI
- Git
- GitHub
- Docker

## Project Architecture

The project follows a layered architecture:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
Database
```

## Application Screenshots

### 1. Registration

![CareerTrack Registration](Screenshot%202026-09-16%20165748.png)

### 2. Login

![CareerTrack Login](Screenshot%202026-09-13%20172446.png)

### 3. Dashboard

![CareerTrack Dashboard](Screenshot%202026-09-16%20182250.png)

### 4. Add Job Application

![Add Job Application](Screenshot%202026-09-16%20182341.png)

### 5. Application List and Status

![Application List and Status](Screenshot%202026-09-16%20182322.png)

### 6. Upcoming Interviews

![Upcoming Interviews](Screenshot%202026-09-16%20182421.png)

### 7. Analytics and Charts

![Application Analytics](Screenshot%202026-09-16%20182433.png)

## API

The application provides REST APIs for:

- User registration
- User login
- User management
- Job application management
- Application status updates
- Search and filtering
- Pagination
- Application statistics
- Upcoming interviews

Swagger/OpenAPI documentation is available when the application is running:

```text
http://localhost:8080/swagger-ui/index.html
```

## Database

CareerTrack uses MySQL for persistent data storage.

Create a database named:

```sql
CREATE DATABASE careertrack_db;
```

Database credentials are supplied through environment variables and are not stored in the repository.

## Running the Project Locally

### 1. Clone the repository

```bash
git clone https://github.com/shivasmitha18/CareerTrack.git
cd CareerTrack
```

### 2. Configure environment variables

Set the following environment variables:

```text
DB_URL
DB_USERNAME
DB_PASSWORD
JWT_SECRET
```

### 3. Build the project

Windows:

```bash
.\mvnw.cmd clean package -DskipTests
```

### 4. Run the application

```bash
.\mvnw.cmd spring-boot:run
```

The application will be available at:

```text
http://localhost:8080
```

## Docker

CareerTrack also includes a Dockerfile for containerized deployment.

Build the Docker image:

```bash
docker build -t careertrack:1.0 .
```

Run the container:

```bash
docker run -p 8080:8080 careertrack:1.0
```

Database and JWT configuration should be supplied through environment variables.

## Testing

The project includes tests for controllers and services.

Run the test suite with:

```bash
.\mvnw.cmd test
```

## Project Highlights

CareerTrack demonstrates practical backend development concepts including:

- REST API development
- Authentication and authorization
- JWT token handling
- Password encryption
- JPA/Hibernate
- MySQL integration
- DTOs
- Validation
- Exception handling
- Pagination and filtering
- API documentation
- Automated testing
- Docker containerization
- Frontend-backend integration

## Future Enhancements

Possible future improvements include:

- AI-powered career insights
- Interview reminders and notifications
- Advanced analytics
- Cloud deployment
- Resume management
- Job recommendation features

## Author

Shivasmitha

## Repository

https://github.com/shivasmitha18/CareerTrack
