import express from 'express';
import cors from 'cors';
import helmet from 'helmet';
import { loadEnv } from './config/env.js';
import { logger } from './config/logger.js';
import authRoutes from './routes/auth.routes.js';
import dashboardRoutes from './routes/dashboard.routes.js';
import liquidacionesRoutes from './routes/liquidaciones.routes.js';
import availabilityRoutes from './routes/availability.routes.js';

export async function createApp() {
  const env = loadEnv();

  const app = express();
  app.use(helmet());
  app.use(cors());
  app.use(express.json());

  app.get('/health', (_req, res) => {
    res.json({ status: 'ok', service: 'referee', uptime: process.uptime() });
  });

  app.use('/auth', authRoutes);
  app.use('/dashboard', dashboardRoutes);
  app.use('/liquidaciones', liquidacionesRoutes);
  app.use('/availability', availabilityRoutes);

  app.use((err: unknown, _req: express.Request, res: express.Response, _next: express.NextFunction) => {
    logger.error({ err }, 'Error inesperado en referee-service');
    res.status(500).json({ message: 'Error interno en referee-service' });
  });

  return { app, env };
}

if (process.env.NODE_ENV !== 'test') {
  createApp()
    .then(({ app, env }) => {
      const server = app.listen(env.PORT, () => {
        logger.info({ port: env.PORT }, 'Referee service listening');
      });

      const gracefulShutdown = () => {
        logger.info('Cerrando referee-service...');
        server.close(() => process.exit(0));
      };

      process.on('SIGINT', gracefulShutdown);
      process.on('SIGTERM', gracefulShutdown);
    })
    .catch((error) => {
      logger.error({ error }, 'No se pudo iniciar referee-service');
      process.exit(1);
    });
}

