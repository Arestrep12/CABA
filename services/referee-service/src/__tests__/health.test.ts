import request from 'supertest';
import { afterAll, beforeAll, describe, expect, it } from 'vitest';

describe('Referee Service health', () => {
  let server: import('http').Server;

  beforeAll(async () => {
    process.env.NODE_ENV = 'test';
    process.env.CORE_API_URL = 'http://localhost:8080';
    process.env.AGENDA_API_URL = 'http://localhost:4001';
    process.env.REQUEST_TIMEOUT_MS = '1000';

    const envModule = await import('../config/env.js');
    envModule.resetEnvCache();

    const { createApp } = await import('../index.js');
    const { app } = await createApp();
    await new Promise<void>((resolve) => {
      server = app.listen(0, () => resolve());
    });
  });

  afterAll(async () => {
    if (server) {
      await new Promise<void>((resolve) => server.close(() => resolve()));
    }
  });

  it('debería responder estado ok', async () => {
    const response = await request(server).get('/health').expect(200);
    expect(response.body.status).toBe('ok');
  });
});

