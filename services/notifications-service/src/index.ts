import express from 'express';
import helmet from 'helmet';
import { ZodError, z } from 'zod';
import { loadEnv } from './config/env.js';
import { logger } from './config/logger.js';
import { notificationQueue } from './config/queue.js';

const notificationSchema = z.object({
  to: z.string().email(),
  subject: z.string().min(1),
  template: z.string().min(1),
  variables: z.record(z.any()).optional(),
});

export async function createApp() {
  loadEnv();
  const app = express();
  app.use(helmet());
  app.use(express.json());

  app.get('/health', (_req, res) => {
    res.json({ status: 'ok', service: 'notifications', uptime: process.uptime() });
  });

  app.post('/send', async (req, res, next) => {
    try {
      const payload = notificationSchema.parse(req.body);
      await notificationQueue.add('email', payload, { attempts: 3, backoff: { type: 'exponential', delay: 5000 } });
      return res.status(202).json({ accepted: true });
    } catch (error) {
      next(error);
    }
  });

  app.use(
    (
      err: unknown,
      _req: express.Request,
      res: express.Response,
      next: express.NextFunction,
    ) => {
      if (err instanceof ZodError) {
        return res.status(400).json({ message: 'Datos inválidos', issues: err.issues });
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
      logger.error({ err }, 'Error inesperado notifications-service');
      return res.status(500).json({ message: 'Error interno' });
    },
  );

  return app;
}

if (process.env.NODE_ENV !== 'test') {
  const env = loadEnv();
  createApp()
    .then((app) => {
      app.listen(env.PORT, () => logger.info({ port: env.PORT }, 'Notifications service listening'));
    })
    .catch((error) => {
      logger.error(error, 'No se pudo iniciar notifications-service');
      process.exit(1);
    });
}

