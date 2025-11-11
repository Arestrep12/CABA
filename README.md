## CABA Pro · Plataforma Integral de Arbitraje

Monorepo que orquesta el sistema central de CABA Pro: incluye el núcleo Spring Boot, gateway BFF, microservicios Node para agenda y notificaciones, además del nuevo `referee-service` pensado como BFF ligero para árbitros.

### Arquitectura de servicios

- `core-app`: Spring Boot 3 (MySQL) con dominio, liquidaciones, seguridad JWT, OpenAPI y generación de reportes PDF/Excel.
- `bff-gateway`: Gateway Spring MVC que centraliza tráfico público hacia `core-app`, agenda y notificaciones.
- `agenda-service`: Microservicio Express/TypeScript (MongoDB + Redis opcional) para disponibilidades y sincronización de asignaciones.
- `notifications-service`: Express/TypeScript (Redis + SMTP) para colas BullMQ y envío de emails.
- `referee-service`: Nuevo BFF Express/TypeScript que expone endpoints específicos para árbitros consumiendo `core-app` y `agenda-service`.

### Requisitos locales

- Java 21 + Maven 3.9
- Node 20 + pnpm ≥ 8 (activado con `corepack enable`)
- Docker / Docker Compose (para el stack contenedorizado)

### Ejecutar en desarrollo

```bash
# instalar dependencias
pnpm install

# services node (watch mode individual)
pnpm --filter @caba/agenda-service dev
pnpm --filter @caba/notifications-service dev
pnpm --filter @caba/referee-service dev

# core Spring Boot
cd core-app
mvn spring-boot:run
```

Variables clave (default en `application.yml`):

- `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`
- `AGENDA_SERVICE_URL`, `NOTIFICATIONS_SERVICE_URL`
- `integrations.openweather.*` (habilita clima en dashboard; requiere API key de OpenWeather)

### API pública y documentación

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

Endpoints relevantes recién añadidos:

- `GET /api/dashboard/resumen`
- `GET /api/liquidaciones/{id}/reporte?formato=pdf|xlsx`
- `POST /api/liquidaciones/arbitro/{arbitroId}`

### Referee Service (nuevo)

Variables necesarias (`services/referee-service`):

```bash
CORE_API_URL=http://localhost:8080
AGENDA_API_URL=http://localhost:4001
PORT=4003
```

Endpoints expuestos:

- `POST /auth/login` → proxy a `/api/auth/login` del core.
- `GET /dashboard` → resumen con clima y métricas.
- `GET /liquidaciones?arbitroId=...`
- `GET /liquidaciones/:id/reporte?formato=pdf|xlsx`
- `GET/POST /availability` → disponibilidad vía agenda-service.

### Docker Compose

Genera imágenes multi-stage por servicio (`Dockerfile` en cada módulo) y una orquestación completa:

```bash
docker compose up --build
```

Servicios publicados:

- `core-app`: `http://localhost:8080`
- `bff-gateway`: `http://localhost:8081`
- `referee-service`: `http://localhost:4003`
- `agenda-service`: `http://localhost:4001`
- `notifications-service`: `http://localhost:4002`
- `mailhog`: `http://localhost:8025`

Dependencias incluidas: MySQL 8.4, MongoDB 7, Redis 7.2.

### Reportes PDF / Excel

El `LiquidacionService` ahora aplica inversión de dependencias con `ReporteGenerador` y dos implementaciones (`PdfReporteGenerador`, `ExcelReporteGenerador`). Se generan `byte[]` vía OpenPDF y Apache POI.

Para descargar:

```
GET /api/liquidaciones/{id}/reporte?formato=pdf
Authorization: Bearer <token>
```

### Tests y lint

- `mvn -pl core-app test` *(actualmente bloqueado por dependencias Lombok del módulo legado; ver notas en el informe final)*.
- `pnpm --filter @caba/agenda-service test`
- `pnpm --filter @caba/notifications-service test`
- `pnpm --filter @caba/referee-service test`

### Notas de entorno

- Configurar `OPENWEATHER_API_KEY` y `OPENWEATHER_ENABLED=true` para habilitar clima real.
- SMTP local expuesto vía MailHog (`MAIL_TRANSPORT=smtp://mailhog:1025`, `MAIL_FROM=notificaciones@caba.local`).
- Redis y Mongo usan volúmenes nombrados para facilitar persistencia.

