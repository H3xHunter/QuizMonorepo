# Microservices Ecosystem

Ecosistema de microservicios implementado con Spring Boot y Spring Cloud, utilizando Eureka Server para service discovery y API Gateway para enrutamiento centralizado.

## 📋 Estado Actual

**Infraestructura Base Implementada:**
- ✅ Eureka Server (Service Discovery)
- ✅ API Gateway (Routing & Load Balancing)
- ✅ Configuración de seguridad actualizada
- ✅ Maven multi-módulo configurado

**Servicios Implementados:**
- ✅ Auth Service (JWT Authentication) - Puerto 8081
- ✅ Medianos Service - Puerto 8082
- ✅ Photos Service - Puerto 8083

## 🏗️ Arquitectura

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Client/Web    │────│   API Gateway   │────│ Eureka Server   │
│                 │    │   Port: 8080    │    │   Port: 8761    │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                              │
                              ├── Auth Service (8081) ✅
                              ├── Medianos Service (8082) ✅
                              └── Photos Service (8083) ✅
```

## 🛠️ Tecnologías

- **Java 21**
- **Spring Boot 3.3.5**
- **Spring Cloud 2023.0.3**
- **Netflix Eureka** (Service Discovery)
- **Spring Cloud Gateway** (API Gateway)
- **Maven** (Multi-módulo)
- **PostgreSQL 16** (3 instancias separadas en Docker)
- **Docker & Docker Compose** (Orquestación de bases de datos)

## 📁 Estructura del Proyecto

```
microservices-ecosystem/
├── pom.xml                           # Parent POM
├── README.md                         # Este archivo
├── CLAUDE.md                         # Claude Code instructions
├── .gitignore
├── eureka-server/                    # Service Discovery
│   ├── pom.xml
│   ├── src/main/java/com/condocker/eureka/
│   │   ├── EurekaServerApplication.java
│   │   └── config/EurekaSecurityConfig.java
│   └── src/main/resources/
│       └── application.yml
├── api-gateway/                      # API Gateway
│   ├── pom.xml
│   ├── src/main/java/com/condocker/gateway/
│   │   └── ApiGatewayApplication.java
│   └── src/main/resources/
│       └── application.yml
├── auth-service/                     # Authentication Service ✅
│   ├── pom.xml
│   ├── src/main/java/com/condocker/
│   │   ├── AuthApplication.java
│   │   ├── controller/
│   │   ├── service/
│   │   └── entity/
│   └── src/main/resources/
│       └── application.yml
├── medianos-service/                 # Medianos Service ✅
│   ├── pom.xml
│   ├── src/main/java/com/condocker/
│   │   ├── MedianosServiceApplication.java
│   │   ├── controller/
│   │   ├── service/
│   │   └── entity/
│   └── src/main/resources/
│       └── application.yml
└── photos-service/                   # Photos Service ✅
    ├── pom.xml
    ├── src/main/java/com/condocker/
    │   ├── PhotosApplication.java
    │   ├── controller/
    │   ├── service/
    │   └── entity/
    └── src/main/resources/
        └── application.yml
```

## 🚀 Cómo Ejecutar

### Prerequisitos
- Java 21 JDK
- Maven 3.8+
- Docker Desktop (para bases de datos PostgreSQL)
- IntelliJ IDEA (recomendado)

### Paso 1: Clonar y Configurar
```bash
git clone <repository-url>
cd microservices-ecosystem
mvn clean compile
```

### Paso 2: Iniciar Bases de Datos PostgreSQL (DOCKER)
```bash
# Iniciar las 3 instancias de PostgreSQL
docker-compose up -d

# Verificar que estén corriendo
docker-compose ps

