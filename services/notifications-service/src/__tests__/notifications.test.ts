import { afterAll, beforeAll, describe, expect, it, vi } from 'vitest';
import request from 'supertest';
let appServer: import('http').Server;
let notificationQueue: import('bullmq').Queue;

vi.mock('nodemailer', () => ({
  createTransport: vi.fn().mockReturnValue({
    sendMail: vi.fn().mockResolvedValue({ accepted: ['test@caba.app'] }),
  }),
}));

beforeAll(async () => {
  process.env.NODE_ENV = 'test';
  process.env.REDIS_URL = 'redis://localhost:6379';
  process.env.MAIL_TRANSPORT = 'smtp://localhost:1025';
  process.env.MAIL_FROM = 'no-reply@caba.app';
  const { resetEnv, loadEnv } = await import('../config/env.js');
  resetEnv();
  const env = loadEnv();
  const { createApp } = await import('../index.js');
  notificationQueue = (await import('../config/queue.js')).notificationQueue;
  const app = await createApp();
  await new Promise<void>((resolve) => {
    appServer = app.listen(env.PORT, () => resolve());
  });
});

afterAll(async () => {
  if (appServer) {
    await new Promise<void>((resolve) => appServer.close(() => resolve()));
  }
  await notificationQueue.close();
});

describe('notifications-service', () => {
  it('enfile colas de email', async () => {
    await request(appServer)
      .post('/send')
      .send({
        to: 'destinatario@caba.app',
        subject: 'Test',
        template: 'Hola {{nombre}}',
        variables: { nombre: 'Árbitro' },
      })
      .expect(202);

    const jobs = await notificationQueue.getJobs(['waiting', 'delayed']);
    expect(jobs.length).toBe(1);
  });
});
