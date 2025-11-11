# Proyecto Semestral · CABA Pro

## Introducción del Proyecto

- Crear el sistema nervioso central de una institución de arbitraje de baloncesto (a adaptar a fútbol) sustituyendo procesos manuales (hojas de cálculo, WhatsApp, llamadas) por una plataforma web robusta, eficiente y escalable.
- El sistema debe cubrir desde la gestión de perfiles de árbitros, programación de partidos, comunicación oficial, hasta la liquidación de pagos.

## Requisitos Funcionales Clave

- **Gestión de personal:** CRUD completo de árbitros con datos personales, foto, especialidad (campo, mesa) y escalafón (FIBA, Primera, etc.).
- **Programación de partidos:** Asignar árbitros a roles específicos (primer árbitro, apuntador, cronometrador) según especialidad y disponibilidad.
- **Ciclo de vida del agendamiento:** Notificaciones a árbitros para aceptar/rechazar asignaciones; panel central con estado de cada asignación.
- **Gestión financiera simplificada:** Definir tarifas por escalafón y torneo; generar reportes de liquidación para pagos garantizando transparencia.

## Arquitectura de Capas y Stack Tecnológico

- **Capa de Presentación (Spring Boot Core):**
  - Framework: Spring MVC + Thymeleaf (dashboards web) y Spring REST (exponer APIs públicas).
  - Seguridad: Spring Security + JWT para sesión web/admin, OAuth2 Resource Server para consumo de microservicios.
  - Cliente HTTP: Spring Cloud OpenFeign/WebClient para orquestar microservicios Node.
- **Capa de Aplicación (Servicios):**
  - Servicios con `@Service` y `@Transactional`.
  - Manejo de eventos de dominio con Spring Application Events y mensajería (RabbitMQ/Kafka opcional).
  - Validaciones con Jakarta Bean Validation (`@Valid`, `@Constraint` custom).
- **Capa de Dominio:**
  - Entidades JPA (`@Entity`), Value Objects y agregados siguiendo DDD ligero.
  - Mapeo `MapStruct` para DTOs, lógica de negocio aislada en servicios de dominio.
- **Capa de Infraestructura:**
  - Persistencia: Spring Data JPA sobre MySQL 8.
  - Migraciones: Flyway (Java-based) y scripts SQL en `infra/mysql`.
  - Cache: Redis (Spring Data Redis) para agendas y sesiones cortas.
  - Observabilidad: Actuator + Micrometer (Prometheus/Grafana), logs con Logback estructurado (JSON).
- **Capa de Integración / BFF:**
  - API Gateway (Spring Cloud Gateway o módulo BFF) centraliza autenticación, rate limiting y routing a microservicios Express.
  - Documentación de contratos con OpenAPI 3 (springdoc) y compatibilidad con Swagger UI.
- **Microservicios Node/Express (independientes):**
  - `agenda-service`: Express + TypeScript, persistencia MongoDB/Redis para disponibilidad; zod para validación; ESLint + Prettier para estilos.
  - `notifications-service`: Express + TypeScript, integración con AWS SES (o nodemailer SMTP local) y colas BullMQ sobre Redis.
  - Tests: Vitest + supertest; cobertura mínima 80%.
- **Comunicación asíncrona:**
  - Event bus Redis Streams o RabbitMQ (infra configurable) para propagación de eventos (partido asignado, partido actualizado).
- **Infraestructura y DevOps:**
  - Contenedores Docker, orquestación local con docker-compose.
  - CI/CD: GitHub Actions (build Maven, pnpm build/test, análisis SonarCloud opcional).
  - Calidad: Husky + lint-staged, comprobaciones `mvn verify`, `pnpm lint`.

## Implementación en Spring Boot (base Maven)

- Repositorio público en GitHub con configuración colaborativa y permisos adecuados.
- Clonar repo, crear proyecto Spring Boot 3.x con Maven y subir estructura típica (`src/main/java`, `src/main/resources`, `pom.xml`, `.gitignore`).
- Compartir responsabilidades por clases o entidades del dominio (Árbitro, Partido, Torneo, etc.).
- Rol del arquitecto: redactar documentación adicional y revisar cada pull request, aplicando reglas de estilo y arquitectura.

## Reglas de Programación

- No incluir lógica de negocio en controladores; usar servicios dedicados (`PartidoService`, etc.).
- Cada ruta mapeada a su controlador correspondiente; vistas con Thymeleaf sin lógica de negocio embebida.
- Inyección de dependencias mediante `@Autowired` o constructor, evitando instancias manuales.
- Usar GitHub Projects para gestión de tareas.
- Aplicación web Spring Boot 3.x + base de datos MySQL; configuración de acceso en `application.properties`/`application.yml`.
- Implementar todas las clases del modelo de dominio (nivel C4).
- Entidades JPA (`@Entity`, `@Table`) y repositorios con `JpaRepository`/`CrudRepository`.
- Servicios (`@Service`) para lógica; controladores (`@Controller`) para interacción de usuario.
- Registro de cambios de esquema con Flyway/Liquibase o scripts SQL.
- Generar datos ficticios con scripts SQL o `CommandLineRunner`; exportar dataset para reproducción local.
- Reflejar relaciones de dominio como anotaciones JPA (ej. `@OneToMany` de `Torneo` a `Partido`).
- README con instrucciones de ejecución, URL principal (ej. `http://localhost:8080`), configuraciones adicionales.

## Secciones Principales de la Aplicación

- **Rol Árbitro (`/` o `/dashboard`):** Calendario (próximos, pasados), perfil, asignaciones; no puede crear torneos ni gestionar otros árbitros.
- **Rol Administrador (`/admin/*`):** CRUD completos al menos para entidades principales (ej. `Árbitro`, `Torneo`); vistas restringidas a usuarios `ROLE_ADMIN`.
- **Autenticación:** Diferenciar administrador de usuario normal mediante sistema de login.
- **Seguridad:** Spring Security protegiendo rutas administrativas (`/admin/**`) para `ROLE_ADMIN`.

## Funcionalidades Adicionales (mínimo 4)

- Generación de PDF para liquidaciones mensuales con resumen de partidos y montos por árbitro.
- Dashboard administrador con métricas: % de partidos aceptados vs. rechazados, top 5 árbitros activos, torneos vigentes.
- Filtro avanzado de programación por rango de fechas, torneo o árbitro específico.
- Sistema de notificaciones por email cuando se asigna o modifica un partido (ej. AWS SES).
- Documentar en Wiki: página de funcionalidades interesantes con ubicación de código (clase y método) y página con capturas de las 3 secciones clave (login, dashboard árbitro, administración de torneos).