# Ver logs si hay problemas
docker-compose logs -f
```

**Bases de datos creadas:**
- `auth-db`: PostgreSQL en **localhost:5432** (database: `auth_db`)
- `photos-db`: PostgreSQL en **localhost:5433** (database: `photos_db`)
- `medianos-db`: PostgreSQL en **localhost:5434** (database: `medianos_db`)

Credenciales por defecto para todas: `postgres / postgres`

### Paso 3: Ejecutar Servicios (Orden Importante)

#### 3.1 Iniciar Eureka Server (PRIMERO)
```bash
# En IntelliJ: Run EurekaServerApplication
# O en terminal:
cd eureka-server
mvn spring-boot:run
```
**Verificar**: http://localhost:8761

#### 3.2 Iniciar API Gateway (SEGUNDO)
```bash
# En IntelliJ: Run ApiGatewayApplication
# O en terminal:
cd api-gateway
mvn spring-boot:run
```
**Verificar**: http://localhost:8080/actuator/health

#### 3.3 Iniciar Servicios de Negocio (DESPUÉS de Eureka y Gateway)
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
**Verificar en Eureka Dashboard**: http://localhost:8761 (deben aparecer los 3 servicios registrados)

### Paso 4: Verificar Funcionamiento
1. **Dashboard Eureka**: http://localhost:8761
    - Debe mostrar `API-GATEWAY`, `AUTH-SERVICE`, `MEDIANOS-SERVICE`, y `PHOTOS-SERVICE` registrados
2. **Gateway Health**: http://localhost:8080/actuator/health
    - Debe responder `{"status":"UP"}`
3. **Service Health Checks**:
    - Auth Service: http://localhost:8081/actuator/health
    - Medianos Service: http://localhost:8082/actuator/health
    - Photos Service: http://localhost:8083/actuator/health
4. **Eureka via Gateway**: http://localhost:8080/eureka/
    - Debe mostrar dashboard de Eureka

## 🐳 Gestión de Bases de Datos Docker

### Comandos Docker Compose
```bash
# Iniciar todas las bases de datos
docker-compose up -d

# Detener todas las bases de datos
docker-compose down

# Detener y ELIMINAR TODOS LOS DATOS (cuidado!)
docker-compose down -v

# Ver estado de los contenedores
docker-compose ps

# Ver logs en tiempo real
docker-compose logs -f

# Reiniciar una base de datos específica
docker-compose restart auth-db
docker-compose restart photos-db
docker-compose restart medianos-db
```

### Conectar a PostgreSQL Directamente

**Usando psql desde el contenedor:**
```bash
# Auth database
docker exec -it auth-postgres psql -U postgres -d auth_db

# Photos database
docker exec -it photos-postgres psql -U postgres -d photos_db

# Medianos database
docker exec -it medianos-postgres psql -U postgres -d medianos_db
```

**Usando herramientas GUI (DBeaver, pgAdmin, DataGrip):**

| Servicio | Host | Port | Database | Username | Password |
|----------|------|------|----------|----------|----------|
| Auth Service | localhost | **5432** | auth_db | postgres | postgres |
| Photos Service | localhost | **5433** | photos_db | postgres | postgres |
| Medianos Service | localhost | **5434** | medianos_db | postgres | postgres |

### Persistencia de Datos

Los datos se almacenan en volúmenes Docker persistentes:
- `auth-db-data` - Datos del servicio de autenticación
- `photos-db-data` - Datos del servicio de fotos
- `medianos-db-data` - Datos del servicio medianos

Los datos persisten aunque se detenga el contenedor. Solo se borran con `docker-compose down -v`.

## 🔗 Endpoints Disponibles

### Eureka Server (Puerto 8761)
| Endpoint | Descripción |
|----------|-------------|
| `http://localhost:8761` | Dashboard de Eureka |
| `http://localhost:8761/actuator/health` | Health check |
| `http://localhost:8761/actuator/info` | Información del servicio |

### API Gateway (Puerto 8080)
| Endpoint | Descripción |
|----------|-------------|
| `http://localhost:8080/actuator/health` | Health check |
| `http://localhost:8080/actuator/gateway/routes` | Rutas configuradas |
| `http://localhost:8080/eureka/` | Dashboard Eureka via Gateway |

## 🧪 Testing

### Quick Tests con HTTP Client
```http
### Test Eureka Health
GET http://localhost:8761/actuator/health

### Test Gateway Health
GET http://localhost:8080/actuator/health

### Test Gateway Routes
GET http://localhost:8080/actuator/gateway/routes

### Test Eureka through Gateway
GET http://localhost:8080/eureka/
```

### Con curl
```bash
# Test básico de conectividad
curl http://localhost:8761/actuator/health
curl http://localhost:8080/actuator/health

# Ver rutas del Gateway
curl http://localhost:8080/actuator/gateway/routes
```

## ⚠️ Seguridad

