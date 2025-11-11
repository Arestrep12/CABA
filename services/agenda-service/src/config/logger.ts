import pino from 'pino';

const nodeEnv = process.env.NODE_ENV ?? 'development';
const isProd = nodeEnv === 'production';
const isTest = nodeEnv === 'test';

export const logger = pino({
  name: 'agenda-service',
  level: isProd ? 'info' : isTest ? 'silent' : 'debug',
  transport:
    !isProd && !isTest
      ? {
          target: 'pino-pretty',
          options: {
            colorize: true,
            translateTime: 'SYS:standard',
          },
        }
      : undefined,
});

