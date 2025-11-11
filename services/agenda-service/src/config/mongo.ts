import mongoose from 'mongoose';
import { loadEnv } from './env.js';
import { logger } from './logger.js';

export async function connectMongo(): Promise<typeof mongoose> {
  mongoose.set('strictQuery', true);
  const env = loadEnv();
  try {
    const conn = await mongoose.connect(env.MONGO_URL);
    logger.info({ url: env.MONGO_URL }, 'MongoDB conectado');
    return conn;
  } catch (error) {
    logger.error(error, 'Error conectando a MongoDB');
    throw error;
  }
}

export async function disconnectMongo(): Promise<void> {
  await mongoose.disconnect();
}

