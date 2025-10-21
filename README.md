# Microservices Ecosystem

Ecosistema de microservicios implementado con Spring Boot y Spring Cloud, utilizando Eureka Server para service discovery y API Gateway para enrutamiento centralizado.

## 📋 Estado Actual

**Infraestructura Base Implementada:**
- ✅ Eureka Server (Service Discovery)
- ✅ API Gateway (Routing & Load Balancing)
- ✅ Configuración de seguridad actualizada
- ✅ Maven multi-módulo configurado

**Servicios por Implementar:**
- 🔄 Auth Service (JWT Authentication)
- 🔄 Medianos Service
- 🔄 Fotos Service

## 🏗️ Arquitectura

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Client/Web    │────│   API Gateway   │────│ Eureka Server   │
│                 │    │   Port: 8080    │    │   Port: 8761    │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                              │
                              ├── Auth Service (8081) [Por implementar]
                              ├── Medianos Service (8082) [Por implementar]
                              └── Fotos Service (8083) [Por implementar]
```

## 🛠️ Tecnologías

- **Java 21**
- **Spring Boot 3.3.5**
- **Spring Cloud 2023.0.3**
- **Netflix Eureka** (Service Discovery)
- **Spring Cloud Gateway** (API Gateway)
- **Maven** (Multi-módulo)
- **PostgreSQL** (Planned for services)
- **Docker** (Planned)

## 📁 Estructura del Proyecto

```
microservices-ecosystem/
├── pom.xml                           # Parent POM
├── README.md                         # Este archivo
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
└── [auth-service]/                   # Por migrar
└── [medianos-service]/               # Por migrar
└── [fotos-service]/                  # Por migrar
```

## 🚀 Cómo Ejecutar

### Prerequisitos
- Java 21 JDK
- Maven 3.8+
- IntelliJ IDEA (recomendado)

### Paso 1: Clonar y Configurar
```bash
git clone <repository-url>
cd microservices-ecosystem
mvn clean compile
```

### Paso 2: Ejecutar Servicios (Orden Importante)

#### 2.1 Iniciar Eureka Server (PRIMERO)
```bash
# En IntelliJ: Run EurekaServerApplication
# O en terminal:
cd eureka-server
mvn spring-boot:run
```
**Verificar**: http://localhost:8761

#### 2.2 Iniciar API Gateway (SEGUNDO)
```bash
# En IntelliJ: Run ApiGatewayApplication  
# O en terminal:
cd api-gateway
mvn spring-boot:run
```
**Verificar**: http://localhost:8080/actuator/health

### Paso 3: Verificar Funcionamiento
1. **Dashboard Eureka**: http://localhost:8761
    - Debe mostrar `API-GATEWAY` registrado
2. **Gateway Health**: http://localhost:8080/actuator/health
    - Debe responder `{"status":"UP"}`
3. **Eureka via Gateway**: http://localhost:8080/eureka/
    - Debe mostrar dashboard de Eureka

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

### Fase 1: Migración de Servicios
- [ ] Migrar Auth Service al monorepo
- [ ] Migrar Medianos Service al monorepo
- [ ] Migrar Fotos Service al monorepo
- [ ] Configurar PostgreSQL para cada servicio

### Fase 2: Comunicación Inter-Servicios
- [ ] Implementar OpenFeign entre servicios
- [ ] Configurar Resilience4j (Circuit Breaker, Retry)
- [ ] Implementar tracing con Correlation ID

### Fase 3: Containerización
- [ ] Crear Dockerfiles para cada servicio
- [ ] Configurar docker-compose para orquestación
- [ ] Configurar variables de entorno

### Fase 4: Seguridad y Observabilidad
- [ ] Integrar JWT validation en Gateway
- [ ] Configurar logging centralizado
- [ ] Implementar métricas personalizadas
- [ ] Crear colección Postman/Bruno

## 🐛 Troubleshooting

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

| Servicio | Estado | Puerto | Endpoint Health |
|----------|--------|--------|----------------|
| Eureka Server | ✅ Funcionando | 8761 | `/actuator/health` |
| API Gateway | ✅ Funcionando | 8080 | `/actuator/health` |
| Auth Service | 🔄 Por migrar | 8081 | - |
| Medianos Service | 🔄 Por migrar | 8082 | - |
| Fotos Service | 🔄 Por migrar | 8083 | - |

**Última actualización**: Configuración inicial de infraestructura completada