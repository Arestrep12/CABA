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

describe('Availability API', () => {
  it('debería registrar y recuperar disponibilidades', async () => {
    const refereeId = crypto.randomUUID();
    const slots = [
      { dayOfWeek: 1, startTime: '18:00', endTime: '20:00' },
      { dayOfWeek: 3, startTime: '19:00', endTime: '21:00' },
    ];

    await request(appServer)
      .post('/availability')
      .send({ refereeId, slots })
      .expect(204);

    const response = await request(appServer).get(`/availability/${refereeId}`).expect(200);
    expect(response.body.slots).toHaveLength(2);
  });
});

