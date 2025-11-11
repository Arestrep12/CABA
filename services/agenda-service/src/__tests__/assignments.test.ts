import request from 'supertest';
import { beforeAll, afterAll, describe, it, expect } from 'vitest';
import mongoose from 'mongoose';
import { MongoMemoryServer } from 'mongodb-memory-server';
import { createApp } from '../index.js';
import { loadEnv, resetEnvCache } from '../config/env.js';

let appServer: import('http').Server;
let mongo: MongoMemoryServer;

beforeAll(async () => {
  process.env.NODE_ENV = 'test';
  mongo = await MongoMemoryServer.create();
  resetEnvCache();
  process.env.MONGO_URL = mongo.getUri();
  process.env.PORT = '0';
  const env = loadEnv();
  const app = await createApp();
  await new Promise<void>((resolve) => {
    appServer = app.listen(env.PORT, () => resolve());
  });
});

afterAll(async () => {
  await mongoose.disconnect();
  if (appServer) {
    await new Promise<void>((resolve) => appServer.close(() => resolve()));
  }
  if (mongo) {
    await mongo.stop();
  }
});

describe('Assignments API', () => {
  it('debería crear o actualizar asignaciones', async () => {
    const payload = {
      assignmentId: crypto.randomUUID(),
      refereeId: crypto.randomUUID(),
      matchId: crypto.randomUUID(),
      scheduledAt: new Date().toISOString(),
      role: 'PRIMER_ARBITRO',
    };

    await request(appServer).post('/assignments').send(payload).expect(204);

    const response = await request(appServer)
      .get(`/assignments/${payload.assignmentId}`)
      .expect(200);

    expect(response.body.assignmentId).toBe(payload.assignmentId);
  });
});

