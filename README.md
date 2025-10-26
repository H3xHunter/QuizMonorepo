# Microservices Ecosystem

Spring Boot microservices architecture with Netflix Eureka for service discovery, Spring Cloud Gateway for API routing, and OpenFeign for inter-service communication.

## Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Technology Stack](#technology-stack)
- [Quick Start](#quick-start)
- [API Endpoints](#api-endpoints)
- [Database Management](#database-management)
- [Troubleshooting](#troubleshooting)
- [Development Guide](#development-guide)

---

## Overview

This project implements a microservices architecture using Spring Cloud, featuring:

- **Service Discovery**: Netflix Eureka for dynamic service registration and discovery
- **API Gateway**: Single entry point for all client requests with automatic routing
- **Inter-Service Communication**: OpenFeign clients for REST communication between services
- **Database Isolation**: Separate PostgreSQL instances for each service
- **Health Monitoring**: Spring Boot Actuator for health checks and metrics

### Services

| Service | Port | Database | Description |
|---------|------|----------|-------------|
| **Eureka Server** | 8761 | - | Service discovery and registration |
| **API Gateway** | 8080 | - | Single entry point, routing, and load balancing |
| **Auth Service** | 8081 | PostgreSQL (5432) | JWT authentication and user management |
| **Medianos Service** | 8082 | PostgreSQL (5434) | Manages "medianos" entities |
| **Photos Service** | 8083 | PostgreSQL (5433) | Manages photos linked to medianos |

---

## Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                        Client/Browser                        │
└──────────────────────────┬──────────────────────────────────┘
                           │
                           ▼
                 ┌─────────────────────┐
                 │   API Gateway       │
                 │   Port: 8080        │
                 └──────────┬──────────┘
                            │
              ┌─────────────┼─────────────┐
              │             │             │
              ▼             ▼             ▼
      ┌──────────┐  ┌──────────┐  ┌──────────┐
      │   Auth   │  │ Medianos │  │  Photos  │
      │ Service  │  │ Service  │  │ Service  │
      │  :8081   │  │  :8082   │◄─┤  :8083   │ (Feign)
      └────┬─────┘  └────┬─────┘  └────┬─────┘
           │             │             │
           ▼             ▼             ▼
      ┌─────────┐  ┌─────────┐  ┌─────────┐
      │PostgreSQL│  │PostgreSQL│  │PostgreSQL│
      │  :5432   │  │  :5434   │  │  :5433   │
      └──────────┘  └──────────┘  └──────────┘
                            │
                            ▼
                 ┌─────────────────────┐
                 │  Eureka Server      │
                 │  Port: 8761         │
                 │  (Service Registry) │
                 └─────────────────────┘
```

### Key Features

- **Service Discovery**: All services register with Eureka at startup
- **Client-Side Load Balancing**: Gateway uses Ribbon for distributing requests
- **Inter-Service Communication**: Medianos ↔ Photos communication via OpenFeign
- **Database Per Service**: Each microservice has its own PostgreSQL database
- **Centralized Routing**: All external requests go through the API Gateway

---

## Technology Stack

- **Java 21**
- **Spring Boot 3.3.5**
- **Spring Cloud 2023.0.3**
  - Netflix Eureka Server & Client
  - Spring Cloud Gateway
  - OpenFeign
- **Maven** (Multi-module project)
- **PostgreSQL 16** (Docker containers)
- **Docker & Docker Compose**
- **Lombok** (Reduce boilerplate code)

---

## Quick Start

### Prerequisites

- **Java 21 JDK** installed
- **Maven 3.8+** installed
- **Docker Desktop** running (for PostgreSQL databases)
- **Git** for cloning the repository

### Step 1: Clone and Build

```bash
# Clone the repository
git clone <repository-url>
cd QuizMonorepo

# Build all modules (this downloads dependencies)
mvn clean install -DskipTests
```

### Step 2: Start PostgreSQL Databases

```bash
# Start all 3 PostgreSQL instances
docker-compose up -d

# Verify databases are running
docker-compose ps

# Expected output: 3 containers running (auth-postgres, photos-postgres, medianos-postgres)
```

**Database Configuration:**

| Service | Host | Port | Database | Username | Password |
|---------|------|------|----------|----------|----------|
| Auth | localhost | 5432 | `auth_db` | postgres | postgres |
| Photos | localhost | 5433 | `photos_db` | postgres | postgres |
| Medianos | localhost | 5434 | `medianos_db` | postgres | postgres |

### Step 3: Start Services (Order Matters!)

**⚠️ IMPORTANT**: Start services in this exact order:

#### 1. Start Eureka Server (Service Discovery)

```bash
cd eureka-server
mvn spring-boot:run
```

**Wait until you see**: `Started Eureka Server`
**Verify**: Open http://localhost:8761 - You should see the Eureka dashboard

#### 2. Start API Gateway

```bash
# In a new terminal
cd api-gateway
mvn spring-boot:run
```

**Wait until you see**: `Started ApiGatewayApplication`
**Verify**: `curl http://localhost:8080/actuator/health` should return `{"status":"UP"}`

#### 3. Start Business Services (any order)

```bash
# Terminal 1 - Auth Service
cd auth-service
mvn spring-boot:run

# Terminal 2 - Medianos Service
cd medianos-service
mvn spring-boot:run

# Terminal 3 - Photos Service
cd photos-service
mvn spring-boot:run
```

### Step 4: Verify Everything is Running

1. **Eureka Dashboard**: http://localhost:8761
   - Should show 4 instances registered: `API-GATEWAY`, `AUTH-SERVICE`, `MEDIANOS-SERVICE`, `PHOTOS-SERVICE`

2. **Gateway Routes**: http://localhost:8080/actuator/gateway/routes
   - Should show routes to all services

3. **Health Checks**:
   ```bash
   curl http://localhost:8761/actuator/health  # Eureka
   curl http://localhost:8080/actuator/health  # Gateway
   curl http://localhost:8081/actuator/health  # Auth
   curl http://localhost:8082/actuator/health  # Medianos
   curl http://localhost:8083/actuator/health  # Photos
   ```

---

## API Endpoints

All services can be accessed through the **API Gateway** at `http://localhost:8080`.

**IMPORTANT**: All API tests must go through the Gateway, not directly to services.

### Gateway Routing Configuration

The API Gateway uses **two routing strategies**:

#### 1. Auto-Discovery Routing (Dynamic)

**Enabled**: Service discovery via Eureka automatically creates routes for all registered services.

```yaml
spring:
  cloud:
    gateway:
      discovery:
        locator:
          enabled: true
          lower-case-service-id: true
```

**Auto-Generated Routes**:

| Service in Eureka | Gateway URL Pattern | Target Service |
|-------------------|---------------------|----------------|
| `AUTH-SERVICE` | `http://localhost:8080/auth-service/**` | `localhost:8081` |
| `MEDIANOS-SERVICE` | `http://localhost:8080/medianos-service/**` | `localhost:8082` |
| `PHOTOS-SERVICE` | `http://localhost:8080/photos-service/**` | `localhost:8083` |

The Gateway automatically:
- Discovers services registered in Eureka
- Creates routes using lowercase service names
- Performs client-side load balancing (if multiple instances exist)
- Rewrites paths by removing the service prefix

#### 2. Explicit Routes (Manual)

**Configured Routes**:
- **Eureka Dashboard**: `http://localhost:8080/eureka/` → `http://localhost:8761`

#### CORS Configuration (Development)

```yaml
spring:
  cloud:
    gateway:
      globalcors:
        cors-configurations:
          '[/**]':
            allowedOrigins: "*"
            allowedMethods: "*"
            allowedHeaders: "*"
```

**Status**: ✅ Configured for development
- All origins, methods, and headers are allowed
- **Production**: Should restrict to specific origins

### API Gateway Endpoints

Base URL: `http://localhost:8080`

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/actuator/health` | Gateway health status |
| GET | `/actuator/gateway/routes` | **View all active routes** (JSON) |
| GET | `/actuator/gateway/globalfilters` | View global filters |
| GET | `/actuator/info` | Gateway service information |
| GET | `/actuator/metrics` | Prometheus-compatible metrics |
| GET | `/eureka/` | Access Eureka dashboard through gateway |

**Example - View Routes**:
```bash
curl http://localhost:8080/actuator/gateway/routes | jq
```

---

### Auth Service

Base URL: `http://localhost:8080/auth-service` (via Gateway)
Direct URL: `http://localhost:8081` (direct access)

| Method | Endpoint | Description | Request Body |
|--------|----------|-------------|--------------|
| GET | `/actuator/health` | Service health check | - |

**Note**: Auth endpoints are currently under development.

---

### Medianos Service

Base URL: `http://localhost:8080/medianos-service` (via Gateway)
Direct URL: `http://localhost:8082` (direct access)

#### Endpoints

| Method | Endpoint | Description | Request Body | Response |
|--------|----------|-------------|--------------|----------|
| GET | `/api/medianos/` | Get all medianos | - | `List<MedianoDTO>` |
| POST | `/api/medianos/` | Create new mediano | `MedianoDTO` | `200 OK` |
| GET | `/api/medianos/{id}` | Get mediano by ID | - | `Mediano` |
| GET | `/api/medianos/{id}/fotos` | Get mediano with photos (Feign) | - | `MedianoWithPhotosDTO` |
| GET | `/api/medianos/filtrado/{nombre}` | Find mediano by name | - | `Mediano` |

#### DTOs

**MedianoDTO** (Request - POST):
```json
{
  "nombre": "string",
  "altura": 175,
  "email": "mediano@example.com"
}
```

**MedianoWithPhotosDTO** (Response - GET /{id}/fotos):
```json
{
  "id": "uuid-string",
  "nombre": "string",
  "altura": 175,
  "email": "mediano@example.com",
  "photos": [
    {
      "id": 1,
      "path": "/images/photo1.jpg",
      "description": "Photo description",
      "created": "2025-10-26",
      "medianoId": "uuid-string"
    }
  ]
}
```

#### Example Requests

```bash
# Get all medianos
curl http://localhost:8080/medianos-service/api/medianos/

# Create a new mediano
curl -X POST http://localhost:8080/medianos-service/api/medianos/ \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Juan Perez",
    "altura": 175,
    "email": "juan@example.com"
  }'

# Get mediano with all photos (uses Feign to call Photos Service)
curl http://localhost:8080/medianos-service/api/medianos/{id}/fotos
```

---

### Photos Service

Base URL: `http://localhost:8080/photos-service` (via Gateway)
Direct URL: `http://localhost:8083` (direct access)

#### Endpoints

| Method | Endpoint | Description | Request Body | Response |
|--------|----------|-------------|--------------|----------|
| GET | `/api/fotos/` | Get all photos | - | `List<Photo>` |
| GET | `/api/fotos/mediano/{medianoId}` | Get photos by mediano ID | - | `List<Photo>` |

#### Photo Entity

```json
{
  "id": 1,
  "path": "/images/photo1.jpg",
  "description": "Photo description",
  "created": "2025-10-26",
  "medianoId": "uuid-string"
}
```

#### Example Requests

```bash
# Get all photos
curl http://localhost:8080/photos-service/api/fotos/

# Get photos for a specific mediano
curl http://localhost:8080/photos-service/api/fotos/mediano/{medianoId}
```

---

### Eureka Server

Base URL: `http://localhost:8761`

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/` | Eureka web dashboard |
| GET | `/actuator/health` | Eureka server health |
| GET | `/eureka/apps` | All registered applications (XML) |
| GET | `/eureka/apps/{serviceName}` | Specific service info |

---

## Database Management

### Starting Databases

```bash
# Start all databases
docker-compose up -d

# Start specific database
docker-compose up -d auth-db
docker-compose up -d photos-db
docker-compose up -d medianos-db
```

### Stopping Databases

```bash
# Stop all databases (data persists)
docker-compose down

# Stop and DELETE all data
docker-compose down -v
```

### Viewing Logs

```bash
# All databases
docker-compose logs -f

# Specific database
docker-compose logs -f auth-db
docker-compose logs -f photos-db
docker-compose logs -f medianos-db
```

### Connecting to Databases

#### Using psql (from Docker container)

```bash
# Auth database
docker exec -it auth-postgres psql -U postgres -d auth_db

# Photos database
docker exec -it photos-postgres psql -U postgres -d photos_db

# Medianos database
docker exec -it medianos-postgres psql -U postgres -d medianos_db
```

#### Using GUI Tools (DBeaver, pgAdmin, DataGrip)

| Service | Host | Port | Database | User | Password |
|---------|------|------|----------|------|----------|
| Auth | localhost | 5432 | auth_db | postgres | postgres |
| Photos | localhost | 5433 | photos_db | postgres | postgres |
| Medianos | localhost | 5434 | medianos_db | postgres | postgres |

### Data Persistence

- Data is stored in Docker volumes: `auth-db-data`, `photos-db-data`, `medianos-db-data`
- Data persists across container restarts
- Only deleted with `docker-compose down -v`

---

## Troubleshooting

### Services Won't Start

**Problem**: Service fails to start with connection errors

**Solutions**:
1. Check if Eureka is running first: `curl http://localhost:8761/actuator/health`
2. Verify database containers are running: `docker-compose ps`
3. Check logs: `docker-compose logs <service-name>`
4. Restart services in order: Eureka → Gateway → Business Services

### Port Already in Use

**Problem**: `Port 8080 is already in use`

**Solutions**:
```bash
# Windows - Find process using port
netstat -ano | findstr :8080

# Kill the process
taskkill /PID <process-id> /F

# Or change the port in application.yml
```

### Cannot Connect to Database

**Problem**: `Connection refused` or `Connection timeout`

**Solutions**:
1. Verify Docker Desktop is running
2. Check containers: `docker-compose ps`
3. Restart containers: `docker-compose restart`
4. Check port conflicts: `netstat -ano | findstr :5432`

### Service Not Appearing in Eureka

**Problem**: Service starts but doesn't show in Eureka dashboard

**Solutions**:
1. Wait 30 seconds (Eureka registration delay)
2. Check service logs for Eureka connection errors
3. Verify `eureka.client.service-url.defaultZone` in `application.yml`
4. Restart the service

### Maven Build Failures

**Problem**: Dependencies won't download or build fails

**Solutions**:
```bash
# Clear Maven cache and rebuild
mvn clean install -U

# Skip tests
mvn clean install -DskipTests

# In IntelliJ
# Maven Tool Window → Reload All Projects
# File → Invalidate Caches / Restart
```

### Feign Client Errors

**Problem**: `FeignException` or inter-service communication fails

**Solutions**:
1. Verify both services are registered in Eureka
2. Check service names match in `@FeignClient(name = "SERVICE-NAME")`
3. Verify endpoints exist in the target service
4. Check Gateway routes: `http://localhost:8080/actuator/gateway/routes`

---

## Development Guide

### Adding a New Microservice

1. **Create module directory** in project root
2. **Add module to parent POM**:
   ```xml
   <modules>
       <module>your-service</module>
   </modules>
   ```

3. **Create service POM** with dependencies:
   ```xml
   <dependency>
       <groupId>org.springframework.cloud</groupId>
       <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
   </dependency>
   <dependency>
       <groupId>org.springframework.cloud</groupId>
       <artifactId>spring-cloud-starter-openfeign</artifactId>
   </dependency>
   ```

4. **Create application.yml**:
   ```yaml
   spring:
     application:
       name: YOUR-SERVICE
   server:
     port: 808X
   eureka:
     client:
       service-url:
         defaultZone: http://localhost:8761/eureka/
   ```

5. **Enable discovery and Feign**:
   ```java
   @SpringBootApplication
   @EnableDiscoveryClient
   @EnableFeignClients
   public class YourServiceApplication { }
   ```

### Project Structure

```
QuizMonorepo/
├── pom.xml                       # Parent POM (manages versions)
├── docker-compose.yml            # PostgreSQL databases
├── README.md                     # This file
├── TODO.md                       # Future enhancements
├── CLAUDE.md                     # AI assistant instructions
│
├── eureka-server/                # Service Discovery
│   ├── pom.xml
│   └── src/main/
│       ├── java/com/condocker/eureka/
│       │   ├── EurekaServerApplication.java
│       │   └── config/EurekaSecurityConfig.java
│       └── resources/application.yml
│
├── api-gateway/                  # API Gateway
│   ├── pom.xml
│   └── src/main/
│       ├── java/com/condocker/gateway/
│       │   └── ApiGatewayApplication.java
│       └── resources/application.yml
│
├── auth-service/                 # Authentication
│   ├── pom.xml
│   └── src/main/
│       ├── java/com/condocker/
│       │   ├── AuthApplication.java
│       │   ├── controller/
│       │   ├── service/
│       │   ├── entity/
│       │   └── repo/
│       └── resources/application.yml
│
├── medianos-service/             # Medianos Management
│   ├── pom.xml
│   └── src/main/
│       ├── java/com/condocker/
│       │   ├── MedianosServiceApplication.java
│       │   ├── client/PhotosFeignClient.java
│       │   ├── controller/MedianoController.java
│       │   ├── service/
│       │   ├── entity/Mediano.java
│       │   ├── dto/
│       │   └── repo/
│       └── resources/application.yml
│
└── photos-service/               # Photos Management
    ├── pom.xml
    └── src/main/
        ├── java/com/condocker/
        │   ├── PhotosApplication.java
        │   ├── client/MedianosFeignClient.java
        │   ├── controller/PhotoController.java
        │   ├── service/
        │   ├── entity/Photo.java
        │   ├── dto/
        │   └── repo/
        └── resources/application.yml
```

### Running Tests

```bash
# Run all tests
mvn test

# Run tests for specific service
cd medianos-service
mvn test

# Skip tests during build
mvn clean install -DskipTests
```

### Building for Production

```bash
# Build all services
mvn clean package

# JAR files will be in each service's target/ directory
# Example: medianos-service/target/medianos-service-1.0.0.jar

# Run JAR
java -jar medianos-service/target/medianos-service-1.0.0.jar
```

---

## Security Considerations

### Current Configuration (Development)

- **Eureka**: No authentication required
- **Gateway**: CORS enabled for all origins
- **Databases**: Default postgres/postgres credentials
- **JWT**: Dependencies present but not yet implemented

### Production Recommendations

1. Enable Eureka authentication
2. Configure CORS for specific origins only
3. Use environment variables for database credentials
4. Implement JWT validation in Gateway
5. Enable HTTPS/TLS
6. Add rate limiting
7. Implement API key authentication

### Security Updates

- **CVE-2025-22228**: Spring Security 6.3.4+ (mitigated)
- **CVE-2024-38827**: Security configuration implemented

---

## Additional Resources

- [Spring Cloud Documentation](https://spring.io/projects/spring-cloud)
- [Netflix Eureka](https://github.com/Netflix/eureka)
- [Spring Cloud Gateway](https://spring.io/projects/spring-cloud-gateway)
- [OpenFeign](https://github.com/OpenFeign/feign)
- [TODO.md](./TODO.md) - Planned features and improvements

---

## License

[Add your license here]

## Contributors

[Add contributors here]

---

**Last Updated**: 2025-10-26
**Version**: 1.0.0