### Vulnerabilidades Mitigadas
- **CVE-2025-22228**: Spring Security BCryptPasswordEncoder - Actualizado a versión segura
- **CVE-2024-38827**: Improper Authorization - Configuración de seguridad implementada

### Configuración de Seguridad
- Eureka Server: Autenticación deshabilitada para desarrollo
- CORS configurado en Gateway para desarrollo
- Rate limiting: Por implementar

## 📝 TODO - Próximos Pasos

### Fase 1: Migración de Servicios ✅
- [x] Migrar Auth Service al monorepo
- [x] Migrar Medianos Service al monorepo
- [x] Migrar Photos Service al monorepo
- [x] Configurar PostgreSQL para cada servicio (3 instancias Docker separadas)
- [x] Configurar docker-compose.yml para orquestación de bases de datos
- [x] Configurar health checks para PostgreSQL

### Fase 2: Comunicación Inter-Servicios
- [ ] Implementar OpenFeign entre servicios
- [ ] Configurar Resilience4j (Circuit Breaker, Retry)
- [ ] Implementar tracing con Correlation ID

### Fase 3: Containerización
- [x] Configurar docker-compose para bases de datos PostgreSQL
- [x] Configurar variables de entorno para conexiones de DB
- [ ] Crear Dockerfiles para cada microservicio
- [ ] Extender docker-compose.yml para incluir microservicios
- [ ] Configurar networking entre contenedores

### Fase 4: Seguridad y Observabilidad
- [ ] Integrar JWT validation en Gateway
- [ ] Configurar logging centralizado
- [ ] Implementar métricas personalizadas
- [ ] Crear colección Postman/Bruno

## 🐛 Troubleshooting

### Servicios no pueden conectar a PostgreSQL

```bash
# 1. Verificar que los contenedores Docker están corriendo
docker-compose ps

# 2. Ver logs de la base de datos
docker-compose logs auth-db
docker-compose logs photos-db
docker-compose logs medianos-db

# 3. Verificar que los puertos no estén ocupados
netstat -ano | findstr :5432
netstat -ano | findstr :5433
netstat -ano | findstr :5434

# 4. Reiniciar contenedores específicos
docker-compose restart auth-db

# 5. Reiniciar completamente (último recurso)
docker-compose down
docker-compose up -d
```

**Error común**: `Connection to localhost:5432 refused`
- **Solución**: Verificar que `docker-compose up -d` se ejecutó correctamente
- **Solución 2**: En Windows, verificar que Docker Desktop está corriendo

### Resetear base de datos completamente

```bash
# ADVERTENCIA: Esto borra TODOS los datos
docker-compose down -v
docker-compose up -d
```

### Health check de bases de datos

```bash
# Verificar salud de contenedores
docker-compose ps

# Si un contenedor está "unhealthy", revisar logs
docker-compose logs <nombre-servicio>
```

### Eureka Server no arranca
```bash
# Verificar puerto 8761 disponible
netstat -an | findstr 8761  # Windows
lsof -i :8761              # Mac/Linux

# Si está ocupado, cambiar puerto en application.yml
```

### Gateway no se registra en Eureka
```bash
# Verificar logs del Gateway para errores de conexión
# Verificar configuración eureka.client.service-url.defaultZone
```

### Dependencias no se descargan
```bash
mvn clean compile
mvn dependency:resolve
# En IntelliJ: Maven Tool Window → Reload All Projects
```

### Build failures
```bash
# Limpiar y recompilar
mvn clean install -DskipTests
# En IntelliJ: Build → Rebuild Project
```

---

## 📊 Estado de Servicios

| Servicio | Estado | Puerto | Endpoint Health | Base de Datos | DB Port |
|----------|--------|--------|----------------|---------------|---------|
| Eureka Server | ✅ Funcionando | 8761 | `/actuator/health` | N/A | - |
| API Gateway | ✅ Funcionando | 8080 | `/actuator/health` | N/A | - |
| Auth Service | ✅ Integrado | 8081 | `/actuator/health` | PostgreSQL (auth_db) | 5432 |
| Medianos Service | ✅ Integrado | 8082 | `/actuator/health` | PostgreSQL (medianos_db) | 5434 |
| Photos Service | ✅ Integrado | 8083 | `/actuator/health` | PostgreSQL (photos_db) | 5433 |

**Última actualización**: Bases de datos PostgreSQL configuradas en Docker (3 instancias separadas para aislamiento por servicio)