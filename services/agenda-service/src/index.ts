import express from 'express';
import cors from 'cors';
import helmet from 'helmet';
import { ZodError } from 'zod';
import availabilityRoutes from './routes/availability.routes.js';
import assignmentsRoutes from './routes/assignments.routes.js';
import { loadEnv } from './config/env.js';
import { connectMongo } from './config/mongo.js';
import { logger } from './config/logger.js';

export async function createApp() {
  await connectMongo();

  const app = express();
  app.use(helmet());
  app.use(cors());
  app.use(express.json());

  app.get('/health', (_req, res) => {
    res.json({ status: 'ok', service: 'agenda', uptime: process.uptime() });
  });

  app.use('/availability', availabilityRoutes);
  app.use('/assignments', assignmentsRoutes);

  app.use(
    (
      err: unknown,
      _req: express.Request,
      res: express.Response,
      next: express.NextFunction,
    ) => {
      if (err instanceof ZodError) {
        return res.status(400).json({ message: 'Validación fallida', issues: err.issues });
      }
      return next(err);
    },
  );

  app.use(
    (
      err: unknown,
      _req: express.Request,
      res: express.Response,
      _next: express.NextFunction,
    ) => {
      logger.error({ err }, 'Error inesperado');
      return res.status(500).json({ message: 'Error interno' });
    },
  );

  return app;
}

if (process.env.NODE_ENV !== 'test') {
  const env = loadEnv();
  createApp()
    .then((app) => {
      const server = app.listen(env.PORT, () => {
        logger.info({ port: env.PORT }, 'Agenda service listening');
      });

      const shutdown = () => {
        logger.info('Cerrando agenda-service...');
        server.close(() => process.exit(0));
      };

      process.on('SIGINT', shutdown);
      process.on('SIGTERM', shutdown);
    })
    .catch((error) => {
      logger.error(error, 'Fallo al iniciar agenda-service');
      process.exit(1);
    });
}

