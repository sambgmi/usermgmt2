# User Management System with Country Integration

## Project Overview
A Spring Boot application that combines user management with country data integration, featuring secure authentication, REST APIs, and containerization.

## Technical Stack
- Backend: Spring Boot 3.x
- Database: MySQL 8.0
- Security: JWT Authentication
- Documentation: OpenAPI/Swagger
- Containerization: Docker
- API Integration: REST Countries API

## Architecture
### User Management Module
- JWT-based authentication
- BCrypt password encryption
- Role-based authorization
- CRUD operations for user profiles

### Country Management Module
- Integration with REST Countries API
- Local database caching
- Automatic data synchronization
- CRUD operations for country data

## API Endpoints

### Authentication
- `POST /api/auth/register`: User registration
- `POST /api/auth/login`: User authentication

### User Operations
- `GET /api/users/me`: Get current user profile
- `PUT /api/users/me`: Update user profile
- `DELETE /api/users/me`: Delete user account

### Country Operations
- `GET /api/countries`: List all countries
- `GET /api/countries/{id}`: Get country details
- `POST /api/countries`: Add new country
- `PUT /api/countries/{id}`: Update country
- `DELETE /api/countries/{id}`: Delete country
- `POST /api/countries/sync`: Sync with external API

## Setup Instructions

### Prerequisites
- JDK 17
- Docker Desktop
- Maven
- MySQL 8.0

### Local Development Setup
1. Clone the repository
2. Configure database:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/usermgmt
spring.datasource.username=root
spring.datasource.password=root